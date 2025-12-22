package com.alone.coder.framework.portal.core.packet;


import cn.hutool.crypto.digest.MD5;
import com.alone.coder.framework.portal.core.PortalException;
import com.alone.coder.framework.portal.core.enums.PortalAuthTypeEnums;
import com.alone.coder.framework.portal.core.utils.PortalUtils;
import lombok.Data;
import org.apache.mina.core.buffer.IoBuffer;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alone.coder.framework.portal.core.enums.PortalAuthTypeEnums.AUTH_CHAP;
import static com.alone.coder.framework.portal.core.enums.PortalAuthTypeEnums.AUTH_PAP;
import static com.alone.coder.framework.portal.core.enums.PortalPacketVersionEnums.HUAWEIV1_TYPE;
import static com.alone.coder.framework.portal.core.enums.PortalPacketVersionEnums.HUAWEIV2_TYPE;

@Data
public class PortalPacket {


    public static final int MAX_PACKET_LENGTH = 1024;
    public static final Map<Integer, String> ACK_CHALLENGE_ERRORS = new HashMap<Integer, String>();
    public static final Map<Integer, String> ACK_AUTH_ERRORS = new HashMap<Integer, String>();
    public static final Map<Integer, String> ACK_LOGOUT_ERRORS = new HashMap<Integer, String>();
    public static final Map<Integer, String> ACK_INFO_ERRORS = new HashMap<Integer, String>();

    static {
        //ACK_CHALLENGE_ERRORS
        ACK_CHALLENGE_ERRORS.put(0, "请求Challenge成功");
        ACK_CHALLENGE_ERRORS.put(1, "请求Challenge被拒绝");
        ACK_CHALLENGE_ERRORS.put(2, "链接已经建立");
        ACK_CHALLENGE_ERRORS.put(3, "有一个用户正在认证过程中，请稍后再试");
        ACK_CHALLENGE_ERRORS.put(4, "请求Challenge失败");
        //ACK_AUTH_ERRORS
        ACK_AUTH_ERRORS.put(0, "用户认证请求成功");
        ACK_AUTH_ERRORS.put(1, "用户认证请求被拒绝");
        ACK_AUTH_ERRORS.put(2, "链接已经建立");
        ACK_AUTH_ERRORS.put(3, "有一个用户正在认证过程中，请稍后再试");
        ACK_AUTH_ERRORS.put(4, "用户认证失败");
        //ACK_LOGOUT_ERRORS
        ACK_LOGOUT_ERRORS.put(0, "用户下线成功");
        ACK_LOGOUT_ERRORS.put(1, "用户下线被拒绝");
        ACK_LOGOUT_ERRORS.put(2, "用户下线失败");
        ACK_LOGOUT_ERRORS.put(3, "用户已经离线");
        //ACK_INFO_ERRORS
        ACK_INFO_ERRORS.put(0, "SUCCESS");
        ACK_INFO_ERRORS.put(1, "功能不被支持");
        ACK_INFO_ERRORS.put(2, "消息处理失败");
    }

    /**
     * 协议版本
     */
    private int ver = 0x01;

    /**
     * 报文类型
     */
    private int type;

    /**
     * 认证方式
     */
    private int isChap = AUTH_PAP.getValue();

    /**
     * 认证结果
     */
    private int rsv = 0;

    /**
     * 序列号
     */
    private short serialNo = 0;
    /**
     * 请求ID
     */
    private short reqId = 0;

    /**
     * 用户IP
     */
    private String userIp;
    /**
     * 用户端口
     */
    private short userPort = 0;
    /**
     * 错误码
     */
    private int errCode = 0;

    /**
     * 属性个数
     */
    private int attrNum = 0;
    /**
     * 认证数据
     */
    private byte[] authenticator;
    /**
     * 密码
     */
    private String secret;

    /**
     * 属性
     */
    private List<PortalAttribute> attributes = new ArrayList<>();



    public PortalPacket() {
    }

    public PortalPacket(byte[] src) throws PortalException {
        this.decodePacket(IoBuffer.wrap(src));
    }

    /**
     * 构造函数
     *
     * @param ver          协议版本
     * @param type         报文类型
     * @param userIp       用户IP
     * @param serialNo     序列号
     * @param reqId        请求ID
     * @param secret       密码
     * @param authTypeEnum 认证方式
     */
    public PortalPacket(int ver, int type, String userIp, short serialNo, short reqId, String secret, PortalAuthTypeEnums authTypeEnum) {
        setVer(ver);
        setType(type);
        setUserIp(userIp);
        setSerialNo(serialNo);
        setReqId(reqId);
        setSecret(secret);
        setIsChap(authTypeEnum.getValue());
    }

    /**
     * 报文解码
     *
     * @param buff 数据源
     * @throws PortalException 异常
     */
    private void decodePacket(IoBuffer buff) throws PortalException {
        buff.rewind();
        if (buff.remaining() > MAX_PACKET_LENGTH) {
            throw new PortalException("Packet size is too large");
        }
        byte ver = buff.get();
        if (ver != HUAWEIV1_TYPE.getValue() && ver != HUAWEIV2_TYPE.getValue()) {
            throw new PortalException("Packet ver error");
        }
        setVer(ver);
        setType(buff.get());
        byte isChap = buff.get();
        if (isChap != AUTH_CHAP.getValue() && isChap != AUTH_PAP.getValue()) {
            throw new PortalException("Packet chap/pap error");
        }
        setIsChap(isChap);
        setRsv(buff.get());
        setSerialNo(buff.getShort());
        setReqId(buff.getShort());
        byte[] userIpData = new byte[4];
        buff.get(userIpData);
        setUserIp(PortalUtils.decodeIpv4(userIpData));
        setUserPort(buff.getShort());
        setErrCode(buff.get());
        setAttrNum(buff.get());
        if (getVer() == HUAWEIV2_TYPE.getValue()) {
            byte[] auth = new byte[16];
            buff.get(auth);
            setAuthenticator(auth);
        }
        for (int i = 0; i < attrNum; i++) {
            PortalAttribute attr = new PortalAttribute();
            attr.setAttributeType(buff.get());
            int len = buff.get();
            if (len == 2)
                continue;
            if (len == 0) {
                continue;
            }
            byte[] attrData = new byte[len - 2];
            buff.get(attrData);
            attr.setAttributeData(attrData);
            attributes.add(attr);
        }
    }

    /**
     * 编码报文
     *
     * @return IoBuffer 数据源
     * @throws PortalException 错误
     */
    public IoBuffer encodePacket() throws PortalException {
        IoBuffer buffer = IoBuffer.allocate(16);
        buffer.setAutoExpand(true);
        buffer.put((byte) getVer());
        buffer.put((byte) getType());
        buffer.put((byte) getIsChap());
        buffer.put((byte) getRsv());
        buffer.putShort(getSerialNo());
        buffer.putShort(getReqId());
        buffer.put(PortalUtils.encodeIpV4(getUserIp()));
        buffer.putShort(getUserPort());
        buffer.put((byte) getErrCode());
        buffer.put((byte) getAttrNum());
        if (getVer() == HUAWEIV2_TYPE.getValue()) {
            byte[] auth = getAuthenticator();
            if (auth == null) {
                throw new PortalException("Request authenticator is empty");
            }
            buffer.put(getAuthenticator());
        }
        for (PortalAttribute attr : getAttributes()) {
            buffer.put(attr.encodeAttribute());
        }
        buffer.flip();
        return buffer;
    }


    public byte[] getAuthenticator() {
        if (authenticator == null) {
            if (getVer() == HUAWEIV2_TYPE.getValue()) {
                setAuthenticator(createRequestAuthenticator(secret));
            }
        }
        return authenticator;
    }


    /**
     * 创建请求验证字
     *
     * @param sharedSecret 共享密钥
     * @return 加密请求
     */
    protected byte[] createRequestAuthenticator(String sharedSecret) {
        byte[] randomBytes = new byte[16];
        MessageDigest md5 = MD5.create().getDigest();
        md5.reset();
        md5.update((byte) getVer());
        md5.update((byte) getType());
        md5.update((byte) getIsChap());
        md5.update((byte) getRsv());
        md5.update(PortalUtils.encodeShort(getSerialNo()));
        md5.update(PortalUtils.encodeShort(getReqId()));
        md5.update(PortalUtils.encodeIpV4(getUserIp()));
        md5.update(PortalUtils.encodeShort(getUserPort()));
        md5.update((byte) getErrCode());
        md5.update((byte) attributes.size());
        md5.update(randomBytes);
        for (PortalAttribute attr : attributes) {
            md5.update(attr.encodeAttribute());
        }
        md5.update(PortalUtils.encodeString(sharedSecret));
        return md5.digest();
    }


    /**
     * 创建响应验证字
     *
     * @param sharedSecret         密钥
     * @param requestAuthenticator 请求验证
     * @return 响应验证
     */
    protected byte[] createResponseAuthenticator(String sharedSecret, byte[] requestAuthenticator) {
        MessageDigest md5 = MD5.create().getDigest();
        md5.reset();
        md5.update((byte) getVer());
        md5.update((byte) getType());
        md5.update((byte) getIsChap());
        md5.update((byte) getRsv());
        md5.update(PortalUtils.encodeShort(getSerialNo()));
        md5.update(PortalUtils.encodeShort(getReqId()));
        md5.update(PortalUtils.encodeIpV4(getUserIp()));
        md5.update(PortalUtils.encodeShort(getUserPort()));
        md5.update((byte) getErrCode());
        md5.update((byte) attributes.size());
        md5.update(requestAuthenticator);
        for (PortalAttribute attr : attributes) {
            md5.update(attr.encodeAttribute());
        }
        md5.update(PortalUtils.encodeString(sharedSecret));
        return md5.digest();
    }

    /**
     * 校验响应验证字
     *
     * @param sharedSecret         密钥
     * @param requestAuthenticator 请求验证
     */
    public void checkResponseAuthenticator(String sharedSecret, byte[] requestAuthenticator) throws PortalException {
        if (requestAuthenticator == null)
            return;
        byte[] authenticator = createResponseAuthenticator(sharedSecret, requestAuthenticator);
        byte[] receivedAuth = getAuthenticator();
        for (int i = 0; i < 16; i++)
            if (authenticator[i] != receivedAuth[i])
                throw new PortalException("response authenticator invalid");
    }
}
