package com.hcl.ownermicroservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


import com.hcl.ownermicroservice.controller.OwnerController;
import com.hcl.ownermicroservice.entities.Ride;
import com.hcl.ownermicroservice.entities.Vehicle;

import com.hcl.ownermicroservice.service.OwnerService;

@SpringBootTest
class OwnerMicroServiceApplicationTests {

	@InjectMocks
	private OwnerController ownerController;
	@Mock
	private OwnerService ownerService;
	
	
	
	@Test
	public void testGetVehicleById()
	{
		String vehicleId="101";
        Vehicle vehicle = new Vehicle();
        boolean expected=false;
        vehicle.setVehicleId(vehicleId);
        when(ownerService.getVehicleById(vehicleId)).thenReturn(expected);
        boolean actual = ownerService.getVehicleById(vehicleId);
        assertEquals(expected,actual);
       
	}
	@Test
	public void testGetRideById()
	{
		int rideId=111;
        Ride ride = new Ride();
        boolean expected=false;
        ride.setRideId(rideId);
        when(ownerService.getRideById(rideId)).thenReturn(expected);
        boolean actual = ownerService.getRideById(rideId);
        assertEquals(expected,actual);   
	}
	
	@Test
    public void testDeleteVehicle() {
        String vehicleid="105";
        int ownerId=1;
        boolean expected=false;
        when(ownerService.deleteVehicle(vehicleid,ownerId)).thenReturn(expected);
        boolean actual=ownerService.deleteVehicle(vehicleid,ownerId);
        assertEquals(expected,actual);   
    }	
	
	@Test
	public void testOwnerByName()
	{
		  String ownerName="owner1";
		  boolean expected=false;
	      when(ownerService.getOwnerByName(ownerName)).thenReturn(expected);
	      boolean actual = ownerService.getOwnerByName(ownerName);
	      assertEquals(expected,actual);  
	}
	
	
}
