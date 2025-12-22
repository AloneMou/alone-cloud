package com.alone.coder.framework.snmp.core.trap;

import com.alone.coder.framework.snmp.config.SnmpTrapProperties;
import com.alone.coder.framework.snmp.core.pojo.SnmpSetting;
import com.alone.coder.framework.snmp.core.enums.AuthProtocolEnum;
import com.alone.coder.framework.snmp.core.enums.PrivProtocolEnum;
import com.alone.coder.framework.snmp.core.enums.SnmpVersionEnum;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.*;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

@Slf4j
public class SnmpTrapReceiver {

    private final SnmpTrapProperties properties;
    private final TrapHandler trapHandler;
    private MessageDispatcher dispatcher;
    private EventLoopGroup group;
    public TransportMapping<UdpAddress> transport;
    private USM usm;


    public SnmpTrapReceiver(SnmpTrapProperties properties, TrapHandler trapHandler) {
        this.properties = properties;
        this.trapHandler = trapHandler;
    }

    public void start() throws Exception {
        initSnmpDispatcher();
        if (properties.getMode() == SnmpTrapProperties.TrapMode.NETTY) {
            startNettyMode();
        } else {
            startSnmp4jMode();
        }
    }

    /**
     * Netty模式，外部收包 → 手动喂 dispatcher
     */
    private void startNettyMode() throws InterruptedException {
        log.info("Starting SNMP Trap listener in NETTY mode...");
        transport = new CustomerTransportMapping(); // dummy transport
        group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
                        byte[] data = new byte[packet.content().readableBytes()];
                        packet.content().readBytes(data);
                        try {
                            UdpAddress sender = new UdpAddress(packet.sender().getHostString() + "/" + packet.sender().getPort());
                            dispatcher.processMessage(transport, sender, ByteBuffer.wrap(data), null);
                        } catch (Exception e) {
                            log.error("Trap message processing error", e);
                        }
                    }
                });
        ChannelFuture channelFuture = bootstrap.bind(properties.getPort()).sync();
        log.info("Netty mode SNMP Trap listener started on port {}", properties.getPort());
    }

    private void startSnmp4jMode() throws IOException {
        log.info("Starting SNMP Trap listener in SNMP4J mode...");
        transport = new DefaultUdpTransportMapping(new UdpAddress("0.0.0.0/" + properties.getPort()));
        Snmp snmp = new Snmp(dispatcher, transport);
        snmp.listen();
        log.info("SNMP4J mode Trap listener started on port {}", properties.getPort());
    }

    public void stop() throws IOException {
        if (properties.getMode() == SnmpTrapProperties.TrapMode.NETTY && group != null) {
            group.shutdownGracefully();
        }
        if (transport != null) {
            transport.close();
        }
        log.info("SNMP Trap listener stopped");
    }

    private void initSnmpDispatcher() {
        dispatcher = new MessageDispatcherImpl();

        // 注册支持的协议版本
        dispatcher.addMessageProcessingModel(new MPv1());
        dispatcher.addMessageProcessingModel(new MPv2c());
        dispatcher.addMessageProcessingModel(new MPv3());

        // 初始化USM和添加多用户
        this.usm = new USM(SecurityProtocols.getInstance(),
                new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);

        // 注册支持的加密与认证算法
        SecurityProtocols securityProtocols = SecurityProtocols.getInstance();
        // 注册 SHA-1
        securityProtocols.addAuthenticationProtocol(new AuthMD5());
        securityProtocols.addAuthenticationProtocol(new AuthSHA());
        // 注册 SHA-2 系列
        securityProtocols.addAuthenticationProtocol(new AuthHMAC128SHA224());
        securityProtocols.addAuthenticationProtocol(new AuthHMAC192SHA256());
        securityProtocols.addAuthenticationProtocol(new AuthHMAC256SHA384());
        securityProtocols.addAuthenticationProtocol(new AuthHMAC384SHA512());
        // 注册 DES
        securityProtocols.addPrivacyProtocol(new PrivAES128());
        securityProtocols.addPrivacyProtocol(new PrivAES192());
        securityProtocols.addPrivacyProtocol(new PrivAES256());
        securityProtocols.addPrivacyProtocol(new Priv3DES());
        securityProtocols.addPrivacyProtocol(new PrivDES());
        // 添加异步Trap事件处理器
        dispatcher.addCommandResponder(trapHandler);
    }

    public void updateUsers(List<SnmpSetting> devices) {
        if (usm == null) {
            log.warn("USM is not initialized yet");
            return;
        }
        // 清空原有用户
        usm.removeAllUsers();
        devices.stream()
                .filter(d -> SnmpVersionEnum.SNMP_V3.getValue().equals(d.getVersion()))
                .forEach(d -> usm.addUser(new UsmUser(
                        new OctetString(d.getUsername()),
                        AuthProtocolEnum.fromName(d.getAuthProtocol()).getOid(),
                        new OctetString(d.getAuthPassphrase()),
                        PrivProtocolEnum.fromName(d.getPrivProtocol()).getOid(),
                        new OctetString(d.getPrivPassphrase())
                )));
    }
}
