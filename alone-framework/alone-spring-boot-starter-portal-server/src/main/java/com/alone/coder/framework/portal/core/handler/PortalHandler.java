package com.alone.coder.framework.portal.core.handler;

import com.alone.coder.framework.portal.config.PortalConfig;
import com.alone.coder.framework.portal.core.packet.PortalPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这个类主要处理与接入设备的非响应报文
 * 比如：同步用户报文 强制用户下线等
 */
@Component
public class PortalHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private final String PORTAL_SESSION_CLIENT_IP_KEY = "PORTAL_SESSION_CLIENT_IP_KEY";

    @Autowired
    protected PortalConfig portalConfig;

    private Map<Short,byte[]> challengeMap = new ConcurrentHashMap<Short,byte[]>();

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        InetSocketAddress remoteAddress = (InetSocketAddress) session.getRemoteAddress();
        session.setAttribute(PORTAL_SESSION_CLIENT_IP_KEY, remoteAddress);
        logger.info("有客户端接入portal服务器，IP地址是：" + remoteAddress,Memarylogger.PORTAL);
    }

    /**
     * 异常处理
     * @param session
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(IoSession session, Throwable cause)throws Exception {
        cause.printStackTrace();
        session.closeNow();
    }

    @Override
    public void messageReceived(IoSession session, Object message)throws Exception {

        if (!(message instanceof IoBuffer)) {
            return;
        }
        IoBuffer buffer = (IoBuffer) message;
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);
        PortalPacket request = null;

        final InetSocketAddress remoteAddress = (InetSocketAddress) session.getAttribute(PORTAL_SESSION_CLIENT_IP_KEY);

        logger.info("有客户端发送portal报文过来了，IP地址是：" + remoteAddress,Memarylogger.PORTAL);

        try{
            request = new PortalPacket(data);
        } catch(Exception e){
            return;
        }

        PortalPacket resp = null;

        // TODO 这里处理报文并响应

        if(resp!=null){
            logger.info(String.format("发送 Portal 响应 %s", resp.toString()),Memarylogger.PORTAL);
            IoBuffer outbuff = resp.encodePacket();
            session.write(outbuff,remoteAddress);
            session.closeOnFlush();
        }

    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {

    }
}
