package com.alone.coder.framework.snmp.core.trap;

import cn.hutool.core.collection.CollUtil;
import com.alone.coder.framework.snmp.core.service.SnmpSettingApi;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.PDU;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
public class TrapHandler implements CommandResponder {

    private final ExecutorService executor;
    private final SnmpSettingApi snmpSettingApi;

    public TrapHandler(ExecutorService executor, SnmpSettingApi snmpSettingApi) {
        this.executor = executor;
        this.snmpSettingApi = snmpSettingApi;
    }

    @Override
    public <A extends Address> void processPdu(CommandResponderEvent<A> event) {
        executor.submit(() -> {
            try {
                PDU pdu = event.getPDU();
                if (pdu == null) {
                    return;
                }
                if (CollUtil.isEmpty(pdu.getVariableBindings())) {
                    log.info("Trap/Inform is empty");
                    return;
                }
                Address address = event.getPeerAddress();
                List<VariableBinding> variableBindings = pdu.getAll();
                if (address instanceof UdpAddress udpAddress) {
                    snmpSettingApi.sendTrap(udpAddress.getInetAddress().getHostAddress(), variableBindings);
                }
                log.info("Received Trap from {} | secName={} | ver={}",
                        event.getPeerAddress(), event.getSecurityName(), event.getMessageProcessingModel());
                if (pdu.getType() == PDU.INFORM) {
                    PDU responsePDU = (PDU) pdu.clone();
                    responsePDU.setType(PDU.RESPONSE);
                    responsePDU.setRequestID(pdu.getRequestID());

                    event.getMessageDispatcher().returnResponsePdu(
                            event.getMessageProcessingModel(),
                            event.getSecurityModel(),
                            event.getSecurityName(),
                            event.getSecurityLevel(),
                            responsePDU,
                            event.getMaxSizeResponsePDU(),
                            event.getStateReference(),
                            new StatusInformation()
                    );
                    log.info("Sent INFORM response to {}", event.getPeerAddress());
                }
            } catch (Exception e) {
                log.error("Error handling trap/inform", e);
            }
        });
    }
}
