package com.hcl.ownermicroservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hcl.ownermicroservice.entities.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner,Integer>{
	
	@Query(value="select * from owner where owner_name=?1",nativeQuery=true)
	public Owner findByName(String ownerName);
	

}
