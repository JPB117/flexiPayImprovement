package com.icpak.rest.models.membership;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(value="Member Model", description="ICPAK Member Model")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonSerialize(include=Inclusion.NON_NULL)

@Entity
@Table
public class Member extends PO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userRefId;

	public Member() {
	}
	
	public Member(String userId) {
		this.userRefId = userId;
	}
	
	public String getUserRefId() {
		return userRefId;
	}

	public void setUserRefId(String userRefId) {
		this.userRefId = userRefId;
	}
}
