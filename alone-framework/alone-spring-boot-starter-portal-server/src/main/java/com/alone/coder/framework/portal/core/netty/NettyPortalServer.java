package com.alone.coder.framework.portal.core.netty;

import com.alone.coder.framework.portal.config.PortalProperties;
import com.alone.coder.framework.portal.core.PortalException;
import com.alone.coder.framework.portal.core.packet.PortalPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyPortalServer {

    private final PortalProperties portalProperties;

    public NettyPortalServer(PortalProperties portalProperties) {
        this.portalProperties = portalProperties;
    }

    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private Channel channel;
    private ConcurrentHashMap<String, CompletableFuture<PortalPacket>> pendingRequests;

    public void start() throws Exception {
        this.group = new NioEventLoopGroup();
        this.pendingRequests = new ConcurrentHashMap<>();
        this.bootstrap = new Bootstrap()
                .group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) {
                        ch.pipeline().addLast(new PortalPacketDecoder());
                        ch.pipeline().addLast(new PortalResponseHandler(pendingRequests));
                    }
                });
        // 绑定到指定端口
        this.channel = this.bootstrap.bind(portalProperties.getPort()).sync().channel();
    }

    public void stop() throws Exception {
        log.info("正在停止Portal服务...");
        if (channel != null) {
            channel.close();
        }
        if (group != null) {
            group.shutdownGracefully();
        }
        if (pendingRequests != null) {
            pendingRequests.clear();
        }
    }

    /**
     * 异步推送报文：监听返回值
     *
     * @param request 报文
     * @param ipaddr  接入设备IP地址
     * @param port    接入设备开放的端口号：默认2000
     * @return 接入设备返回的报文
     */
    public CompletableFuture<PortalPacket> sendToAcAsync(PortalPacket request, String ipaddr, int port) {
        if (channel == null) {
            CompletableFuture<PortalPacket> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(new PortalException("Netty客户端未初始化"));
            return failedFuture;
        }

        try {
            request.setSecret(portalProperties.getSecret());
            CompletableFuture<PortalPacket> future = new CompletableFuture<>();
            String requestId = ipaddr + ":" + port + ":" + request.getSerialNo();
            pendingRequests.put(requestId, future);
            // 编码请求报文
            ByteBuf buff = Unpooled.wrappedBuffer(request.encodePacket().array());
            InetSocketAddress recipient = new InetSocketAddress(ipaddr, port);
            // 发送报文
            channel.writeAndFlush(new DatagramPacket(buff, recipient));
            // 设置超时
            future.orTimeout(30, TimeUnit.SECONDS)
                    .whenComplete((result, throwable) -> {
                        pendingRequests.remove(requestId);
                        if (throwable != null) {
                            log.error("请求超时或发生错误: {}", throwable.getMessage());
                        }
                    });
            return future;
        } catch (Exception e) {
            log.error("发送报文异常", e);
            CompletableFuture<PortalPacket> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(new PortalException("网络异常", e));
            return failedFuture;
        }
    }

    /**
     * 同步推送报文：监听返回值（阻塞方式，用于兼容现有接口）
     *
     * @param request 报文
     * @param ipaddr  接入设备IP地址
     * @param port    接入设备开放的端口号：默认2000
     * @return 接入设备返回的报文
     * @throws PortalException 异常
     */
    public PortalPacket sendToAc(PortalPacket request, String ipaddr, int port) throws PortalException {
        try {
            return sendToAcAsync(request, ipaddr, port).get(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new PortalException("网络异常", e);
        }
    }

    /**
     * 同步推送报文：监听返回值（阻塞方式，用于兼容现有接口）
     *
     * @param request 报文
     * @param ipaddr  接入设备IP地址
     * @return 接入设备返回的报文
     * @throws PortalException 异常
     */
    public PortalPacket sendToAc(PortalPacket request, String ipaddr) throws PortalException {
        return sendToAc(request, ipaddr, portalProperties.getNasPort());
    }

    /**
     * 异步推送报文：无返回值 不等待
     *
     * @param array  报文
     * @param ipaddr 接入设备IP地址
     * @param port   接入设备开放的端口号：默认2000
     */
    public void sendToAcNoReplyAsync(byte[] array, String ipaddr, int port) {
        if (channel == null) {
            log.warn("Netty客户端未初始化");
            return;
        }
        try {
            // 编码请求报文
            ByteBuf buff = Unpooled.wrappedBuffer(array);
            InetSocketAddress recipient = new InetSocketAddress(ipaddr, port);
            // 发送报文
            channel.writeAndFlush(new DatagramPacket(buff, recipient));
        } catch (Exception e) {
            log.error("发送无回复报文异常", e);
        }
    }


    /**
     * 推送报文：无返回值 不等待（阻塞方式，用于兼容现有接口）
     *
     * @param request 报文
     * @param ipaddr  接入设备IP地址
     * @param port    接入设备开放的端口号：默认2000
     * @throws PortalException 异常
     */
    public void sendToAcNoReply(PortalPacket request, String ipaddr, int port) throws PortalException {
        request.setSecret(portalProperties.getSecret());
        request.setIsChap(portalProperties.getAuthType().getValue());
        sendToAcNoReplyAsync(request.encodePacket().array(), ipaddr, port);
    }

    /**
     * 推送报文：无返回值 不等待（阻塞方式，用于兼容现有接口）
     *
     * @param array  报文
     * @param ipaddr 接入设备IP地址
     * @param port   端口
     */
    public void sendToAcNoReply(byte[] array, String ipaddr, int port) {
        sendToAcNoReplyAsync(array, ipaddr, port);
    }

    /**
     * 推送报文：无返回值 不等待（阻塞方式，用于兼容现有接口）
     *
     * @param array  报文
     * @param ipaddr 接入设备IP地址
     */
    public void sendToAcNoReply(byte[] array, String ipaddr) {
        sendToAcNoReplyAsync(array, ipaddr, portalProperties.getNasPort());
    }

    /**
     * 推送报文：无返回值 不等待（阻塞方式，用于兼容现有接口）
     *
     * @param request 报文
     * @param ipaddr  接入设备IP地址
     * @throws PortalException 异常
     */
    public void sendToAcNoReply(PortalPacket request, String ipaddr) throws PortalException {
        sendToAcNoReply(request, ipaddr, portalProperties.getNasPort());
    }
}