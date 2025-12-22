package com.alone.coder.radius.tinyradius.io.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.alone.coder.radius.tinyradius.core.packet.request.RadiusRequest;
import com.alone.coder.radius.tinyradius.core.packet.response.RadiusResponse;
import com.alone.coder.radius.tinyradius.io.RadiusEndpoint;
import com.alone.coder.radius.tinyradius.io.client.RadiusClient;
import com.alone.coder.radius.tinyradius.io.client.handler.PromiseAdapter;
import com.alone.coder.radius.tinyradius.io.server.RequestCtx;

import java.util.Optional;


/**
 * RadiusServer handler that proxies packets to destination.
 * <p>
 * RadiusClient port should be set to proxy port, which will be used to communicate
 * with upstream servers. RadiusClient should also use a variant of {@link PromiseAdapter}
 * which matches requests/responses by adding a custom Proxy-State attribute.
 */
@Log4j2
@RequiredArgsConstructor
public abstract class ProxyHandler extends SimpleChannelInboundHandler<RequestCtx> {

    private final RadiusClient radiusClient;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestCtx msg) {
        final RadiusRequest request = msg.getRequest();

        RadiusEndpoint clientEndpoint = msg.getEndpoint();
        Optional<RadiusEndpoint> serverEndpoint = getOriginServer(request, clientEndpoint);

        if (serverEndpoint.isEmpty()) {
            log.info("Server not found for client proxy request, ignoring");
            return;
        }

        log.debug("Proxying packet to {}", serverEndpoint.get().address());

        radiusClient.communicate(request, serverEndpoint.get()).addListener(f -> {
            final RadiusResponse packet = (RadiusResponse) f.getNow();
            if (f.isSuccess() && packet != null) {
                final RadiusResponse response = RadiusResponse.create(
                        request.getDictionary(), packet.getType(), packet.getId(), packet.getAuthenticator(), packet.getAttributes());
                ctx.writeAndFlush(msg.withResponse(response));
            }
        });
    }

    /**
     * @param request        the request in question
     * @param clientEndpoint the client endpoint the request originated from
     *                       (containing the address, port number and shared secret)
     * @return RadiusEndpoint to proxy request to
     */
    protected abstract Optional<RadiusEndpoint> getOriginServer(RadiusRequest request, RadiusEndpoint clientEndpoint);
}
