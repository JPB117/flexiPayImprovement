package com.icpak.rest.models.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Index;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(value="Permission Model", description="Represents an assignable Permission")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

@Entity
@Table(name="permission")
public class Permission extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(unique=true, nullable=false)
	@Index(name="idx_permission_name")
	private String name;
	
	private String description;
	
	public Permission() {
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "{name:"+name+",description:"+description+",permissionid:"+refId+"}";
	}
	
	public Permission clone(String ... token){
		Permission permission = new Permission();
		permission.setName(name);
		permission.setDescription(description);
		
		return permission;
	}
}
