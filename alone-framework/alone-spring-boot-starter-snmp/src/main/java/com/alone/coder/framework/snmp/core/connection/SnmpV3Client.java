package com.alone.coder.framework.snmp.core.connection;

import lombok.extern.slf4j.Slf4j;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SnmpV3Client implements SnmpClient {

    private Snmp snmp;
    private UserTarget<UdpAddress> target;

    private final UdpAddress address;
    private final String username;
    private final OID authProtocol;
    private final OctetString authPassphrase;
    private final OID privProtocol;
    private final OctetString privPassphrase;

    public SnmpV3Client(UdpAddress address,
                        String username,
                        OID authProtocol, OctetString authPassphrase,
                        OID privProtocol, OctetString privPassphrase) throws IOException {
        this.address = address;
        this.username = username;
        this.authProtocol = authProtocol;
        this.authPassphrase = authPassphrase;
        this.privProtocol = privProtocol;
        this.privPassphrase = privPassphrase;
        this.init();
    }

    private void init() throws IOException {
        this.snmp = new Snmp(new DefaultUdpTransportMapping());
        this.snmp.listen();

        // USM初始化
        USM usm = new USM(SecurityProtocols.getInstance(),
                new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);

        // 添加用户
        UsmUser user = new UsmUser(
                new OctetString(this.username),
                this.authProtocol, this.authPassphrase,
                this.privProtocol, this.privPassphrase);
        usm.addUser(user);

        // 目标设置
        this.target = new UserTarget<UdpAddress>();
        this.target.setAddress(address);
        this.target.setRetries(2);
        this.target.setTimeout(1500);
        this.target.setVersion(SnmpConstants.version3);
        this.target.setSecurityLevel(determineSecurityLevel(authProtocol, privProtocol));
        this.target.setSecurityName(new OctetString(username));
    }

    private int determineSecurityLevel(OID authProtocol, OID privProtocol) {
        if (authProtocol == null && privProtocol == null) {
            return SecurityLevel.NOAUTH_NOPRIV;
        } else if (authProtocol != null && privProtocol == null) {
            return SecurityLevel.AUTH_NOPRIV;
        } else if (authProtocol != null && privProtocol != null) {
            return SecurityLevel.AUTH_PRIV;
        } else {
            return SecurityLevel.NOAUTH_NOPRIV;
        }
    }

    @Override
    public VariableBinding get(OID oid) throws IOException {
        try {
            ScopedPDU pdu = new ScopedPDU();
            pdu.add(new VariableBinding(oid));
            pdu.setType(PDU.GET);

            ResponseEvent<UdpAddress> event = snmp.send(pdu, target);
            if (event != null && event.getResponse() != null) {
                return event.getResponse().get(0);
            }
            throw new IOException("Timeout or null response for GET");
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("通过节点[%s]未找到节点数据".formatted(oid.toString()));
        }
    }

    @Override
    public List<VariableBinding> get(List<OID> oids) throws IOException {
        List<VariableBinding> result = new ArrayList<>();
        ScopedPDU pdu = new ScopedPDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);

        ResponseEvent<UdpAddress> event = snmp.send(pdu, target);
        if (event == null) {
            throw new IOException("Timeout or null response for GET");
        }
        PDU response = event.getResponse();
        if (response == null) {
            throw new IOException("Timeout or null response for GET");
        } else {
            log.info("response pdu size is {}", response.size());
            for (int i = 0; i < response.size(); i++) {
                VariableBinding vb = response.get(i);
                result.add(vb);
            }
            return result;
        }

    }

    @Override
    public List<VariableBinding> getStr(List<String> oids) throws IOException {
        List<OID> oidLs = new ArrayList<>();
        for (String oid : oids) {
            oidLs.add(new OID(oid));
        }
        return get(oidLs);
    }

    @Override
    public List<VariableBinding> walk(OID rootOid) throws IOException {
        try {
            List<VariableBinding> result = new ArrayList<>();
            OID currentOid = rootOid;

            while (true) {
                ScopedPDU pdu = new ScopedPDU();
                pdu.add(new VariableBinding(currentOid));
                pdu.setType(PDU.GETNEXT);

                ResponseEvent<UdpAddress> event = snmp.send(pdu, target);
                if (event == null || event.getResponse() == null) {
                    throw new IOException("Timeout or null response for WALK");
                }
                VariableBinding vb = event.getResponse().get(0);

                if (vb == null || !vb.getOid().startsWith(rootOid) || vb.getOid().compareTo(currentOid) <= 0) {
                    break;  // Walk结束
                }
                result.add(vb);
                currentOid = vb.getOid();
            }
            return result;
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("通过根节点[%s]未找到下级节点数据".formatted(rootOid.toString()));
        }

    }

    @Override
    public void set(VariableBinding varBind) throws IOException {
        ScopedPDU pdu = new ScopedPDU();
        pdu.add(varBind);
        pdu.setType(PDU.SET);

        ResponseEvent<UdpAddress> event = snmp.send(pdu, target);
        if (event == null || event.getResponse() == null) {
            throw new IOException("Timeout or null response for SET");
        }
        if (event.getResponse().getErrorStatus() != PDU.noError) {
            throw new IOException("Error in SET response: " + event.getResponse().getErrorStatusText());
        }
    }

    @Override
    public void close() throws IOException {
        snmp.close();
    }

    @Override
    public Snmp getSnmp() {
        return snmp;
    }

    @Override
    public String getTargetAddress() {
        return target.getAddress().toString();
    }

    @Override
    public int getVersion() {
        return SnmpConstants.version3;
    }

    @Override
    public void refresh() throws IOException {
        try {
            snmp.close();
        } catch (Exception e) {
            log.error("close snmp error", e);
        }
        this.init();
    }
}
