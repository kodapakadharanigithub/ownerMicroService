package com.hcl.ownermicroservice.entities;

import java.util.ArrayList;
import java.util.List;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Owner {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer ownerId;
	@Column(nullable=false,unique=true)
	@NotNull
	@Size(min=2, message="OwnerName should Greater than 2 characters")
	private String  ownerName;
	@Column(nullable=false)
	@NotNull
	@Size(min=8, message="Password must be Contain 8 characters")
	private String passWord;
	@OneToMany(cascade=CascadeType.ALL,mappedBy="owner")
	private List<Vehicle> vehicleList=new ArrayList<>();
	public Owner()
	{
		
	}
	public Owner( String ownerName, String passWord) {
		super();
		//this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.passWord = passWord;
	}
	public Owner(int ownerId, String ownerName, String passWord) {
		super();
		this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.passWord = passWord;
	}
	
	public Owner(Integer ownerId, String ownerName, String passWord, List<Vehicle> vehicleList) {
		super();
		this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.passWord = passWord;
		this.vehicleList = vehicleList;
	}
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	@Override
	public String toString() {
		return "Owner [ownerId="  + ", ownerName=" + ownerName + ", passWord=" + passWord + "]";
	}
	
	
	

}
