package com.hcl.ownermicroservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hcl.ownermicroservice.entities.UserBookings;


@Repository
public interface UserBookingsRepository extends JpaRepository<UserBookings, Integer>{
	
	@Query(value="update User_Bookings set ride_id=?1,vehicle_id=?2 where booking_id=?3",nativeQuery=true)
	public void updateUserBooking(int ride_id,String vehicle_id,
			int booking_id);
	
	@Query(value="select * from User_Bookings where user_id=?1",nativeQuery=true)
	public List<UserBookings> findAllBookings(int user_id);
	
	@Query(value="select * from User_Bookings where user_id=?1 and booking_id=?2",nativeQuery=true)
	public UserBookings findBookingByUserId(int user_id,int booking_id);
	
	@Query(value="select * from User_Bookings where status=?1  and user_id=?2",nativeQuery=true)
	public List<UserBookings> findPendingBookings(String status,int user_id);
	
	@Query(value="select * from User_Bookings where status=?1",nativeQuery=true)
	public List<UserBookings> findPendings(String status);
	
	
	@Query(value="select * from User_Bookings where status=?1 and user_id=?2 and booking_id=?3 ",nativeQuery=true)
	public UserBookings findConfirmedBookings(String status,int user_id,int booking_id);

}
