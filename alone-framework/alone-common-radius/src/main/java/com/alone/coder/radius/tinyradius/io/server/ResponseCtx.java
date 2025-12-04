package com.alone.coder.radius.tinyradius.io.server;


import lombok.Getter;
import com.alone.coder.radius.tinyradius.core.packet.request.RadiusRequest;
import com.alone.coder.radius.tinyradius.core.packet.response.RadiusResponse;
import com.alone.coder.radius.tinyradius.io.RadiusEndpoint;

@Getter
public class ResponseCtx extends RequestCtx {

    private final RadiusResponse response;

    public ResponseCtx(RadiusRequest packet, RadiusEndpoint endpoint, RadiusResponse response) {
        super(packet, endpoint);
        this.response = response;
    }
}
