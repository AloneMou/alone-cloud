package com.alone.coder.framework.portal.core.netty;

import com.alone.coder.framework.portal.core.packet.PortalPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PortalResponseHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private final ConcurrentHashMap<String, CompletableFuture<PortalPacket>> pendingRequests;

    public PortalResponseHandler(ConcurrentHashMap<String, CompletableFuture<PortalPacket>> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        try {
            // 获取发送方地址
            InetSocketAddress sender = msg.sender();
            if (sender == null) {
                log.warn("无法获取发送方地址");
                return;
            }

            String ip = sender.getAddress().getHostAddress();
            int port = sender.getPort();
            
            // 解析报文内容
            byte[] data = new byte[msg.content().readableBytes()];
            msg.content().readBytes(data);
            
            PortalPacket response = new PortalPacket(data);
            
            // 构造请求ID并查找对应的CompletableFuture
            String requestId = ip + ":" + port + ":" + response.getSerialNo();
            CompletableFuture<PortalPacket> future = pendingRequests.get(requestId);

            if (future != null) {
                future.complete(response);
                pendingRequests.remove(requestId); // 处理完后从映射中移除
            } else {
                log.debug("未找到匹配的待处理请求: {}", requestId);
            }
        } catch (Exception e) {
            log.error("处理Portal响应时发生异常", e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("处理Portal响应时发生异常", cause);
        ctx.close();
    }
}