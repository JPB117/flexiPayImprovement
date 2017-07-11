
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
 *         &lt;element name="Online_Member_Payments" type="{urn:microsoft-dynamics-schemas/page/online_member_payments}Online_Member_Payments" minOccurs="0"/>
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
    "onlineMemberPayments"
})
@XmlRootElement(name = "Read_Result")
public class ReadResult {

    @XmlElement(name = "Online_Member_Payments")
    protected OnlineMemberPayments onlineMemberPayments;

    /**
     * Gets the value of the onlineMemberPayments property.
     * 
     * @return
     *     possible object is
     *     {@link OnlineMemberPayments }
     *     
     */
    public OnlineMemberPayments getOnlineMemberPayments() {
        return onlineMemberPayments;
    }

    /**
     * Sets the value of the onlineMemberPayments property.
     * 
     * @param value
     *     allowed object is
     *     {@link OnlineMemberPayments }
     *     
     */
    public void setOnlineMemberPayments(OnlineMemberPayments value) {
        this.onlineMemberPayments = value;
    }

}
