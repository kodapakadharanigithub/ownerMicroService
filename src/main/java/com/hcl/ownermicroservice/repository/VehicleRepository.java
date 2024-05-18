package com.hcl.ownermicroservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.hcl.ownermicroservice.entities.Vehicle;

@EnableJpaRepositories
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String>{
	
	@Query(value="Select * from vehicle v where owner_id_fk=?1",nativeQuery=true)
	public List<Vehicle> findVehiclesByOwnerId(int owner_id_fk);
	
	@Query(value="select v.vehicle_id,v.type,v.model,v.owner_id_fk from vehicle v join ride r "
			+ "on v.vehicle_id=r.vehicle_id where r.status=?1 and v.type=?2",
			nativeQuery=true)
	public List<Vehicle> findVehiclesByType(String status,String type);
	
	
	@Query(value="Select * from vehicle v where  vehicle_id=?1 and owner_id_fk=?2 ",nativeQuery=true)
	public Vehicle findOwnerVehicles(String vehicle_id,int owner_id_fk);
	
	
	
	
	
	
	

	


}
