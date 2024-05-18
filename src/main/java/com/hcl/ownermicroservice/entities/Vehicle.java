package com.hcl.ownermicroservice.entities;

//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Vehicle {
	
	@Id
	private String vehicleId;
	@Column(nullable=false)
	private String type;
	private String model;
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="owner_id_fk")
	private Owner owner;
	public Vehicle()
	{
		
	}
	public Vehicle(String vehicleId, String type, String model, Owner owner) {
		super();
		this.vehicleId = vehicleId;
		this.type = type;
		this.model = model;
		this.owner = owner;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "Vehicle [vehicleId=" + vehicleId + ", type=" + type + ", model=" + model  + "]";
	}
	
}
