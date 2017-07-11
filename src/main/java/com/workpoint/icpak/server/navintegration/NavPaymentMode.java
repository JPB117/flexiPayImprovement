
package com.workpoint.icpak.server.navintegration;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Payment_Mode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Payment_Mode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_blank_"/>
 *     &lt;enumeration value="Cash"/>
 *     &lt;enumeration value="Cheque"/>
 *     &lt;enumeration value="MPESA"/>
 *     &lt;enumeration value="Credit_Card"/>
 *     &lt;enumeration value="Online_Payment"/>
 *     &lt;enumeration value="EFT"/>
 *     &lt;enumeration value="Deposit_Slip"/>
 *     &lt;enumeration value="Banker_x0027_s_Cheque"/>
 *     &lt;enumeration value="RTGS"/>
 *     &lt;enumeration value="Custom3"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Payment_Mode")
@XmlEnum
public enum NavPaymentMode {

    @XmlEnumValue("_blank_")
    BLANK("_blank_"),
    @XmlEnumValue("Cash")
    CASH("Cash"),
    @XmlEnumValue("Cheque")
    CHEQUE("Cheque"),
    MPESA("MPESA"),
    @XmlEnumValue("Credit_Card")
    CREDIT_CARD("Credit_Card"),
    @XmlEnumValue("Online_Payment")
    ONLINE_PAYMENT("Online_Payment"),
    EFT("EFT"),
    @XmlEnumValue("Deposit_Slip")
    DEPOSIT_SLIP("Deposit_Slip"),
    @XmlEnumValue("Banker_x0027_s_Cheque")
    BANKER_X_0027_S_CHEQUE("Banker_x0027_s_Cheque"),
    RTGS("RTGS"),
    @XmlEnumValue("Custom3")
    CUSTOM_3("Custom3");
    private final String value;

    NavPaymentMode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NavPaymentMode fromValue(String v) {
        for (NavPaymentMode c: NavPaymentMode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
