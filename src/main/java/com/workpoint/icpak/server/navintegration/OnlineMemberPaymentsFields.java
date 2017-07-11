
package com.workpoint.icpak.server.navintegration;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Online_Member_Payments_Fields.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Online_Member_Payments_Fields">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Transaction_No"/>
 *     &lt;enumeration value="Transaction_Code"/>
 *     &lt;enumeration value="Payment_Mode"/>
 *     &lt;enumeration value="Transaction_Date"/>
 *     &lt;enumeration value="Account_No"/>
 *     &lt;enumeration value="Name"/>
 *     &lt;enumeration value="Amount"/>
 *     &lt;enumeration value="Description"/>
 *     &lt;enumeration value="Status"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Online_Member_Payments_Fields")
@XmlEnum
public enum OnlineMemberPaymentsFields {

    @XmlEnumValue("Transaction_No")
    TRANSACTION_NO("Transaction_No"),
    @XmlEnumValue("Transaction_Code")
    TRANSACTION_CODE("Transaction_Code"),
    @XmlEnumValue("Payment_Mode")
    PAYMENT_MODE("Payment_Mode"),
    @XmlEnumValue("Transaction_Date")
    TRANSACTION_DATE("Transaction_Date"),
    @XmlEnumValue("Account_No")
    ACCOUNT_NO("Account_No"),
    @XmlEnumValue("Name")
    NAME("Name"),
    @XmlEnumValue("Amount")
    AMOUNT("Amount"),
    @XmlEnumValue("Description")
    DESCRIPTION("Description"),
    @XmlEnumValue("Status")
    STATUS("Status");
    private final String value;

    OnlineMemberPaymentsFields(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OnlineMemberPaymentsFields fromValue(String v) {
        for (OnlineMemberPaymentsFields c: OnlineMemberPaymentsFields.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
