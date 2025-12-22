package com.alone.coder.framework.portal.core.netty;

import com.alone.coder.framework.portal.core.PortalException;
import com.alone.coder.framework.portal.core.packet.PortalPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PortalPacketDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        try {
            ByteBuf content = msg.content();
            if (content.readableBytes() > 0) {
                byte[] data = new byte[content.readableBytes()];
                content.readBytes(data);
                PortalPacket packet = new PortalPacket(data);
                out.add(packet);
            }
        } catch (PortalException e) {
            log.error("解码Portal报文失败", e);
        }
    }
}