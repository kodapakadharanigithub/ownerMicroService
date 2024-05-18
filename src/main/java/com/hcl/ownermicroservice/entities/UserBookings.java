package com.hcl.ownermicroservice.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserBookings {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int bookingId;
	private int userId;
	@Column(nullable=false)
	private String bookingDate;
	@Column(nullable=false)
	private String bookingTime;
	@Column(nullable=false)
	private String pickupLocation;
	@Column(nullable=false)
	private String dropLocation;
	private int rideId;
	private String vehicleId;
	private String status;
	public UserBookings()
	{
		
	}
	public UserBookings(int bookingId, int userId, String bookingDate, String bookingTime, String pickupLocation,
			String dropLocation, int rideId, String vehicleId, String status) {
		super();
		this.bookingId = bookingId;
		this.userId = userId;
		this.bookingDate = bookingDate;
		this.bookingTime = bookingTime;
		this.pickupLocation = pickupLocation;
		this.dropLocation = dropLocation;
		this.rideId = rideId;
		this.vehicleId = vehicleId;
		this.status = status;
	}
	public int getBookingId() {
		return bookingId;
	}
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}
	public String getBookingTime() {
		return bookingTime;
	}
	public void setBookingTime(String bookingTime) {
		this.bookingTime = bookingTime;
	}
	public String getPickupLocation() {
		return pickupLocation;
	}
	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}
	public String getDropLocation() {
		return dropLocation;
	}
	public void setDropLocation(String dropLocation) {
		this.dropLocation = dropLocation;
	}
	public int getRideId() {
		return rideId;
	}
	public void setRideId(int rideId) {
		this.rideId = rideId;
	}
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "UserBookings [bookingId=" + bookingId + ", userId=" + userId + ", bookingDate=" + bookingDate
				+ ", bookingTime=" + bookingTime + ", pickupLocation=" + pickupLocation + ", dropLocation="
				+ dropLocation + ", rideId=" + rideId + ", vehicleId=" + vehicleId + ", status=" + status + "]";
	}
	
	
	
	
	
}
