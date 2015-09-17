package com.workpoint.icpak.shared.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by wladek on 9/17/15.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class CreditCardResponse implements Serializable{

//    {
//        "content":{"invalid_parameters":"'Card Number must be exactly 16 characters in length.'," +
//            "'Address1 is required.','Expiry Date must be exactly 6 characters in length.'," +
//            "'Country is required.','State is required.','Zip Code is required.'," +
//            "'Security Code can not exceed 4 characters in length.','Currency is required.'"},
//
//        "status":{"status_code":2000,
//            "status_description":"Invalid Parameters",
//            "status":"FAIL"}
//    }

    private String invalidParams;
    private String statusCode;
    private String statusDesc;
    private String status;

    public String getInvalidParams() {
        return invalidParams;
    }

    public void setInvalidParams(String invalidParams) {
        this.invalidParams = invalidParams;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CreditCardResponse(){
    }

    @Override
    public String toString() {
        return "CreditCardResponse{" +
                "invalidParams='" + invalidParams + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", statusDesc='" + statusDesc + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}