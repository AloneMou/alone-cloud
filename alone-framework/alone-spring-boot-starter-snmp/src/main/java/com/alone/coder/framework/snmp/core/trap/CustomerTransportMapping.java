package com.alone.coder.framework.snmp.core.trap;

import lombok.extern.slf4j.Slf4j;
import org.snmp4j.TransportStateReference;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.AbstractTransportMapping;

import java.io.IOException;

@Slf4j
public class CustomerTransportMapping extends AbstractTransportMapping<UdpAddress>{
    @Override
    public Class<? extends Address> getSupportedAddressClass() {
        return UdpAddress.class;
    }

    @Override
    public void sendMessage(UdpAddress address, byte[] message, TransportStateReference tmStateReference, long timeoutMillis, int maxRetries) throws IOException {
        log.info("CustomerTransportMapping.sendMessage");
    }

    @Override
    public int getMaxOutboundMessageSize() {
        return 65535;
    }

    @Override
    public void close() throws IOException {
        log.info("CustomerTransportMapping.close");
    }

    @Override
    public void listen() throws IOException {
        log.info("CustomerTransportMapping.listen");
    }

    @Override
    public UdpAddress getListenAddress() {
        return new UdpAddress("0.0.0.0/162");
    }
}
