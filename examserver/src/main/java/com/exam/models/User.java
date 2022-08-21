package com.exam.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="USERS")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String username;
	private String first_name;
	private String last_name;
	private String password;
	private String email;
	private String phone_no;
	private boolean enabled = true;
	private String profile;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "user")
	@JsonIgnore
	private Set<UserRole> userRole= new HashSet<>();
	
	
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getFirst_name() {
		return first_name;
	}



	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}



	public String getLast_name() {
		return last_name;
	}



	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}


	
	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPhone_no() {
		return phone_no;
	}



	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}



	public boolean isEnabled() {
		return enabled;
	}



	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}



	public String getProfile() {
		return profile;
	}



	public void setProfile(String profile) {
		this.profile = profile;
	}



	public Set<UserRole> getUserRole() {
		return userRole;
	}



	public void setUserRole(Set<UserRole> userRole) {
		this.userRole = userRole;
	}



	public User() {
		super();
	}



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Set<Authority> set=new HashSet<>();
		this.userRole.forEach(userrol->{
			set.add(new Authority(userrol.getRole().getRoleName()));
		});
		return set;
	}



	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}



	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}



	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}


	
}
