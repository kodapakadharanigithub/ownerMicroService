package com.hcl.ownermicroservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hcl.ownermicroservice.controller.OwnerController;
import com.hcl.ownermicroservice.dto.VehicleRequest;
import com.hcl.ownermicroservice.entities.QueryResponse;
import com.hcl.ownermicroservice.entities.Ride;
import com.hcl.ownermicroservice.entities.UserBookings;
import com.hcl.ownermicroservice.entities.Vehicle;
import com.hcl.ownermicroservice.exceptions.NoAvailableVehiclesException;
import com.hcl.ownermicroservice.exceptions.NoRidesAvailableException;
import com.hcl.ownermicroservice.exceptions.RideAlreadyExistOrNotException;
import com.hcl.ownermicroservice.exceptions.VehicleAlreadyExistOrNotException;
import com.hcl.ownermicroservice.service.OwnerService;

@SpringBootTest
public class ControllerTest {
	
	@InjectMocks
	private OwnerController ownerController;
	@Mock
	private OwnerService ownerService;
	
	
	 @Test
	 public void testGetAllMyRidesWithRidesAvailable() throws NoRidesAvailableException {
	        List<Ride> ridesList = new ArrayList<>();
	        ridesList.add(new Ride());
	        when(ownerService.getAllMyRides(anyInt())).thenReturn(ridesList);
	        ResponseEntity<?> responseEntity = ownerController.getAllMyRides();
	        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	        assertEquals(ridesList, responseEntity.getBody());
	  }
	 
	 @Test
	 public void testGetAllMyRidesWithNoRidesAvailable() throws NoRidesAvailableException {
	        int ownerId = 123; 
	        when(ownerService.getAllMyRides(ownerId)).thenReturn(new ArrayList<>());
	        assertThrows(NoRidesAvailableException.class, () -> {
	        	ownerController.getAllMyRides();
	        });
	 }
	 
	 @Test
	 public void testGetAllMyVehiclesWithVAvailable() throws NoAvailableVehiclesException {
	        List<Vehicle> vehiclesList = new ArrayList<>();
	        vehiclesList.add(new Vehicle());
	        when(ownerService.getAllMyVehicles(anyInt())).thenReturn(vehiclesList);
	        ResponseEntity<?> responseEntity = ownerController.getAllMyVehicles();
	        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	  }
	 
	 @Test
	 public void testGetAllMyVehiclesWithNoVehiclesAvailable() throws NoRidesAvailableException {
	        int ownerId = 123; 
	        when(ownerService.getAllMyVehicles(ownerId)).thenReturn(new ArrayList<>());
	        assertThrows(NoAvailableVehiclesException.class, () -> {
	        	ownerController.getAllMyVehicles();
	        });
	 }
	 
	 
	 @Test
	 public void testGetPendingQueriesNoPendingQueries() {
	        int ownerId=90;
	        when(ownerService.getPendingQueries(ownerId)).thenReturn(new ArrayList<>());
	        ResponseEntity<?> response = ownerController.getPendingQueries();
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	 }
	 
	 @Test
	 public void testGetRidesBasedOnPrice() {
	        List<Ride> ridesList = new ArrayList<>();
	        ridesList.add(new Ride());
	        when(ownerService.getRidesSortByPrice()).thenReturn(ridesList);
	        List<Ride> list = ownerController.getRidesSortBasedOnPrice();
	        assertEquals(ridesList, list);
	  }
	 
	 @Test
	 public void testGetAllMyBookings() {
		 	int userId=9;
	        List<UserBookings> userBookingsList = new ArrayList<>();
	        userBookingsList.add(new UserBookings());
	        when(ownerService.getAllMyBookings(userId)).thenReturn(userBookingsList);
	        List<UserBookings> list = ownerController.getAllMyBookings(userId);
	        assertEquals(userBookingsList, list);
	  }
	 
	 @Test
	 public void testGetPendingBookings() {
	        ResponseEntity<?> responseEntity = ownerController.getPendingBookings();
	        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	  }
	 
	 @Test
	 public void testGetConfirmedBookings() {
		 	int userId=9;
		 	int bookingId=9;
	        UserBookings userBookings = new UserBookings();
	        when(ownerService.getConfirmedBookings(userId,bookingId)).thenReturn(userBookings);
	        UserBookings actualUserBookings = ownerController.getConfirmedBookings(userId,bookingId);
	        assertEquals(userBookings, actualUserBookings);
	  }
	 
	 @Test
	 public void testConfirmBooking() {
		 	int userId=9;
	        ResponseEntity<?> responseEntity = ownerController.confirmBooking(userId);
	        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	  }
	 
	 @Test
	 public void testReplyToQuery()
	 {
		   QueryResponse queryResponse=new QueryResponse();
	       ResponseEntity<?> responseEntity = ownerController.replyToQuery(queryResponse);
	       assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	 }
	 
	 @Test
	 public void testGetNotificationBooking()
	 {
		 ResponseEntity<?> responseEntity = ownerController.getNotificationBooking();
	     assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	 }
	 
	 @Test
	 public void testDeleteVehicle() throws VehicleAlreadyExistOrNotException {
	       String vehicleId="1000";
	        assertThrows(VehicleAlreadyExistOrNotException.class, () -> {
	        	ownerController.deleteVehicle(vehicleId);
	        });
	 }
	 
	 @Test
	 public void testCancelRide() throws RideAlreadyExistOrNotException
	 {
		 int rideId=90;
		 assertThrows(RideAlreadyExistOrNotException.class, () -> {
	        	ownerController.cancelRide(rideId);
	        });
	 }
	 
	 @Test
	 public void testGetQueryResponse()
	 {
		 int queryId=10;
		 QueryResponse queryResponse = new QueryResponse();
		 when(ownerService.getQueryResponse(queryId)).thenReturn(queryResponse);
		 QueryResponse queryResponse2 = ownerController.getQueryResponse(queryId);
		 assertEquals(queryResponse,queryResponse2);
	 }
	
	 @Test
	 public void testGetVehicle()
	 {
		 String vehicleId="1000";
		 Vehicle vehicle=new Vehicle();
		 when(ownerService.getVehicle(vehicleId)).thenReturn(vehicle);
		 Vehicle vehicle2=ownerController.getVehicle(vehicleId);
		 assertEquals(vehicle,vehicle2);
	 }
	 
	 @Test
	 public void testCancelVehicle()
	 {
		 int bookingId=10;
		 int userId=10;
		 boolean flag=false;
		 when(ownerService.cancelVehicle(bookingId,userId)).thenReturn(flag);
		 boolean flag2=ownerController.cancelVehicle(bookingId, userId);
		 assertEquals(flag,flag2);

	 }
	 
	 
	 
	 
 
}
