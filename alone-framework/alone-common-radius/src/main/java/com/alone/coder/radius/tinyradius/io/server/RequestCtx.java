package com.alone.coder.radius.tinyradius.io.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.alone.coder.radius.tinyradius.core.packet.request.RadiusRequest;
import com.alone.coder.radius.tinyradius.core.packet.response.RadiusResponse;
import com.alone.coder.radius.tinyradius.io.RadiusEndpoint;

@Getter
@RequiredArgsConstructor
public class RequestCtx {

    private final RadiusRequest request;
    private final RadiusEndpoint endpoint;

    public ResponseCtx withResponse(RadiusResponse response) {
        return new ResponseCtx(request, endpoint, response);
    }

    public String toString() {
        return "RequestCtx{" +
                "packet=" + getRequest() +
                ", endpoint=" + getEndpoint() +
                "}";
    }
}
