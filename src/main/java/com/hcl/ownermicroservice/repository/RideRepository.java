package com.hcl.ownermicroservice.repository;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.hcl.ownermicroservice.entities.Ride;

@EnableJpaRepositories
@Repository
public interface RideRepository extends JpaRepository<Ride,Integer>{
	
	@Query(value="Select * from ride  where owner_id=?1",nativeQuery=true)
	public List<Ride> findRidesByOwnerId(int owner_id);
	
	@Query(value="Select * from ride  where vehicle_id=?1 and owner_id=?2",nativeQuery=true)
	public List<Ride> findVehicleRidesByOwnerId(String vehicle_id,int owner_id);
	
	public List<Ride> findAll(Sort sort);
	
	@Query(value="select ride_id from ride where ride_date=?1 and ride_time=?2 and pick_up_location=?3 and drop_location=?4 and status=?5",nativeQuery=true)
	public Integer checkVehicle(String booking_date,String booking_time,String pick_up_location,
			String drop_Location,String status);
	@Query(value="select vehicle_id from ride where ride_date=?1 and ride_time=?2 and pick_up_location=?3 and drop_location=?4 and status=?5",nativeQuery=true)
	public String checkVehicleId(String booking_date,String booking_time,String pick_up_location,
			String drop_Location,String status);
	
	@Query(value="select * from ride where owner_id=?1 and status=?2",nativeQuery=true)
	public List<Ride> findOwnerBookedRides(int owner_id,String status);

}
