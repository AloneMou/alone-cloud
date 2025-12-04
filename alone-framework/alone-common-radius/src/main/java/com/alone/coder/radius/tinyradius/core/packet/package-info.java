/**
 * Classes for encoding and decoding Radius packets.
 * <p>
 * The interface {@link com.alone.coder.radius.tinyradius.core.packet.RadiusPacket} supports
 * getters for the core elements of a packet. {@link com.alone.coder.radius.tinyradius.core.packet.BaseRadiusPacket}
 * provides the reference implementation with support for managing attributes.
 * <p>
 * {@link com.alone.coder.radius.tinyradius.core.packet.request.RadiusRequest} and {@link com.alone.coder.radius.tinyradius.core.packet.response.RadiusResponse}
 * support encoding and verifying authenticators for requests and responses
 * respectively. Other subclasses inherit from these to support their specific
 * authentication mechanisms and data validation.
 * <p>
 * Each Radius packet has an associated dictionary of attribute
 * types that allows the convenient access to packet attributes
 * and sub-attributes.
 */
package com.alone.coder.radius.tinyradius.core.packet;