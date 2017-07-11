
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
 *         &lt;element name="Online_Member_Payments_List" type="{urn:microsoft-dynamics-schemas/page/online_member_payments}Online_Member_Payments_List"/>
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
    "onlineMemberPaymentsList"
})
@XmlRootElement(name = "UpdateMultiple_Result")
public class UpdateMultipleResult {

    @XmlElement(name = "Online_Member_Payments_List", required = true)
    protected OnlineMemberPaymentsList onlineMemberPaymentsList;

    /**
     * Gets the value of the onlineMemberPaymentsList property.
     * 
     * @return
     *     possible object is
     *     {@link OnlineMemberPaymentsList }
     *     
     */
    public OnlineMemberPaymentsList getOnlineMemberPaymentsList() {
        return onlineMemberPaymentsList;
    }

    /**
     * Sets the value of the onlineMemberPaymentsList property.
     * 
     * @param value
     *     allowed object is
     *     {@link OnlineMemberPaymentsList }
     *     
     */
    public void setOnlineMemberPaymentsList(OnlineMemberPaymentsList value) {
        this.onlineMemberPaymentsList = value;
    }

}
