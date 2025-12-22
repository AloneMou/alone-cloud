package com.alone.coder.framework.portal.core;

import com.alone.coder.framework.portal.core.packet.PortalPacket;
import io.netty.channel.ChannelFuture;
import org.apache.mina.core.buffer.IoBuffer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * 接入设备提供 UDP 2000端口供portal服务器进行响应报文收发
 */
@Component
public class PortalClient {

    private int timeout = 30000;


    /**
     * 推送报文：监听返回值
     *
     * @param request 报文
     * @param ipaddr  接入设备IP地址
     * @param port    接入设备开放的端口号：默认2000
     * @return 接入设备返回的报文
     * @throws PortalException 异常
     */
    public PortalPacket sendToAc(PortalPacket request, String ipaddr, int port) throws PortalException {
        DatagramSocket sock = null;
        try {
            sock = new DatagramSocket();
            IoBuffer buff = request.encodePacket();
            byte[] data = new byte[buff.remaining()];
            buff.get(data);
            DatagramPacket packetOut = new DatagramPacket(data, data.length, new InetSocketAddress(ipaddr, port));
            DatagramPacket packetIn = new DatagramPacket(new byte[PortalPacket.MAX_PACKET_LENGTH], PortalPacket.MAX_PACKET_LENGTH);
            sock.setSoTimeout(timeout);
            sock.send(packetOut);
            sock.receive(packetIn);
            PortalPacket resp = new PortalPacket(packetIn.getData());
            resp.checkResponseAuthenticator(request.getSecret(), request.getAuthenticator());
            sock.close();
            return resp;
        } catch (SocketException e) {
            throw new PortalException("网络异常", e);
        } catch (IOException e) {
            throw new PortalException("IO异常", e);
        }
    }

    /**
     * 推送报文：无返回值 不等待
     *
     * @param request 报文
     * @param ipaddr  接入设备IP地址
     * @param port    接入设备开放的端口号：默认2000
     * @throws PortalException 异常
     */
    public void sendToAcNoReply(PortalPacket request, String ipaddr, int port) throws PortalException {
        try (DatagramSocket sock = new DatagramSocket()) {
            IoBuffer buff = request.encodePacket();
            byte[] data = new byte[buff.remaining()];
            buff.get(data);
            DatagramPacket packetOut = new DatagramPacket(data, data.length, new InetSocketAddress(ipaddr, port));
            sock.setSoTimeout(timeout);
            sock.send(packetOut);
        } catch (SocketException e) {
            throw new PortalException("网络异常", e);
        } catch (IOException e) {
            throw new PortalException("IO异常", e);
        }
    }
}
