package com.hcl.ownermicroservice.serviceimpl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hcl.ownermicroservice.dto.BookingRequest;
import com.hcl.ownermicroservice.dto.QueryRequest;
import com.hcl.ownermicroservice.entities.Owner;
import com.hcl.ownermicroservice.entities.QueryResponse;
import com.hcl.ownermicroservice.entities.Ride;
import com.hcl.ownermicroservice.entities.UserBookings;
import com.hcl.ownermicroservice.entities.Vehicle;
import com.hcl.ownermicroservice.repository.OwnerRepository;
import com.hcl.ownermicroservice.repository.QueryResponseRepository;
import com.hcl.ownermicroservice.repository.RideRepository;
import com.hcl.ownermicroservice.repository.UserBookingsRepository;
import com.hcl.ownermicroservice.repository.VehicleRepository;
import com.hcl.ownermicroservice.service.OwnerService;
@Service
public class OwnerServiceImpl implements OwnerService{	
	@Autowired
	private OwnerRepository ownerRepository;
	
	@Autowired 
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private RideRepository rideRepository;
	
	@Autowired
	private UserBookingsRepository userBookingsRepository;
	
	@Autowired
	private QueryResponseRepository queryResponseRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private static final String USER_URL="http://user-ms/user";
	
	
	
	private  Vehicle vehicle;
	private  Ride ride;
	
	@Override
	public List<Owner> getAllOwners() {
		//to get all the owners
		return ownerRepository.findAll();
	}
	
	@Override
	public Owner ownerRegister(Owner owner) {
		//to register the owner
		return ownerRepository.save(owner);
		
	}

	@Override
	public boolean getOwnerByName(String ownerName) {
		// TO check and get the owner exist or not by id
		boolean flag=true;
		Owner owner = ownerRepository.findByName(ownerName);
		if (owner == null) {
			// if owner not exist
			flag = false;
		}
		return flag;	
	}
	
	@Override
	public Vehicle insertVehicle(Vehicle vehicle) {
		// TO insert the vehicle into database
		return  vehicleRepository.save(vehicle);
		
	}
	@Override
	public boolean getVehicleById(String vehicleId) {
		// TO get or check vehicle exist or not
		boolean flag=true;

		// to get the vehicle by id
		Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
		if (vehicle == null) {
			// if vehicle not exist
			flag = false;
		}			
		return flag;
		
	}

	@Override
	public List<Vehicle> getAllMyVehicles(int ownerId) {
		// TO get all the vehicles
		return vehicleRepository.findVehiclesByOwnerId(ownerId);
	}

	@Override
	public boolean deleteVehicle(String vehicleId,int ownerId) {
		// To delete the the vehicle by id 
		 vehicle = vehicleRepository.findById(vehicleId).orElse(null);
		 if(vehicle!=null)
		 {
			 Owner owner = vehicle.getOwner();
			 //to check ownerId and vehicle ownerId same or not
			 if(owner!=null && ownerId==owner.getOwnerId())
			 {
				 vehicleRepository.deleteById(vehicle.getVehicleId());;
				 return true;
			 }
		 }
		return false;	
	}
	@Override
	public boolean updateVehicle(Vehicle vehicle,int ownerId) {
		// TO update the vehicle by id
		Vehicle vehicle2=vehicleRepository.findOwnerVehicles(vehicle.getVehicleId(),ownerId);
		
		if(vehicle2!=null)
		{
			vehicle.setVehicleId(vehicle2.getVehicleId());
			vehicleRepository.save(vehicle);
			return true;
		}
		return false;	
	}
	
	@Override
	public String insertRide(Ride ride){
		//it will check vehicle exist or not
		boolean res = getVehicleById(ride.getVehicleId());
	
		if (res == true) {
			Vehicle vehicle = vehicleRepository.findOwnerVehicles(ride.getVehicleId(), ride.getOwnerId());
			// to check whether vehicle is owned by logged in owner or not
			if (vehicle != null && vehicle.getVehicleId().equals(ride.getVehicleId())
					&& vehicle.getOwner().getOwnerId() == ride.getOwnerId()) {
				// to check date and time are correct or not
				String strDate = DateConversion(ride.getRideDate());
				String strtime = timeConversion(ride.getRideTime());

				if (strDate != null) {
					return strDate;
				} else if (strtime != null) {
					return strtime;
				} else {
					// 2024-12-31 24:59:59
					int year = Integer.parseInt(ride.getRideDate().substring(0, 4));
					int month = Integer.parseInt(ride.getRideDate().substring(5, 7));
					int date = Integer.parseInt(ride.getRideDate().substring(8, 10));
					int hour = Integer.parseInt(ride.getRideTime().substring(0, 2));
					int min = Integer.parseInt(ride.getRideTime().substring(3, 5));
					int sec = Integer.parseInt(ride.getRideTime().substring(6, 8));
					// to check date is correct or not
					if ((year >= 2024 && year <= 3000) && (month >= 1 && month <= 12) && (date >= 1 && date <= 31)) {
						// to check time is correct or not
						if ((hour >= 1 && hour <= 24) && (min >= 0 && min <= 59) && (sec >= 0 && sec <= 59)) {
							// if all details are correct ride will be added
							rideRepository.save(ride);
							return null;
						}

						else {
							// if time is not correct
							return "Enter correct Time!!!";
						}

					} else {
						// if date is not correct
						return "Enter Correct Date!!!";
					}
				}
			} else {
				// if vehicle is not owned by logged in owner
				return "vehicle id " + ride.getVehicleId() + " is not owned by you so u can't add ride for it!!";
			}
		} else {
			// if vehicle is not exist ride can't be added
			return "Vehicle Id " + ride.getVehicleId() + " not exist so can't add ride for that vehicle!!";
		}
			
	}
	

	@Override
	public boolean getRideById(int rideId) {
		// TO get or check ride exist or not
		boolean flag=true;
		Optional<Ride> ride=rideRepository.findById(rideId);
		// if ride not exist
		if (ride.isEmpty()) {
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean deleteRide(int rideId,int ownerId) {
		// TO delete ride from owner
		ride=rideRepository.findById(rideId).orElse(null);
		//to check whether ride is created by logged in owner or not
		if(ride.getOwnerId()==ownerId)
		{
			//if ride is created by already logged in owner
			rideRepository.deleteById(rideId);
			return true;
		}
		//if ride is not created by already logged in owner
		return false;
		
	}

	@Override
	public List<Ride> getAllMyRides(int ownerId) {
		// TO get all the rides
		return rideRepository.findRidesByOwnerId(ownerId);
	}
	
	
	
	@Override
	public String  DateConversion(String date) 
	{
		String  msg=null;
		//defining the format of string representation
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			//parsing the string to date
			dateFormat.parse(date);
		}
		catch(ParseException e)
		{
			msg="Date format is not correct check format should be=yyyy-MM-dd";
			return msg;
		}
		return msg;
	}
	
	@Override
	public String timeConversion(String time) 
	{
		String  msg=null;
		//defining the format of string representation
		SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm:ss");
		try
		{
			//parse the string to java.util.Date
			timeFormat.parse(time);
		}
		catch(ParseException e)
		{
			msg="Time format is not correct check format should be=HH:mm:ss";
			return msg;
		}
		return msg;
	}

	@Override
	public List<Ride> getVehicleRidesByOwnerId(String vehicleId, int ownerId) {
		// TO get vehicle rides bases on ownerid and vehidid
		return rideRepository.findVehicleRidesByOwnerId(vehicleId, ownerId);
		 
	}
	@Override
	public List<UserBookings> getPendingBookings(int ownerId) {
		List<UserBookings> list=userBookingsRepository.findPendings("pending");
		List<UserBookings> list2=new ArrayList<>();
		if(list!=null)
		{
			for(UserBookings userBookings:list)
			{
				String vehicleId=userBookings.getVehicleId();
				Vehicle vehicle=getVehicle(vehicleId);
				if(vehicle!=null)
				{
					if(vehicle.getOwner().getOwnerId()==ownerId)
					{
						list2.add(userBookings);
					}
				}
				
			}
			return list2;
		}
		else
		{
			return null;
		}
	}	
	@Override
	public List<String> confirmBooking(int ownerId,int userId) {
		// To confirm the user booking if there are any pending bookings
		List<String> resultList=new ArrayList<>();
		List<UserBookings> list = userBookingsRepository.findPendingBookings("pending", userId);
		if(list!=null)
		{
			for(UserBookings userBooking:list)
			{
				int rideId =userBooking.getRideId();
				ride=rideRepository.findById(rideId).orElse(null);
				if(userBooking!=null && ride.getOwnerId()==ownerId)
				{
					userBooking.setStatus("successfull");
					userBookingsRepository.save(userBooking);
					ride.setStatus("booked");
					ride.setRideId(rideId);
					rideRepository.save(ride);
					resultList.add("Booking Confirmed for ride Id: " + rideId);
				} 
				else {
					resultList.add("You can't confirm the booking because this ride is not created By You !!!");
				}
			}
		}
		return resultList;
	}

	@Override
	public List<Ride> sendNotification(int ownerId) {
		// TO send the notification of his vehicles once it is booked
		List<Ride> ridesList = rideRepository.findOwnerBookedRides(ownerId, "booked");
		return ridesList;
	}

	@Override
	public List<QueryRequest> getPendingQueries(int ownerId) {
		// TO get the queries what are pending
		//to get the pending queries details from userMicroservice queryRequest Repository
		String url=USER_URL+"/getPendingQueries/{ownerId}";
		List<QueryRequest> list=restTemplate.getForObject(url, List.class,ownerId);
		return list;

	}

	@Override
	public boolean replyToQuery(QueryResponse queryResponse) {
		//to save the query answer
		//to update the status of queryRequest into userMicroService queryRequest Repository
		String url=USER_URL+"/replyToQuery";
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<QueryResponse> requestEntity=new HttpEntity<>(queryResponse,headers);
		return restTemplate.postForObject(url,requestEntity,Boolean.class);
	}
	
	
	
	//these all are methods for userMicroService interaction
	@Override
	public List<Vehicle> getAvailableVehicles(String type) {
		// TO get available vehicles based on type
		return vehicleRepository.findVehiclesByType("available", type);
		
	}
	
	@Override
	public List<Ride> getRidesSortByPrice() {
		// TO get the rides by sorting the price
		List<Ride> list=rideRepository.findAll(Sort.by(Sort.Direction.
				fromString("asc"), "price"));
		return list;
	}
	
	@Override
	public boolean checkVehicle(BookingRequest bookingRequest) {
		// TO check the vehicle available or not for booking of user
		//to get rideId of available vehicle
		Integer rideId=rideRepository.checkVehicle(bookingRequest.getBookingDate(),bookingRequest.getBookingTime()
				,bookingRequest.getPickupLocation(),
				bookingRequest.getDropLocation(), "available");
		
		//to get VehicleId of available vehicle
		String vehicleId=rideRepository.checkVehicleId(bookingRequest.getBookingDate(),bookingRequest.getBookingTime()
				,bookingRequest.getPickupLocation(),
				bookingRequest.getDropLocation(), "available");
		
		if(rideId!=null && vehicleId!=null)
		{
			//if vehicle is available to save the details of user's ride into userBookings
			UserBookings userBookings=new UserBookings();
			userBookings.setUserId(bookingRequest.getUserId());
			userBookings.setBookingDate(bookingRequest.getBookingDate());
			userBookings.setBookingTime(bookingRequest.getBookingTime());
			userBookings.setDropLocation(bookingRequest.getDropLocation());
			userBookings.setPickupLocation(bookingRequest.getPickupLocation());	
			userBookings.setRideId(rideId);
			userBookings.setVehicleId(vehicleId);
			userBookings.setStatus("pending");
			userBookingsRepository.save(userBookings);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public List<UserBookings> getAllMyBookings(int userId) {
		// TO get all my bookings
		return userBookingsRepository.findAllBookings(userId);	
	}
	
	@Override
	public UserBookings getConfirmedBookings(int userId,int bookingId) {
		// TO get confirmed bookings from the database
		return userBookingsRepository.findConfirmedBookings("successfull", userId,bookingId);
		
	}
	
	@Override
	public boolean cancelVehicle(int bookingId,int userId) {
		// TO cancel the booked vehicle
		boolean result=false;
		UserBookings userBookings= userBookingsRepository.findBookingByUserId(userId, bookingId);
		if(userBookings==null)
		{
			result= false;
		}
		else
		{
			//to update the status as available of the ride after cancelling
			int rideId=userBookings.getRideId();
			boolean flag=updateRideStatus("available",rideId);
			if(flag==true)
			{
				result= true;
			}
			else
			{
				result= false;
			}
			userBookingsRepository.deleteById(bookingId);
			result= true;
		}
		return result;
	}
	
	@Override
	public boolean updateRideStatus(String status, int rideId) {
		// TO update the status as booked
		
		ride=rideRepository.findById(rideId).orElse(null);
		if(ride!=null)
		{
			ride.setRideId(rideId);
			ride.setStatus(status);
			rideRepository.save(ride);
			return true;
		}
		return false;
		
		
	}

	@Override
	public Vehicle getVehicle(String vehicleId) {
		//to get vehicle by id
		return vehicleRepository.findById(vehicleId).orElse(null);
		 
	}

	@Override
	public void saveQueryResponse(QueryResponse queryResponse) {
		//to save the response of queryResponse into QueryResponseRepository
		queryResponseRepository.save(queryResponse);
		
	}

	@Override
	public QueryResponse getQueryResponse(int queryId) {
		//to get Query Response by id
		return queryResponseRepository.findById(queryId).orElse(null);

	}

	

	

}
