package com.hcl.ownermicroservice.service;

import java.text.ParseException;
import java.util.List;

import com.hcl.ownermicroservice.dto.BookingRequest;
import com.hcl.ownermicroservice.dto.QueryRequest;
import com.hcl.ownermicroservice.entities.Owner;
import com.hcl.ownermicroservice.entities.QueryResponse;
import com.hcl.ownermicroservice.entities.Ride;
import com.hcl.ownermicroservice.entities.UserBookings;
import com.hcl.ownermicroservice.entities.Vehicle;



public interface OwnerService {
	
	public List<Owner> getAllOwners();
	public Owner ownerRegister(Owner owner);
	public boolean getOwnerByName(String ownerName);
	public Vehicle insertVehicle(Vehicle vehicle);
	public boolean getVehicleById(String vehicleId);
	public List<Vehicle> getAllMyVehicles(int ownerId);
	public boolean deleteVehicle(String vehicleId,int ownerId);
	public boolean updateVehicle(Vehicle vehicle,int ownerId);
	public String insertRide(Ride ride);
	public boolean getRideById(int rideId);
	public boolean deleteRide(int rideId,int ownerId);
	public List<Ride> getAllMyRides(int ownerId);
	public List<Ride> getVehicleRidesByOwnerId(String vehicleId,int ownerId);
	public List<String> confirmBooking(int ownerId,int userId);
	public String  DateConversion(String date) throws ParseException;
	public String timeConversion(String time) throws ParseException;
	public List<Ride> sendNotification(int ownerId);
	public List<QueryRequest> getPendingQueries(int ownerId);
	public boolean replyToQuery(QueryResponse queryResponse);
	public List<UserBookings> getPendingBookings(int ownerId);
	
	//these all are methods for userMicroService interaction
	public List<Vehicle> getAvailableVehicles(String type);
	public List<Ride> getRidesSortByPrice();
	public boolean checkVehicle(BookingRequest bookingRequest);
	public List<UserBookings> getAllMyBookings(int user_id);
	public UserBookings getConfirmedBookings(int userId,int bookingId);
	public boolean cancelVehicle(int booking_id,int id);
	public boolean updateRideStatus(String status,int rideId);
	public Vehicle getVehicle(String vehicleId);
	public void saveQueryResponse(QueryResponse queryResponse);
	public QueryResponse getQueryResponse(int queryId);
	



}
