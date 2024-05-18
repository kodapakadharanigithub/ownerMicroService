package com.hcl.ownermicroservice.entities;

//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Ride {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int rideId;
	private String pickUpLocation;
	private String dropLocation;
	private String rideDate;
	private String rideTime;
	private String status;
	private double price;
	private String vehicleId;
	private int ownerId;
	public Ride()
	{
		
	}
	public Ride(int rideId, String pickUpLocation, String dropLocation, String rideDate, String rideTime, String status,
			double price, String vehicleId, int ownerId) {
		super();
		this.rideId = rideId;
		this.pickUpLocation = pickUpLocation;
		this.dropLocation = dropLocation;
		this.rideDate = rideDate;
		this.rideTime = rideTime;
		this.status = status;
		this.price = price;
		this.vehicleId = vehicleId;
		this.ownerId = ownerId;
	}
	public int getRideId() {
		return rideId;
	}
	public void setRideId(int rideId) {
		this.rideId = rideId;
	}
	public String getPickUpLocation() {
		return pickUpLocation;
	}
	public void setPickUpLocation(String pickUpLocation) {
		this.pickUpLocation = pickUpLocation;
	}
	public String getDropLocation() {
		return dropLocation;
	}
	public void setDropLocation(String dropLocation) {
		this.dropLocation = dropLocation;
	}
	public String getRideDate() {
		return rideDate;
	}
	public void setRideDate(String rideDate) {
		this.rideDate = rideDate;
	}
	public String getRideTime() {
		return rideTime;
	}
	public void setRideTime(String rideTime) {
		this.rideTime = rideTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	@Override
	public String toString() {
		return "Ride [rideId=" + rideId + ", pickUpLocation=" + pickUpLocation + ", dropLocation=" + dropLocation
				+ ", rideDate=" + rideDate + ", rideTime=" + rideTime + ", status=" + status + ", price=" + price
				+ ", vehicleId=" + vehicleId + ", ownerId=" + ownerId + "]";
	}
	
	
	
	
	
	

}
