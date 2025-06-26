package com.alone.coder.module.seepro.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.extra.ssh.Sftp;
import com.jcraft.jsch.*;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JschUtils {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(JschUtils.class);

    private static final int TIMEOUT = 5 * 60 * 1000;

    /**
     * 获取session
     *
     * @param host     ip
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return Session
     */
    public static Session getSession(String host, int port, String username, String password) {
        return JschUtil.openSession(host, port, username, password,1000);
    }

    /**
     * 异步执行,不需要结果
     *
     * @param session Session
     * @param cmdLs   命令
     */
    public static String execCmd(Session session, List<String> cmdLs) {
        String finalCmd = CollUtil.join(cmdLs, " && ");
        return JschUtil.exec(session, finalCmd, StandardCharsets.UTF_8);
    }


    /**
     * 获取文件列表
     *
     * @param session    Session
     * @param remotePath 远程目录地址
     * @return 文件列表
     */
    public static List<Map> getFileList(Session session, String remotePath) {
        Sftp sftp = null;
        try {
            sftp = JschUtil.createSftp(session);
            List<Map> fileList = new ArrayList<>();
            List<ChannelSftp.LsEntry> files = sftp.lsEntries(remotePath);
            for (ChannelSftp.LsEntry entry : files) {
                SftpATTRS attrs = entry.getAttrs();
                String permissions = attrs.getPermissionsString();  // 权限，如 -rw-r--r--
                long fileSize = attrs.getSize();                    // 大小（字节）
                int mTime = attrs.getMTime();                       // 修改时间（秒时间戳）
                String timeStr = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date((long) mTime * 1000));
                Map<String, Object> entryMap = new HashMap<>();
                entryMap.put("name", entry.getFilename());
                entryMap.put("permissions", permissions);
                entryMap.put("size", fileSize);
                entryMap.put("date", timeStr);
            }
            return fileList;
        } finally {
            sftp.close();
        }
    }

    /**
     * 上传文件,相同路径ui覆盖
     *
     * @param session    Session
     * @param remotePath 远程目录地址
     * @param uploadFile 文件 File
     */
    public static void uploadFile(Session session, String remotePath, File uploadFile) {
        try {
            Sftp sftp = JschUtil.createSftp(session);
            sftp.upload(remotePath, uploadFile);
            sftp.close();
            LOG.info("File upload successfully: {}", uploadFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查命令
     *
     * @param rawCmd 原始命令
     * @return 命令
     */
    public static String checkExecCmd(String rawCmd) {
        // 禁止交互式命令无参数运行
        List<String> forbidden = List.of("cat", "top", "vim", "less", "more");
        for (String cmd : forbidden) {
            // 正则避免空格变种
            if (rawCmd.trim().equals(cmd)) {
                throw new RuntimeException("命令 [" + cmd + "] 需要参数，否则会卡死远程连接，已拦截执行。");
            }
        }
        return rawCmd;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String cwd = "";
        Session session = getSession("47.112.168.8", 22, "root", "lh20011210@");
        if (session == null || !session.isConnected()) {
            System.err.println("SSH连接失败");
            return;
        }
        cwd = execCmd(session, List.of("pwd")).trim(); // 初始目录
        while (true) {
            try {
                System.out.print("[" + cwd + "] 命令: ");
                String cmd = scanner.nextLine().trim();

                if ("exit".equalsIgnoreCase(cmd)) {
                    session.disconnect();
                    System.out.println("已断开连接。");
                    break;
                }
                cmd = checkExecCmd(cmd);
                // 只处理单独的 cd 命令
                if (cmd.startsWith("cd ") && !cmd.contains("&&")) {
                    String target = cmd.substring(3).trim();
                    String newCwd = execCmd(session, Arrays.asList("cd " + cwd, "cd " + target, "pwd")).trim();
                    if (!StrUtil.isEmpty(newCwd)) {
                        cwd = newCwd;
                    } else {
                        System.out.println("目录切换失败");
                    }
                    continue;
                }

                // 如果是复合命令，比如包含了 && 或 ;，就直接执行
                List<String> cmdList = new ArrayList<>();
                if (cmd.contains("&&") || cmd.contains(";")) {
                    cmdList.add(cmd);
                } else {
                    cmdList.add("cd " + cwd);
                    cmdList.add(cmd);
                }

                String result = execCmd(session, cmdList);
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}



