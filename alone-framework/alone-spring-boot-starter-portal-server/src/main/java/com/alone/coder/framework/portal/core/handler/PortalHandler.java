package com.alone.coder.framework.portal.core.handler;

import com.alone.coder.framework.portal.core.packet.PortalPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.buffer.IoBuffer;

import java.net.InetSocketAddress;

/**
 * 这个类主要处理与接入设备的非响应报文
 * 比如：同步用户报文 强制用户下线等
 */
@Slf4j
public class PortalHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private final String PORTAL_SESSION_CLIENT_IP_KEY = "PORTAL_SESSION_CLIENT_IP_KEY";

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        IoBuffer buffer = IoBuffer.wrap(datagramPacket.content().array());
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);
        PortalPacket request = null;
        final InetSocketAddress remoteAddress = datagramPacket.sender();
        log.info("有客户端发送portal报文过来了，IP地址是：{}", remoteAddress.toString());
        try {
            request = new PortalPacket(data);
        } catch (Exception e) {
            log.error("解析报文错误", e);
        }
//        PortalPacket resp = null;
//        // TODO 这里处理报文并响应
//        if (resp != null) {
//            log.info(String.format("发送 Portal 响应 %s", resp.toString()), Memarylogger.PORTAL);
//            IoBuffer outbuff = resp.encodePacket();
//            session.write(outbuff, remoteAddress);
//            session.closeOnFlush();
//        }

    }
}
