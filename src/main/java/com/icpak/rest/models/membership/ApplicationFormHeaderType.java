package com.icpak.rest.models.membership;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;

@Entity
@Table(name="`Application form header Type`")
public class ApplicationFormHeaderType extends PO{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="`timestamp`",columnDefinition="timestamp NOT NULL default current_timestamp")
	private Timestamp timestamp;
	
	@Column(nullable=false, name="`Code`", length=20)
	private String code;
	
	@Column(nullable=false, name="`Description`", length=50)
	private String description;
	
	public ApplicationFormHeaderType() {
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
