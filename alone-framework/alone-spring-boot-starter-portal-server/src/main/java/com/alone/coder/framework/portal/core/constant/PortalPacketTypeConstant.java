package com.alone.coder.framework.portal.core.constant;

/**
 * 消息类型定义
 */
public interface PortalPacketTypeConstant {

    //========================[Portal Server--> BAS]========================
    /**
     * Portal Server--> BAS
     * <p>表示此报文是Portal Server向BAS发送的 Challenge请求报文</p>
     * 必须
     */
    int REQ_CHALLENGE = 0x01;
    /**
     * BAS --> Portal server
     * <p>表示此报文是BAS对Portal Server请求Challenge报文的响应报文</p>
     * 必须
     */
    int ACK_CHALLENGE = 0x02;

    /**
     * Portal server --> BAS
     * <p>表示此报文是Portal Server向BAS发送的请求认证报文</p>
     * 必须
     */
    int REQ_AUTH = 0x03;

    /**
     * BAS --> Portal server
     * <p>表示此报文是BAS对Portal Server请求认证报文的响应报文</p>
     * 必须
     */
    int ACK_AUTH = 0x04;

    /**
     * Portal server --> BAS
     * <p>表示此报文是Portal  Server向BAS发送的下线请求报文</p>
     * 必须
     */
    int REQ_LOGOUT = 0x05;
    /**
     * BAS --> Portal server
     * <p>表示此报文是BAS对Portal Server下线请求的响应报文</p>
     * 必须
     */
    int ACK_LOGOUT = 0x06;
    /**
     * Portal server --> BAS
     * <p>表示此报文是Portal Server收到认证成功响应报文后向BAS发送的确认报文</p>
     * 建议
     */
    int AFF_ACK_AUTH = 0x07;

    /**
     * BAS --> Portal server
     * <p>表示此报文是BAS发送给Portal Server，用户被强制下线的通知报文</p>
     * 必须
     */
    int NTF_LOGOUT = 0x08;
    /**
     * Portal server --> BAS
     * <p>信息询问报文</p>
     * 必须
     */
    int REQ_INFO = 0x09;
    /**
     * BAS --> Portal server
     * <p>信息询问的应答报文</p>
     * 必须
     */
    int ACK_INFO = 0x0a;
    /**
     * Portal server --> BAS
     * <p>Portal Server向BAS发送的发现新用户要求上线的通知报文</p>
     * 建议
     */
    int NTF_USERDISCOVER = 0x0b;
    /**
     * BAS --> Portal server
     * <p>BAS向Portal Server发送的通知更改某个用户IP地址的通知报文</p>
     * 必须
     */
    int NTF_USERIPCHANGE = 0x0c;
    /**
     * Portal server --> BAS
     * <p>PortalServer通知BAS对用户表项的IP切换已成功</p>
     * 必须
     */
    int AFF_NTF_USERIPCHAN = 0x0d;
    /**
     * Portal server --> BAS
     * <p>
     * PortalServer通知BAS用户强制下线成功,
     * BAS通过NTF_LOGOUT报文通知Portal Server用户下线后,
     * Portal Server回应BAS设备用户下线完成的回应报文。
     * 如果Portal Server收到了BAS的用户下线请求,
     * 必须回应ACK_NTF_LOGOUT，以通知BAS服务器，无论用户是否在线。
     * 同时，Portal Server必须确保用户下线处理成功。
     * </p>
     * 必须
     */
    int ACK_NTF_LOGOUT = 0x0e;
    /**
     * Portal server --> BAS
     * <p>
     * 逃生心跳报文，PortalServer周期性的向BAS发送该报文，
     * 以表明PortalServer可以正常提供服务。BAS如果连续多次没有接收到该报文，
     * 说明PortalServer已经停止服务，BAS即切换为逃生状态，此时不再强制用户认证，允许用户的报文直接通过。
     * 该报文没有回应报文。
     * </p>
     * 必须
     */
    int NTF_HEARTBEAT = 0x0f;

    /**
     * Portal server --> BAS
     * 用户心跳报文，PortalServer周期性的向BAS发送该报文，
     * 以表明该用户仍然在线，BAS如果连续多次没有接收到含有该用户IP的报文，
     * 说明该用户已经断线，BAS将向RADIUS服务器发送下线报文，将用户下线。
     * 用户心跳报文中包含了多个用户的IP地址。
     * 必须
     */
    int NTF_USER_HEARTBEAT = 0x10;
    /**
     * BAS --> Portal server
     * 用户心跳回应报文，BAS接收到PortalServer的用户心跳报文后，
     * 会遍历这些用户IP地址，并将已经下线的用户IP地址放入回应报文中。
     * PortalServer收到回应报文后，将用户下线。
     * 如果用户心跳报文中的所有用户都在线，则BAS将不发送回应报文。
     * 必须
     */
    int ACK_NTF_USER_HEARTBEAT = 0x11;
    /**
     * BAS --> Portal server
     * 表示此报文是 BAS 向Portal Server 发送的Challenge请求报文，主要适用于EAP_TLS认证。
     * 建议
     */
    int NTF_CHALLENGE = 0x12;
    /**
     * BAS --> Portal server
     * 用户消息通知报文。在Pap/Chap认证方式下，计费回应报文中Radius服务器需要向用户下发一些消息，例如帐号余额等信息。
     * 建议
     */
    int NTF_USER_NOTIFY = 0x13;
    /**
     * Portal server --> BAS
     * PortalServer通知BAS消息已收到
     * 建议
     */
    int AFF_NTF_USER_NOTIFY = 0x14;
}
