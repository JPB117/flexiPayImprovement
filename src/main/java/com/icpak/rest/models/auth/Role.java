/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.icpak.rest.models.auth;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;

/**
 * Model object that represents a security role.
 */
@ApiModel(value="Role Model", description="Model of a role")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(User.class)

@Entity
@Table(name="role")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Role extends PO{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
    @Basic(optional=false)
    @Column(length=100, unique=true,nullable=false)
    @Index(name="idx_role_name")
    private String name;

    @Basic(optional=false)
    @Column(length=255)
    private String description;

    @ElementCollection(fetch=FetchType.LAZY)
    @CollectionTable(name="role_permission", joinColumns=
    		@JoinColumn(name="roleid", referencedColumnName="id"))
    @Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
    @Column(name="permission")
    private Set<String> permissions = new HashSet<>();
    
    @ManyToMany(fetch=FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    protected Role() {
    }

    public Role(String name) {
        this.name = name;
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

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

	public void addUser(User user) {
		users.add(user);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Role)){
			return false;
		}
		
		Role other = (Role)obj;
		
		return other.getRefId().equals(getRefId());
	}
	
	public Role clone(String ... expand){
		
		Role role = new Role();
		role.setRefId(getRefId());
		role.setName(name);
		
		if(expand!=null){
			for(String token: expand){
				if(token.equals("users")){
					for(User user: users){
						role.addUser(user.clone());
					}
				}
				
				if(token.equals("permissions")){
					role.setPermissions(permissions);
				}
			}
		}
		return role;
	}

	public void addPermission(Permission permission) {
		permissions.add(permission.getName());
	}

	public void removePermission(Permission permission) {
		permissions.remove(permission);
	}

	public void setUsers(Collection<User> members) {
		users.addAll(members);
	}
	
	public Collection<User> getUsers(){
		return users;
	}

}


