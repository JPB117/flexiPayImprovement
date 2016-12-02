
package com.workpoint.icpak.server.navintegration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetRecIdFromKey_Result" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getRecIdFromKeyResult"
})
@XmlRootElement(name = "GetRecIdFromKey_Result")
public class GetRecIdFromKeyResult {

    @XmlElement(name = "GetRecIdFromKey_Result", required = true)
    protected String getRecIdFromKeyResult;

    /**
     * Gets the value of the getRecIdFromKeyResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetRecIdFromKeyResult() {
        return getRecIdFromKeyResult;
    }

    /**
     * Sets the value of the getRecIdFromKeyResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetRecIdFromKeyResult(String value) {
        this.getRecIdFromKeyResult = value;
    }

}
