package com.alone.coder.radius.tinyradius.io.client.handler;

import java.net.SocketAddress;

public interface BlacklistManager {

    boolean isBlacklisted(SocketAddress address);

    void logFailure(SocketAddress address, Throwable cause);

    void reset(SocketAddress address);
}
