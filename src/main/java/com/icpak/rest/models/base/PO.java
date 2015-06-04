package com.icpak.rest.models.base;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.hibernate.annotations.Index;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.icpak.rest.IDUtils;

//@JsonInclude(Include.NON_NULL)
//@JsonSerialize(include=Inclusion.NON_NULL)
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@MappedSuperclass
public abstract class PO extends ResourceModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@XmlTransient
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable=false, unique=true, updatable=false, length=45)
    //@Index(name="idx_ref_id")
	protected String refId;

	@XmlTransient
	@Column
	private String createdBy;
	
	@XmlTransient
	@Column
	private String updatedBy;
	
	@XmlTransient
	@Column
	private Date created;
	
	@XmlTransient
	@Column
	private Date updated;
	
	@XmlTransient
	@Column(columnDefinition="int default 1")
	private int isActive=1;

	public void init(){
		if(this.getId()==null){
			created = new Date(System.currentTimeMillis());
			createdBy = null;
		}else{
			updated= new Date(System.currentTimeMillis());
			createdBy=null;
		}
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {		
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	@PrePersist
	public void onPrePersist(){
		this.created=new Date();
		
		Subject subject = SecurityUtils.getSubject();
		if(subject.getPrincipal()!=null){
			this.createdBy = subject.getPrincipal().toString();
		}
		
		if(refId==null){
			refId = IDUtils.generateId();
		}
	}
	
	@PreUpdate
	public void onPreUpdate(){
		this.updated=new Date();
		Subject subject = SecurityUtils.getSubject();
		if(subject.getPrincipal()!=null){
			this.createdBy = subject.getPrincipal().toString();
		}
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}		
	
	public Date getLastModified(){
		if(updated!=null){
			return updated;
		}
		
		return created;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}
}
