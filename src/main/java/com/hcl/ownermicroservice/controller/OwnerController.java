package com.hcl.ownermicroservice.controller;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.ownermicroservice.dto.BookingRequest;
import com.hcl.ownermicroservice.dto.LoginRequest;
import com.hcl.ownermicroservice.dto.QueryRequest;
import com.hcl.ownermicroservice.dto.VehicleRequest;
import com.hcl.ownermicroservice.entities.Owner;
import com.hcl.ownermicroservice.entities.QueryResponse;
import com.hcl.ownermicroservice.entities.Ride;
import com.hcl.ownermicroservice.entities.UserBookings;
import com.hcl.ownermicroservice.entities.Vehicle;
import com.hcl.ownermicroservice.exceptions.NoAvailableVehiclesException;
import com.hcl.ownermicroservice.exceptions.NoRidesAvailableException;
import com.hcl.ownermicroservice.exceptions.RideAlreadyExistOrNotException;
import com.hcl.ownermicroservice.exceptions.UserAlreadyExistOrNotException;
import com.hcl.ownermicroservice.exceptions.VehicleAlreadyExistOrNotException;
import com.hcl.ownermicroservice.service.OwnerService;
import com.hcl.ownermicroservice.util.JwtUtil;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/owner")
public class OwnerController {
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private OwnerService ownerService;

	private  int id;
	private  String userName;
	private  String passWord;
	private  Owner owner;
	
	Logger logger=LoggerFactory.getLogger(OwnerController.class);
	
	@Value("${owner.oName}")
	private String oName;

	
	private int oId;
	@Value("${owner.oPass}")
	private String oPass;
 
	@GetMapping("/addOwnerThroughCloud")
	public ResponseEntity<?> getData() throws UserAlreadyExistOrNotException{
		owner =new Owner(oId,oName,oPass);
		boolean flag=ownerService.getOwnerByName(oName);
		if(flag==false)
		{
			ownerService.ownerRegister(owner);
			return new ResponseEntity<>("owner added through cloud",HttpStatus.OK);
		}
		else
		{
			throw new UserAlreadyExistOrNotException("owner can't added through cloud because Already exist");
		}
	}
	@PostMapping("/login")
	public String ownerLogin(@RequestBody LoginRequest loginRequest )
	{
		//to get the all the owners
		List<Owner> list = ownerService.getAllOwners();
		boolean flag=false;
		if(list==null)
		{
			logger.info("OOPS!!There are no owners");
			return "OOPS!!There are no owners";
		}
		else
		{
			for(Owner o:list)
			{
				//to check whether owner is valid or not 
				if(loginRequest.getUserName().equals(o.getOwnerName()) && loginRequest.getPassWord().equals(o.getPassWord()))
				{	
					flag=true;
					id=o.getOwnerId();
					userName=o.getOwnerName();
					passWord=o.getPassWord();
					break;
				}
				else
				{
					flag=false;	
				}
			}
		}
		if(flag==true)
		{
			//if owner credentials are valid token generated for the owner on their username
			String token=jwtUtil.generateToken(loginRequest.getUserName());
			logger.info(userName+" Logged in successfully");
			return token;
		}
		else
		{
			//if owner credentials are Invalid
			logger.error("Bad/Invalid Credentials Check And Try To Login Again");
			return "Bad/Invalid Credentials Check And Try To Login Again";
		}	
	}
	@PostMapping("/register")
	public ResponseEntity<?> ownerRegister(@Valid @RequestBody Owner owner) throws UserAlreadyExistOrNotException
	{
		//it will check whether owner exist or not	
		boolean flag = ownerService.getOwnerByName(owner.getOwnerName());
		if (flag == false) {
			// if owner not exist owner will be registered
			ownerService.ownerRegister(owner);
			logger.info(owner.getOwnerName()+" Registered Successfully");
			return new ResponseEntity<>("Registered Successfully", HttpStatus.CREATED);
		} 
		else 
		{
			//if owner already exist
			logger.info(owner.getOwnerName()+" Already Exist So u Can't register again");
			throw new UserAlreadyExistOrNotException(owner.getOwnerName()+" Already Exist So u Can't register again");
		}
	}
	
	@PostMapping("/addVehicle")
	public ResponseEntity<?> addVehicle(@RequestBody VehicleRequest vehicleRequest) throws VehicleAlreadyExistOrNotException		
	{
		//to check whether vehicle type is correct or not
		if(vehicleRequest.getType().equalsIgnoreCase("2-wheeler") || vehicleRequest.getType().equalsIgnoreCase("4-wheeler"))
		{
			//it will check vehicle exist or not
			boolean flag=ownerService.getVehicleById(vehicleRequest.getVehicleId());
			Vehicle vehicle=new Vehicle();
			if(flag==false)
			{
					//if vehicle not exist then vehicle will be added
					vehicle.setVehicleId(vehicleRequest.getVehicleId());
					vehicle.setModel(vehicleRequest.getModel());
					vehicle.setType(vehicleRequest.getType());
					owner=new Owner();
					owner.setOwnerId(id);
					owner.setOwnerName(userName);
					owner.setPassWord(passWord);
					vehicle.setOwner(owner);
					ownerService.insertVehicle(vehicle);
					logger.info("Vehicle Added Successfully");
					return new ResponseEntity<>("Vehicle Added Successfully",HttpStatus.CREATED);
			}
			
			else
			{
				//if vehicle existed it will not add and it will throw exception
				logger.info(vehicleRequest.getVehicleId()+" Already Exist U Can't add again");
				throw new VehicleAlreadyExistOrNotException(vehicleRequest.getVehicleId()+" Already Exist U Can't add again");

			}
		}
		else
		{
			//if owner enters invalid vehicle type
			logger.info("Vehicle type should be 2-wheeler/4-wheeler");
			return new ResponseEntity<>("Vehicle type should be 2-wheeler/4-wheeler",HttpStatus.BAD_REQUEST);
		}
		
	}
	@GetMapping("/getAllMyVehicles")
	public ResponseEntity<?> getAllMyVehicles() throws NoAvailableVehiclesException
	{
		//to get the list owner vehicle based on id
		List<Vehicle> vehiclesList = ownerService.getAllMyVehicles(id);
		List<String> resultList=new ArrayList<>();
		for(Vehicle v:vehiclesList)
		{
			resultList.add("VehicleId:"+v.getVehicleId()+" Model:"+v.getModel()+" Type:"+v.getType());
		}
		
		if(!vehiclesList.isEmpty())
		{
			//if there are any vehicles it will display
			return new ResponseEntity<>(resultList,HttpStatus.OK);
		}
		else
		{
			//if there are no vehicles it throws exception
			logger.error("No vehicles on owner id: "+id);
			throw new NoAvailableVehiclesException("No vehicles on owner id: "+id);
		}
	}
	@DeleteMapping("/deleteVehicle/{vehicleId}")
	public ResponseEntity<?> deleteVehicle(@PathVariable String vehicleId) throws VehicleAlreadyExistOrNotException
	{
		//to check whether vehicle id exist or not
		boolean flag=ownerService.getVehicleById(vehicleId);
		if(flag==false)
		{
			//if vehicle not exist and it throws exception
			logger.error(vehicleId+" not Exist So can't Delete");
			throw new VehicleAlreadyExistOrNotException(vehicleId+" not Exist So can't Delete");
		}
		else
		{
			//if vehicle exist
			boolean res=ownerService.deleteVehicle(vehicleId,id);
			if(res==true)
			{
				List<Ride> ridesList=ownerService.getVehicleRidesByOwnerId(vehicleId, id);
				for(Ride r:ridesList)
				{
					ownerService.deleteRide(r.getRideId(), id);
				}
				logger.info("Vehicle Deleted Successfully");
				return new ResponseEntity<>("Vehicle Deleted Successfully",HttpStatus.OK);
			}
			else
			{
				logger.info("You can't delete this Vehicle Because it is not created By You!!");
				return new ResponseEntity<>("You can't delete this Vehicle Because it is not created By You!!",HttpStatus.BAD_REQUEST);
			}
		}	
	}
	
	@PutMapping("/updateVehicle")
	public ResponseEntity<?> updateVehicle(@RequestBody VehicleRequest vehicleRequest) throws VehicleAlreadyExistOrNotException
	{
		//to check owner enter correct vehicle type or not
		if(vehicleRequest.getType().equalsIgnoreCase("2-wheeler") || vehicleRequest.getType().equalsIgnoreCase("4-wheeler"))
		{
			//to check whether vehicle exist or not
			boolean flag=ownerService.getVehicleById(vehicleRequest.getVehicleId());
			if(flag==false)
			{
				//if vehicle not exist it throws exception
				throw new VehicleAlreadyExistOrNotException(vehicleRequest.getVehicleId()+" doesn't Exist U Can't Update");
			}
			else
			{
				//if vehicle exist update his vehicle details only
				owner=new Owner();
				owner.setOwnerId(id);
				owner.setOwnerName(userName);
				owner.setPassWord(passWord);
				Vehicle vehicle=new Vehicle();
				vehicle.setOwner(owner);
				vehicle.setModel(vehicleRequest.getModel());
				vehicle.setType(vehicleRequest.getType());
				vehicle.setVehicleId(vehicleRequest.getVehicleId());
				boolean res=ownerService.updateVehicle(vehicle,id);
				if(res==true)
				{
					logger.info("Vehicle Updated Successfully");
					return new ResponseEntity<>("Vehicle Updated Successfully",HttpStatus.OK);
				}
				else
				{
					logger.error("You can't Update this Vehicle Because it is created By You!!");
					return new ResponseEntity<>("You can't Update this Vehicle Because it is created By You!!",HttpStatus.BAD_REQUEST);
				}
			}
		}
		else
		{
			//if owner enters invalid vehicle type
			logger.error("You should enter either 2-wheeler or 4-wheeler");
			return new ResponseEntity<>("You should enter either 2-wheeler or 4-wheeler",HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/getAllMyRides")
	public ResponseEntity<?> getAllMyRides() throws NoRidesAvailableException
	{
		//to get ride list based on owner id
		List<Ride> ridesList = ownerService.getAllMyRides(id);
		if(!ridesList.isEmpty())
		{
			//if there are rides
			return new ResponseEntity<>(ridesList,HttpStatus.OK);
		}
		else
		{
			//if there are no rides it throw an exception
			logger.error("No Rides on ownerId: "+id);
			throw new NoRidesAvailableException("No Rides on ownerId: "+id);
		}
	}
	@PostMapping("/addRide")
	public ResponseEntity<?> addRide(@RequestBody Ride ride) throws ParseException, RideAlreadyExistOrNotException
	{
		if(ride.getDropLocation().equalsIgnoreCase(ride.getPickUpLocation()))
		{
			logger.error("Pickup and Drop locations Should be different");
			return new ResponseEntity<>("Pickup and Drop locations Should be different",HttpStatus.BAD_REQUEST);
		}
		else
		{
			//to check ride exist or not
			boolean flag = ownerService.getRideById(ride.getRideId());
			
			if(flag==false)
			{	
				//if ride is not exist then ride will be inserted
				ride.setOwnerId(id);
				ride.setStatus("available");
				String result=ownerService.insertRide(ride);
				if(result==null)
				{
					logger.info("ride added successfully");
					return new ResponseEntity<>(ride,HttpStatus.CREATED);
				}
				else
				{
					logger.error(result);
					return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
				}
	
			}
			else
			{
				//if ride is already existed it throws exception
				logger.error(ride.getRideId()+" Already Exist u can't add again");
				throw new RideAlreadyExistOrNotException("Ride with rideId " +ride.getRideId()+" Already Exist u can't add again");
			}
		}
			
	}
	
	@DeleteMapping("/cancelRide/{rideId}")
	public ResponseEntity<?> cancelRide(@PathVariable int rideId) throws RideAlreadyExistOrNotException
	{
		//to check whether ride exist or not
		boolean flag=ownerService.getRideById(rideId);
		if(flag==false)
		{
			//id ride not exist then owner can't cancel and it throws exception
			logger.error(rideId+" not Exist So can't Cancel");
			throw new RideAlreadyExistOrNotException(rideId+" not Exist So can't Cancel");
		}
		else
		{
			//if ride exist  then owner can cancel
			boolean res=ownerService.deleteRide(rideId,id);
			if(res==true)
			{
				logger.info("Ride Cancelled Successfully");
				return new ResponseEntity<>("Ride Cancelled Successfully",HttpStatus.OK);
			}
			else
			{
				logger.error("You can't cancel this Ride  Because it is not created By You!!");
				return new ResponseEntity<>("You can't cancel this Ride  Because it is not created By You!!",HttpStatus.BAD_REQUEST);	
			}
		}	
	}
	
	@GetMapping("/getNotificationBooking")
	public ResponseEntity<?> getNotificationBooking()
	{
		//to get the notification once it is booked
		List<Ride> ridesList = ownerService.sendNotification(id);
		if(ridesList.isEmpty())
		{
			//if there no vehicles booked
			logger.info("No vehicle Is Booked!!");
			return new ResponseEntity<>("No vehicle Is Booked!!",HttpStatus.NOT_FOUND);
		}
		else
		{
			List<String> result=new ArrayList<>();
			for(Ride r:ridesList)
			{
				
				result.add("Ride is Booked for vehicleId "+r.getVehicleId() + "  and  for rideId  "+r.getRideId());
			}
			//it will display  vehicles list which are booked
			return new ResponseEntity<>(result,HttpStatus.OK);
		}
	}
	@GetMapping("/getPendingBookings")
	public ResponseEntity<?> getPendingBookings()
	{
		List<UserBookings> list=ownerService.getPendingBookings(id);
		if(list!=null && !list.isEmpty())
		{
			return new ResponseEntity<>(list,HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>("There are no bookings Pendings",HttpStatus.NOT_FOUND);
		}
}
	@PostMapping("/confirmBooking/{userId}")
	public ResponseEntity<?> confirmBooking(@PathVariable int userId)
	{
		//to confirm booking of user based on userId 
		List<String> resultList = ownerService.confirmBooking(id,userId);
		if(!resultList.isEmpty())
			return new ResponseEntity<>(resultList,HttpStatus.OK);
		else
			return new ResponseEntity<>("There are no bookings for confirmation",HttpStatus.NOT_FOUND);

		
	}
	
	@GetMapping("/getPendingQueries")
	public ResponseEntity<?> getPendingQueries()
	{
		//to get pending queries based on userid
		List<QueryRequest> list = ownerService.getPendingQueries(id);
		if(list!=null && !list.isEmpty())
		{
			//if their is any query pending it will display
			return new ResponseEntity<>(list,HttpStatus.OK);
		
		}
		else
		{
			//if there are no queries pending
			logger.info("There are no pending queries ");
			return new ResponseEntity<>("There are no pending queries for OwnerId: "+id,HttpStatus.NOT_FOUND);
		}
	}
	@PostMapping("/replyToQuery")
	public ResponseEntity<?> replyToQuery(@RequestBody QueryResponse queryResponse)
	{
		//to reply the query asked by user
		boolean flag=ownerService.replyToQuery(queryResponse);
		if(flag==true)
		{
			logger.info("Replied Successfully");
			return new ResponseEntity<>("Replied Successfully",HttpStatus.OK) ;
		}
		else
		{
			logger.error("You entered incorrect query details");
			return new ResponseEntity<>("You entered incorrect query details",HttpStatus.BAD_REQUEST) ;
		}
	}
	
	
	
	//these all are the urls used for userMicroService interaction with repositories
	@ApiIgnore
	@GetMapping("/getAvailableVehicles/{type}")
	public List<String> getAvailableVehicles(@PathVariable String type)
	{
			//to get all the available vehicles
			List<Vehicle> vehiclesList = ownerService.getAvailableVehicles(type);
			List<String> resultList=new ArrayList<>();
			for(Vehicle v:vehiclesList)
			{
				resultList.add("VehicleId:"+v.getVehicleId()+" Model:"+v.getModel()+" Type:"+v.getType());
			}
			return resultList;
						
	}
	
	@ApiIgnore
	@GetMapping("/getRidesSortBasedOnPrice")
	public List<Ride> getRidesSortBasedOnPrice()
	{
		//to get sorted ride details based on price
		List<Ride> ridesList=ownerService.getRidesSortByPrice();
		return ridesList ;	
	}
	
	@ApiIgnore
	@PostMapping("/checkVehicle")
	public boolean checkVehicle(@RequestBody BookingRequest bookingRequest)
	{
		return ownerService.checkVehicle(bookingRequest);
	}
	
	@ApiIgnore
	@GetMapping("/getAllMyBookings/{userId}")
	public List<UserBookings> getAllMyBookings(@PathVariable int userId)
	{
		List<UserBookings> list=ownerService.getAllMyBookings(userId);
		return list;
	}
	
	@ApiIgnore
	@GetMapping("/getConfirmedBookings/{userId}/{bookingId}")
	public UserBookings getConfirmedBookings(@PathVariable int userId,@PathVariable int bookingId)
	{
		UserBookings userBookings= ownerService.getConfirmedBookings(userId,bookingId);
		return userBookings;
	}

	@ApiIgnore
	@GetMapping("/cancelVehicle/{bookingId}/{userId}")
	public boolean cancelVehicle(@PathVariable int bookingId,@PathVariable int userId)
	{
		return ownerService.cancelVehicle(bookingId,userId);		
	}
	
	@ApiIgnore
	@GetMapping("/getVehicle/{vehicleId}")
	public Vehicle getVehicle(@PathVariable String vehicleId)
	{
		return ownerService.getVehicle(vehicleId);
	}
		
	@ApiIgnore
	@PostMapping("/saveQueryResponse")
	public void saveQueryResponse(@RequestBody QueryResponse queryResponse)
	{
		ownerService.saveQueryResponse(queryResponse);
	}	
	
	@ApiIgnore
	@GetMapping("/getQueryResponse/{queryId}")
	public QueryResponse getQueryResponse(@PathVariable int queryId)
	{
		return ownerService.getQueryResponse(queryId);
	}
}