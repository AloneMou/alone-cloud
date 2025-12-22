package com.alone.coder.framework.snmp.core.connection;

import com.alone.coder.framework.snmp.core.enums.SnmpVersionEnum;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class SnmpClientFactoryImpl implements SnmpClientFactory {

    private final ConcurrentMap<Long, SnmpClient> clients = new ConcurrentHashMap<>();

    @Override
    public SnmpClient getClient(Long id) {
        return clients.get(id);
    }

    @Override
    public void removeClient(Long id) {
        if (clients.containsKey(id)) {
            SnmpClient client = clients.get(id);
            try {
                client.close();
            } catch (Exception e) {
                log.error("close client error", e);
            } finally {
                clients.remove(id);
            }
        }
    }

    @Override
    public SnmpClient createClient(Long id, SnmpVersionEnum version, String address, String community) throws IOException {
        UdpAddress udpAddress = new UdpAddress(address);
        SnmpClient client = switch (version) {
            case SNMP_V1 -> new SnmpV1Client(udpAddress, community);
            case SNMP_V2C -> new SnmpV2cClient(udpAddress, community);
            default -> null;
        };
        if (client == null) {
            log.error("Version must be V1 or V2C for this method");
        } else {
            if (clients.containsKey(id)) {
                try {
                    SnmpClient oldClient = clients.get(id);
                    oldClient.close();
                } catch (Exception e) {
                    log.error("close old client error", e);
                }
            }
            clients.put(id, client);
        }
        return client;
    }

    @Override
    public SnmpClient createV3Client(Long id, String address, String username, OID authProtocol, String authPassphrase, OID privProtocol, String privPassphrase) throws IOException {
        try {
            UdpAddress udpAddress = new UdpAddress(address);
            SnmpClient client = new SnmpV3Client(udpAddress, username, authProtocol, new OctetString(authPassphrase), privProtocol, new OctetString(privPassphrase));
            if (clients.containsKey(id)) {
                try {
                    SnmpClient oldClient = clients.get(id);
                    oldClient.close();
                } catch (Exception e) {
                    log.error("close old client error", e);
                }
            }
            clients.put(id, client);
            return client;
        } catch (Exception e) {
            log.error("createV3Client error", e);
        }
        return null;
    }
}
