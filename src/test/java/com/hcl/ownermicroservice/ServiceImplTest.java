package com.hcl.ownermicroservice;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.hcl.ownermicroservice.dto.BookingRequest;
import com.hcl.ownermicroservice.entities.Ride;
import com.hcl.ownermicroservice.entities.UserBookings;
import com.hcl.ownermicroservice.entities.Vehicle;
import com.hcl.ownermicroservice.repository.QueryResponseRepository;
import com.hcl.ownermicroservice.repository.RideRepository;
import com.hcl.ownermicroservice.repository.UserBookingsRepository;
import com.hcl.ownermicroservice.repository.VehicleRepository;
import com.hcl.ownermicroservice.serviceimpl.OwnerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ServiceImplTest {
	
	@Mock
	private VehicleRepository vehicleRepository;
	
	@Mock
	private RideRepository rideRepository;
	
	@InjectMocks
	private OwnerServiceImpl ownerService;
	
	@Mock
	private UserBookingsRepository userBookingsRepository;
	
	@Mock
	private QueryResponseRepository queryResponseRepository;
	
	@Test
    public void testAddVehicle() {
        Vehicle vehicle = new Vehicle(/* vehicle details */);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        Vehicle addedVehicle = ownerService.insertVehicle(vehicle);
        assertNotNull(addedVehicle);
        assertEquals(vehicle, addedVehicle);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }
	@Test
    public void testGetVehicleById() {
        String vehicleId="90";
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(new Vehicle()));
        boolean flag=ownerService.getVehicleById(vehicleId);
        assertEquals(true, flag);
    }

	@Test
    public void testDeleteVehicle() {
        String vehicleId="90";
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(new Vehicle()));
 
       boolean flag=ownerService.deleteVehicle(vehicleId, 1);
 
        assertEquals(false, flag);
    }
	
	@Test
    public void testUpdateVehicle() {
		Vehicle updatedVehicle = new Vehicle(/* updated vehicle details */);
        boolean flag = ownerService.updateVehicle(updatedVehicle, 1);
        assertEquals(false, flag);
 
    }
	
	@Test
    public void testGetAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        list.add(new Vehicle(/* vehicle details */));
        when(vehicleRepository.findVehiclesByOwnerId(1)).thenReturn(list);
 
        List<Vehicle> retrievedVehicles = ownerService.getAllMyVehicles(1);
 
        assertNotNull(retrievedVehicles);
        assertEquals(list.size(), retrievedVehicles.size());
    }
	
	@Test
    public void testAddRide() {
        Ride ride = new Ride(/* ride details */);
        String result=ownerService.insertRide(ride);
        assertNotNull(result);
        assertEquals("Vehicle Id " + ride.getVehicleId() + " not exist so can't add ride for that vehicle!!", result);
    }
	
	@Test
    public void testGetRideById() {
        int rideId=4;
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(new Ride()));
        boolean flag=ownerService.getRideById(rideId);
        assertEquals(true, flag);
    }
	
	@Test
    public void testDeleteRide() {
		int rideId=4;
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(new Ride())); 
        boolean flag=ownerService.deleteRide(4, 2);
        assertEquals(false, flag);
    }	
	@Test
    public void testGetAllRides() {
        List<Ride> list = new ArrayList<>();
        list.add(new Ride(/* ride details */));
        when(rideRepository.findRidesByOwnerId(2)).thenReturn(list);
        List<Ride> retrievedRides = ownerService.getAllMyRides(2);
        assertNotNull(retrievedRides);
        assertEquals(list.size(), retrievedRides.size());
    }
	
	@Test
    public void testGetAvailableVehicles() {
        List<Vehicle> list = new ArrayList<>();
        String type="2-wheeler";
        list.add(new Vehicle(/* vehicle details */));
        when(vehicleRepository.findVehiclesByType("available",type)).thenReturn(list);
 
        List<Vehicle> retrievedVehicles = ownerService.getAvailableVehicles(type);
 
        assertNotNull(retrievedVehicles);
        assertEquals(list.size(), retrievedVehicles.size());
    }
	
	@Test
    public void testRidesSortByPrice() {
        List<Ride> list = new ArrayList<>();
        list.add(new Ride(/* ride details */));
        when(rideRepository.findAll(Sort.by(Sort.Direction.fromString("asc"), "price"))).thenReturn(list);
        List<Ride> retrievedRides = ownerService.getRidesSortByPrice();
        assertNotNull(retrievedRides);
        assertEquals(list.size(), retrievedRides.size());
    }
	
	@Test
    public void testCheckVehicle() {
		BookingRequest bookingRequest=new BookingRequest();
		boolean flag = ownerService.checkVehicle(bookingRequest);
        assertEquals(false, flag);
    }
	
	@Test
    public void testGetAllMyBookings() {
		List<UserBookings> list = new ArrayList<>();
        int userId=1;
        list.add(new UserBookings());
        when(userBookingsRepository.findAllBookings(userId)).thenReturn(list);
        List<UserBookings> retrievedBookings = ownerService.getAllMyBookings(userId);
        assertNotNull(retrievedBookings);
        assertEquals(list.size(), retrievedBookings.size());
    }
	
	@Test
    public void testGetConfirmedBookings() {
		UserBookings userBookings=new UserBookings();
        int userId=1;
        int bookingId=1;
        when(userBookingsRepository.findConfirmedBookings("successfull", userId,bookingId)).thenReturn(userBookings);
        UserBookings retrievedBooking = ownerService.getConfirmedBookings(userId,bookingId);
        assertNotNull(retrievedBooking);
        assertEquals(userBookings, retrievedBooking);
    }
	
	@Test
    public void testCancelVehicle() {
		UserBookings userBookings=new UserBookings();
        int userId=1;
        int bookingId=1;
        when(userBookingsRepository.findBookingByUserId(userId,bookingId)).thenReturn(userBookings);
        boolean flag = ownerService.cancelVehicle(userId,bookingId);
        assertEquals(true, flag);
    }
	
	@Test
    public void testGetVehicle() {
		Vehicle vehicle=null;
		Optional<Vehicle> opVehicle=Optional.ofNullable(vehicle);
        String vehicleId="901";
        when(vehicleRepository.findById(vehicleId)).thenReturn(opVehicle);
        Vehicle retrievedVehicle=ownerService.getVehicle(vehicleId);
        assertEquals(vehicle, retrievedVehicle);
    }
	
}
