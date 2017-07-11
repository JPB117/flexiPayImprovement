package com.workpoint.icpak.shared.model.statement;

import java.util.Date;

import com.workpoint.icpak.shared.model.HasKey;
import com.workpoint.icpak.shared.model.Listable;
import com.workpoint.icpak.shared.model.SerializableObj;

/**
 * Created by wladek on 9/18/15.
 */
public class StatementDto extends SerializableObj implements Listable, HasKey{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String entryNo;
    private String custLedgerEntryNo;
    private String entryType;
    private Date postingDate;
    private String documentType;
    private String documentNo;
    private Double amount;
    private String description;
    private String customerNo;
    private Date dueDate;

    public String getEntryNo() {
        return entryNo;
    }

    public void setEntryNo(String entryNo) {
        this.entryNo = entryNo;
    }

    public String getCustLedgerEntryNo() {
        return custLedgerEntryNo;
    }

    public void setCustLedgerEntryNo(String custLedgerEntryNo) {
        this.custLedgerEntryNo = custLedgerEntryNo;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    @Override
    public String getName() {
        return entryNo;
    }

    @Override
    public String getDisplayName() {
        return entryNo;
    }

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return getRefId();
	}

	public Date getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
}