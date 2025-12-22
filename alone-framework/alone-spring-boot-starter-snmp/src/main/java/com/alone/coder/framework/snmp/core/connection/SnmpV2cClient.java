package com.alone.coder.framework.snmp.core.connection;

import lombok.extern.slf4j.Slf4j;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SnmpV2cClient implements SnmpClient {

    private Snmp snmp;
    protected CommunityTarget<UdpAddress> target;

    private final UdpAddress address;
    private final String community;


    public SnmpV2cClient(UdpAddress address, String community) throws IOException {
        this.address = address;
        this.community = community;
        snmp = new Snmp(new DefaultUdpTransportMapping());
        snmp.listen();

        target = new CommunityTarget<>();
        target.setCommunity(new OctetString(community));
        target.setAddress(address);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);
    }

    @Override
    public VariableBinding get(OID oid) throws IOException {
        try {
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(oid));
            pdu.setType(PDU.GET);

            ResponseEvent<UdpAddress> event = snmp.send(pdu, target);
            if (event != null && event.getResponse() != null) {
                return event.getResponse().get(0);
            }
            throw new IOException("Timeout or null response for GET");
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("通过节点[%s]未找到下级节点数据".formatted(oid.toString()));
        }
    }

    @Override
    public List<VariableBinding> get(List<OID> oids) throws IOException {
        List<VariableBinding> result = new ArrayList<>();
        PDU pdu = new PDU();
        pdu.setType(PDU.GET);
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
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
                PDU pdu = new PDU();
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
        PDU pdu = new PDU();
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
        return SnmpConstants.version2c;
    }

    @Override
    public void refresh() throws IOException {
        try {
            snmp.close();
        } catch (Exception e) {
            log.error("close snmp error", e);
        }
        snmp = new Snmp(new DefaultUdpTransportMapping());
        snmp.listen();
        target = new CommunityTarget<>();
        target.setCommunity(new OctetString(community));
        target.setAddress(address);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);
    }
}
