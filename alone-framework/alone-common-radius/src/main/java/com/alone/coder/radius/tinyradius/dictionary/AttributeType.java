/**
 * $Id: AttributeType.java,v 1.3 2005/09/06 18:06:33 wuttke Exp $
 *
 * @author Matthias Wuttke
 * @version $Revision: 1.3 $
 */
package com.alone.coder.radius.tinyradius.dictionary;


import com.alone.coder.radius.tinyradius.attribute.RadiusAttribute;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Radius attribute type.
 */
public class AttributeType {

    /**
     * Create a new attribute type.
     *
     * @param code Radius attribute type code
     * @param name Attribute type name
     * @param type RadiusAttribute descendant who handles
     *             attributes of this type
     */
    public AttributeType(int code, String name, Class type) {
        setTypeCode(code);
        setName(name);
        setAttributeClass(type);
    }

    /**
     * Constructs a Vendor-Specific sub-attribute type.
     *
     * @param vendor vendor ID
     * @param code   sub-attribute type code
     * @param name   sub-attribute name
     * @param type   sub-attribute class
     */
    public AttributeType(int vendor, int code, String name, Class type) {
        setTypeCode(code);
        setName(name);
        setAttributeClass(type);
        setVendorId(vendor);
    }

    /**
     * Sets the Radius type code for this attribute type.
     *
     * @param code type code, 1-255
     */
    public void setTypeCode(int code) {
        if (code < 1 || code > 255)
            throw new IllegalArgumentException("code out of bounds");
        this.typeCode = code;
    }

    /**
     * Sets the name of this type.
     *
     * @param name type name
     */
    public void setName(String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("name is empty");
        this.name = name;
    }

    /**
     * Sets the RadiusAttribute descendant class which represents
     * attributes of this type.
     */
    public void setAttributeClass(Class type) {
        if (type == null)
            throw new NullPointerException("type is null");
        if (!RadiusAttribute.class.isAssignableFrom(type))
            throw new IllegalArgumentException("type is not a RadiusAttribute descendant");
        this.attributeClass = type;
    }

    /**
     * Returns the name of the given integer value if this attribute
     * is an enumeration, or null if it is not or if the integer value
     * is unknown.
     *
     * @return name
     */
    public String getEnumeration(int value) {
        if (enumeration != null) {
            return enumeration.get(value);
        }
        return null;
    }

    /**
     * Returns the number of the given string value if this attribute is
     * an enumeration, or null if it is not or if the string value is unknown.
     *
     * @param value string value
     * @return Integer or null
     */
    public Integer getEnumeration(String value) {
        if (value == null || value.isEmpty())
            throw new IllegalArgumentException("value is empty");
        if (enumeration == null)
            return null;
        for (Map.Entry<Integer, String> e : enumeration.entrySet()) {
            if (e.getValue().equals(value))
                return e.getKey();
        }
        return null;
    }

    /**
     * Adds a name for an integer value of this attribute.
     *
     * @param num  number that shall get a name
     * @param name the name for this number
     */
    public void addEnumerationValue(int num, String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("name is empty");
        if (enumeration == null)
            enumeration = new HashMap<>();
        enumeration.put(num, name);
    }

    /**
     * String representation of AttributeType object
     * for debugging purposes.
     *
     * @return string
     * @see Object#toString()
     */
    public String toString() {
        String s = getTypeCode() + "/" + getName() + ": " + attributeClass.getName();
        if (getVendorId() != -1)
            s += " (vendor " + getVendorId() + ")";
        return s;
    }

    /**
     *  No vendor specific attribute = -1
     *  Sets the vendor ID.
     */
    @Setter
    @Getter
    private int vendorId = -1;
    /**
     * -- GETTER --
     * Retrieves the Radius type code for this attribute type.
     */
    @Getter
    private int typeCode;
    /**
     * -- GETTER --
     * Retrieves the name of this type.
     */
    @Getter
    private String name;
    /**
     * -- GETTER --
     *  Retrieves the RadiusAttribute descendant class which represents
     *  attributes of this type.
     *
     */
    @Getter
    private Class attributeClass;
    private Map<Integer,String> enumeration = null;

}
