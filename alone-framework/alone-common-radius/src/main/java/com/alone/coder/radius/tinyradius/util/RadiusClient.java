/**
 * $Id: RadiusClient.java,v 1.7 2005/11/10 10:20:21 wuttke Exp $
 * Created on 09.04.2005
 *
 * @author Matthias Wuttke
 * @version $Revision: 1.7 $
 */
package com.alone.coder.radius.tinyradius.util;


import com.alone.coder.radius.tinyradius.packet.AccessRequest;
import com.alone.coder.radius.tinyradius.packet.AccountingRequest;
import com.alone.coder.radius.tinyradius.packet.RadiusPacket;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;

/**
 * This object represents a simple Radius client which communicates with
 * a specified Radius server. You can use a single instance of this object
 * to authenticate or account different users with the same Radius server
 * as long as you authenticate/account one user after the other. This object
 * is thread safe, but only opens a single socket so operations using this
 * socket are synchronized to avoid confusion with the mapping of request
 * and result packets.
 */
public class RadiusClient {

    /**
     * Creates a new Radius client object for a special Radius server.
     *
     * @param hostName     host name of the Radius server
     * @param sharedSecret shared secret used to secure the communication
     */
    public RadiusClient(String hostName, String sharedSecret) {
        setHostName(hostName);
        setSharedSecret(sharedSecret);
    }

    /**
     * Constructs a Radius client for the given Radius endpoint.
     *
     * @param client Radius endpoint
     */
    public RadiusClient(RadiusEndpoint client) {
        this(client.getEndpointAddress().getAddress().getHostAddress(), client.getSharedSecret());
    }

    /**
     * Authenticates a user via PAP.
     *
     * @param userName user name
     * @param password password
     * @return true if authentication is successful, false otherwise
     * @throws RadiusException malformed packet
     * @throws IOException     communication error (after getRetryCount()
     *                         retries)
     */
    public synchronized boolean authenticate(String userName, String password) throws IOException, RadiusException {
        return authenticate(userName, password, authProtocol);
    }

    /**
     * Authenticates a user.
     *
     * @param userName user name
     * @param password password
     * @param protocol either {@link AccessRequest#AUTH_PAP} or {@link AccessRequest#AUTH_CHAP}
     * @return true if authentication is successful, false otherwise
     * @throws RadiusException malformed packet
     * @throws IOException     communication error (after getRetryCount()
     *                         retries)
     */
    public synchronized boolean authenticate(String userName, String password, String protocol) throws IOException, RadiusException {
        AccessRequest request = new AccessRequest(userName, password);
        request.setAuthProtocol(protocol);
        RadiusPacket response = authenticate(request);
        return response.getPacketType() == RadiusPacket.ACCESS_ACCEPT;
    }

    /**
     * Sends an Access-Request packet and receives a response
     * packet.
     *
     * @param request request packet
     * @return Radius response packet
     * @throws RadiusException malformed packet
     * @throws IOException     communication error (after getRetryCount()
     *                         retries)
     */
    public synchronized RadiusPacket authenticate(AccessRequest request) throws IOException, RadiusException {
        if (logger.isInfoEnabled())
            logger.info("send Access-Request packet: {}", request);

        RadiusPacket response = communicate(request, getAuthPort());
        if (logger.isInfoEnabled())
            logger.info("received packet: " + response);

        return response;
    }

    /**
     * Sends an Accounting-Request packet and receives a response
     * packet.
     *
     * @param request request packet
     * @return Radius response packet
     * @throws RadiusException malformed packet
     * @throws IOException     communication error (after getRetryCount()
     *                         retries)
     */
    @Synchronized
    public RadiusPacket account(AccountingRequest request) throws IOException, RadiusException {
        if (logger.isInfoEnabled())
            logger.info("send Accounting-Request packet: {}", request);

        RadiusPacket response = communicate(request, getAcctPort());
        if (logger.isInfoEnabled())
            logger.info("received packet: " + response);

        return response;
    }

    /**
     * Closes the socket of this client.
     */
    public void close() {
        if (serverSocket != null)
            serverSocket.close();
    }

    /**
     * Sets the auth port of the Radius server.
     *
     * @param authPort auth port, 1-65535
     */
    public void setAuthPort(int authPort) {
        if (authPort < 1 || authPort > 65535)
            throw new IllegalArgumentException("bad port number");
        this.authPort = authPort;
    }

    /**
     * Sets the host name of the Radius server.
     *
     * @param hostName host name
     */
    public void setHostName(String hostName) {
        if (hostName == null || hostName.isEmpty())
            throw new IllegalArgumentException("host name must not be empty");
        this.hostName = hostName;
    }

    /**
     * Sets the retry count for failed transmissions.
     *
     * @param retryCount 重试次数
     * @throws IllegalArgumentException when retry count is not positive
     */
    public void setRetryCount(int retryCount) {
        if (retryCount < 1)
            throw new IllegalArgumentException("retry count must be positive");
        this.retryCount = retryCount;
    }

    /**
     * Sets the secret shared between server and client.
     *
     * @param sharedSecret shared secret
     */
    public void setSharedSecret(String sharedSecret) {
        if (sharedSecret == null || sharedSecret.isEmpty())
            throw new IllegalArgumentException("shared secret must not be empty");
        this.sharedSecret = sharedSecret;
    }

    /**
     * Sets the socket timeout
     *
     * @param socketTimeout timeout, ms, >0
     * @throws SocketException when socket timeout, >0 ms
     */
    public void setSocketTimeout(int socketTimeout) throws SocketException {
        if (socketTimeout < 1)
            throw new IllegalArgumentException("socket tiemout must be positive");
        this.socketTimeout = socketTimeout;
        if (serverSocket != null)
            serverSocket.setSoTimeout(socketTimeout);
    }

    /**
     * Sets the Radius server accounting port.
     *
     * @param acctPort acct port, 1-65535
     */
    public void setAcctPort(int acctPort) {
        if (acctPort < 1 || acctPort > 65535)
            throw new IllegalArgumentException("bad port number");
        this.acctPort = acctPort;
    }

    /**
     * Sends a Radius packet to the server and awaits an answer.
     *
     * @param request packet to be sent
     * @param port    server port number
     * @return response Radius packet
     * @throws RadiusException malformed packet
     * @throws IOException     communication error (after getRetryCount()
     *                         retries)
     */
    public RadiusPacket communicate(RadiusPacket request, int port) throws IOException, RadiusException {
        DatagramPacket packetIn = new DatagramPacket(new byte[RadiusPacket.MAX_PACKET_LENGTH], RadiusPacket.MAX_PACKET_LENGTH);
        DatagramPacket packetOut = makeDatagramPacket(request, port);

        try (DatagramSocket socket = getSocket()) {
            for (int i = 1; i <= getRetryCount(); i++) {
                try {
                    socket.send(packetOut);
                    socket.receive(packetIn);
                    return makeRadiusPacket(packetIn, request);
                } catch (IOException ioex) {
                    if (i == getRetryCount()) {
                        if (logger.isErrorEnabled()) {
                            if (ioex instanceof SocketTimeoutException)
                                logger.error("communication failure (timeout), no more retries");
                            else
                                logger.error("communication failure, no more retries", ioex);
                        }
                        throw ioex;
                    }
                    if (logger.isInfoEnabled())
                        logger.info("communication failure, retry " + i);
                    // TODO increase Acct-Delay-Time by getSocketTimeout()/1000
                    // this changes the packet authenticator and requires packetOut to be
                    // calculated again (call makeDatagramPacket)
                }
            }
        }

        return null;
    }

    /**
     * Sends the specified packet to the specified Radius server endpoint.
     *
     * @param remoteServer Radius endpoint consisting of server address,
     *                     port number and shared secret
     * @param request      Radius packet to be sent
     * @return received response packet
     * @throws RadiusException malformed packet
     * @throws IOException     error while communication
     */
    public static RadiusPacket communicate(RadiusEndpoint remoteServer, RadiusPacket request) throws RadiusException, IOException {
        RadiusClient rc = new RadiusClient(remoteServer);
        return rc.communicate(request, remoteServer.getEndpointAddress().getPort());
    }

    /**
     * Returns the socket used for the server communication. It is
     * bound to an arbitrary free local port number.
     *
     * @return local socket
     * @throws SocketException
     */
    protected DatagramSocket getSocket() throws SocketException {
        if (serverSocket == null || serverSocket.isClosed()) {
            serverSocket = new DatagramSocket();
            serverSocket.setSoTimeout(getSocketTimeout());
        }
        return serverSocket;
    }

    /**
     * Creates a datagram packet from a RadiusPacket to be send.
     *
     * @param packet RadiusPacket
     * @param port   destination port number
     * @return new datagram packet
     * @throws IOException
     */
    protected DatagramPacket makeDatagramPacket(RadiusPacket packet, int port) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        packet.encodeRequestPacket(bos, getSharedSecret());
        byte[] data = bos.toByteArray();

        InetAddress address = InetAddress.getByName(getHostName());
        DatagramPacket datagram = new DatagramPacket(data, data.length, address, port);
        return datagram;
    }

    /**
     * Creates a RadiusPacket from a received datagram packet.
     *
     * @param packet  received datagram
     * @param request Radius request packet
     * @return RadiusPacket object
     */
    protected RadiusPacket makeRadiusPacket(DatagramPacket packet, RadiusPacket request) throws IOException, RadiusException {
        ByteArrayInputStream in = new ByteArrayInputStream(packet.getData());
        return RadiusPacket.decodeResponsePacket(in, getSharedSecret(), request);
    }

    /**
     * -- GETTER --
     * Returns the Radius server auth port.
     *
     * @return auth port
     */
    @Getter
    private int authPort = 1812;
    /**
     * -- GETTER --
     *  Returns the Radius server accounting port.
     *
     * @return acct port
     */
    @Getter
    private int acctPort = 1813;
    /**
     * -- GETTER --
     * Returns the host name of the Radius server.
     *
     * @return host name
     */
    @Getter
    private String hostName = null;
    /**
     * -- GETTER --
     * Returns the secret shared between server and client.
     *
     * @return shared secret
     */
    @Getter
    private String sharedSecret = null;
    private DatagramSocket serverSocket = null;
    /**
     * -- GETTER --
     * Returns the retry count for failed transmissions.
     *
     * @return retry count
     */
    @Getter
    private int retryCount = 3;
    /**
     * -- GETTER --
     * Returns the socket timeout.
     *
     * @return socket timeout, ms
     */
    @Getter
    private int socketTimeout = 3000;
    /**
     * -- SETTER --
     *  Set the Radius authentication protocol.
     *
     * @param protocol the protocol, PAP or CHAP
     *
     */
    @Setter
    private String authProtocol = AccessRequest.AUTH_PAP;
    private static Logger logger = LoggerFactory.getLogger(RadiusClient.class);

}
