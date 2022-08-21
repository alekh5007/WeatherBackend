package com.exam.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="roles")
public class Role {

	@Id
	private Long roleid;
	private String roleName;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "role")
	private Set<UserRole> userRole=new HashSet<>();
	
	
	public Long getRoleid() {
		return roleid;
	}


	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}


	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	public Set<UserRole> getUserRole() {
		return userRole;
	}


	public void setUserRole(Set<UserRole> userRole) {
		this.userRole = userRole;
	}


	public Role() {
		super();
	}


}
