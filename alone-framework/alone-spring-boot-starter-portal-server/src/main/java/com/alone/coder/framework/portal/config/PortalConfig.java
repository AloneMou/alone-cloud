package com.alone.coder.framework.portal.config;


import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties(prefix = "aaa.portal")
public class PortalConfig {

    private Log logger = LogFactory.getLog(PortalConfig.class);

    private int simPort;
    private int listenPort;
    private boolean portalEnabled;
    private boolean trace;
    private int papchap;
    private int timeout;
    private int pool;
    private String templateDir;

//    /**
//     * 这是本地模拟AC设备用的
//     * @param acSimHandler
//     * @return
//     * @throws IOException
//     */
//    @Bean(destroyMethod = "unbind")
//    public NioDatagramAcceptor nioAcSimAcceptor(AcSimHandler acSimHandler) throws IOException {
//        if(isPortalEnabled()){
//            logger.info("====== AcSimServer not running =======");
//            return null;
//        }
//        NioDatagramAcceptor nioAcSimAcceptor = new NioDatagramAcceptor();
//        nioAcSimAcceptor.setDefaultLocalAddress(new InetSocketAddress(simPort));
//        DatagramSessionConfig dcfg = nioAcSimAcceptor.getSessionConfig();
//        dcfg.setReceiveBufferSize(33554432);
//        dcfg.setReadBufferSize(8192);
//        dcfg.setSendBufferSize(8192);
//        dcfg.setBothIdleTime(0);
//        dcfg.setReuseAddress(true);
//
//        DefaultIoFilterChainBuilder acsimIoFilterChainBuilder = new DefaultIoFilterChainBuilder();
//        Map<String, IoFilter> filters = new LinkedHashMap<>();
//        ExecutorFilter authExecutorFilter = new ExecutorFilter(3, 8, 60, TimeUnit.SECONDS);
//        filters.put("executor", authExecutorFilter);
//        acsimIoFilterChainBuilder.setFilters(filters);
//        nioAcSimAcceptor.setFilterChainBuilder(acsimIoFilterChainBuilder);
//        nioAcSimAcceptor.setHandler(acSimHandler);
//        nioAcSimAcceptor.bind();
//        logger.info(String.format("====== AcSimServer listen %s ======", simPort));
//        return nioAcSimAcceptor;
//    }

    /**
     * 本方法提供 UDP 50100端口供接入设备发送及接收非响应报文
     *
     * @param portalHandler
     * @param portalIoFilterChainBuilder
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "unbind")
    public NioDatagramAcceptor nioPortalAcceptor(PortalHandler portalHandler, DefaultIoFilterChainBuilder portalIoFilterChainBuilder) throws IOException {
        if(isPortalEnabled()){
            logger.info("====== PortalServer not running =======");
            return null;
        }
        NioDatagramAcceptor nioPortalAcceptor = new NioDatagramAcceptor();
        nioPortalAcceptor.setDefaultLocalAddress(new InetSocketAddress(listenPort));
        DatagramSessionConfig dcfg = nioPortalAcceptor.getSessionConfig();
        dcfg.setReceiveBufferSize(33554432);
        dcfg.setReadBufferSize(8192);
        dcfg.setSendBufferSize(8192);
        dcfg.setBothIdleTime(0);
        nioPortalAcceptor.setFilterChainBuilder(portalIoFilterChainBuilder);
        nioPortalAcceptor.setHandler(portalHandler);
        nioPortalAcceptor.bind();
        logger.info(String.format("====== PortalServer listen %s ======", listenPort));
        return nioPortalAcceptor;
    }

    @Bean
    public DefaultIoFilterChainBuilder portalIoFilterChainBuilder() {
        DefaultIoFilterChainBuilder portalIoFilterChainBuilder = new DefaultIoFilterChainBuilder();
        Map<String, IoFilter> filters = new LinkedHashMap<>();
        ExecutorFilter portalExecutorFilter = new ExecutorFilter(8, getPool(), 60, TimeUnit.SECONDS,new DefaultThreadFactory("portalExecutorFilter",Thread.MAX_PRIORITY));
        filters.put("executor", portalExecutorFilter);
        portalIoFilterChainBuilder.setFilters(filters);
        return portalIoFilterChainBuilder;
    }

    public int getSimPort() {
        return simPort;
    }

    public void setSimPort(int simPort) {
        this.simPort = simPort;
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }


    public int getPapchap() {
        return papchap;
    }

    public void setPapchap(int papchap) {
        this.papchap = papchap;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isTraceEnabled(){
        return trace;
    }

    public boolean isPortalEnabled() {
        return !portalEnabled;
    }

    public void setPortalEnabled(boolean portalEnabled) {
        this.portalEnabled = portalEnabled;
    }

    public int getPool() {
        return pool;
    }

    public void setPool(int pool) {
        this.pool = pool;
    }

    public String getTemplateDir() {
        return templateDir;
    }

    public void setTemplateDir(String templateDir) {
        this.templateDir = templateDir;
    }

    public boolean isTrace() {
        return trace;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }
}
