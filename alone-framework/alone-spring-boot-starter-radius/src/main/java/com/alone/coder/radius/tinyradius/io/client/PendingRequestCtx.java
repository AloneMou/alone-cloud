package com.alone.coder.radius.tinyradius.io.client;

import io.netty.util.concurrent.Promise;
import lombok.Getter;
import com.alone.coder.radius.tinyradius.core.packet.request.RadiusRequest;
import com.alone.coder.radius.tinyradius.core.packet.response.RadiusResponse;
import com.alone.coder.radius.tinyradius.io.RadiusEndpoint;
import com.alone.coder.radius.tinyradius.io.server.RequestCtx;

/**
 * Wrapper that holds a promise to be resolved when response is received.
 */
@Getter
public class PendingRequestCtx extends RequestCtx {

    private final Promise<RadiusResponse> response;

    public PendingRequestCtx(RadiusRequest packet, RadiusEndpoint endpoint, Promise<RadiusResponse> response) {
        super(packet, endpoint);
        this.response = response;
    }

    @Override
    public String toString() {
        return "PendingRequestCtx{" +
                "packet=" + getRequest() +
                ", endpoint=" + getEndpoint() +
                "}";
    }
}
