/**
 * $Id: RadiusProxyConnection.java,v 1.2 2005/10/11 14:18:27 wuttke Exp $
 * Created on 07.09.2005
 * @author glanz, Matthias Wuttke
 * @version $Revision: 1.2 $
 */
package com.alone.coder.radius.tinyradius.proxy;


import com.alone.coder.radius.tinyradius.packet.RadiusPacket;
import com.alone.coder.radius.tinyradius.util.RadiusEndpoint;
import lombok.Getter;

/**
 * This class stores information about a proxied packet.
 * It contains two RadiusEndpoint objects representing the Radius client
 * and server, the port number the proxied packet arrived
 * at originally and the proxied packet itself.
 */
@Getter
public class RadiusProxyConnection {

	/**
	 * Creates a RadiusProxyConnection object.
	 * @param radiusServer server endpoint
	 * @param radiusClient client endpoint
	 * @param port port the proxied packet arrived at originally 
	 */
	public RadiusProxyConnection(RadiusEndpoint radiusServer, RadiusEndpoint radiusClient, RadiusPacket packet, int port) {
		this.radiusServer = radiusServer;
		this.radiusClient = radiusClient;
		this.packet = packet;
		this.port = port;
	}

    /**
     * -- GETTER --
     *  Returns the Radius endpoint of the server.
     *
     * @return endpoint
     */
    private RadiusEndpoint radiusServer;
    /**
     * -- GETTER --
     *  Returns the Radius endpoint of the client.
     *
     * @return endpoint
     */
    private RadiusEndpoint radiusClient;
    /**
     * -- GETTER --
     *  Returns the port number the proxied packet arrived at
     *  originally.
     *
     * @return port number
     */
    private int port;
    /**
     * -- GETTER --
     *  Returns the proxied packet.
     *
     * @return packet
     */
    private RadiusPacket packet;
	
}
