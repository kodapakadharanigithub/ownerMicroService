package com.hcl.ownermicroservice.advice;

 
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hcl.ownermicroservice.exceptions.NoAvailableVehiclesException;
import com.hcl.ownermicroservice.exceptions.NoRidesAvailableException;
import com.hcl.ownermicroservice.exceptions.RideAlreadyExistOrNotException;
import com.hcl.ownermicroservice.exceptions.UserAlreadyExistOrNotException;
import com.hcl.ownermicroservice.exceptions.VehicleAlreadyExistOrNotException;
 
 
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RideAlreadyExistOrNotException.class)

	public ResponseEntity<?> handleRideAlreadyExistOrNotException(RideAlreadyExistOrNotException exception) {

		return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);

	}


	@ExceptionHandler(UserAlreadyExistOrNotException.class)

    public ResponseEntity<?> handleUserAlreadyExistOrNotException(UserAlreadyExistOrNotException exception) {

		return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(VehicleAlreadyExistOrNotException.class)

    public ResponseEntity<?> handleVehicleAlreadyExistOrNotException(VehicleAlreadyExistOrNotException exception) {

		return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);

	}
	
	@ExceptionHandler(NoAvailableVehiclesException.class)

    public ResponseEntity<?> handleNoAvailableVehiclesException(NoAvailableVehiclesException exception) {

		return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);

	}
	
	@ExceptionHandler(NoRidesAvailableException.class)

    public ResponseEntity<?> handleNoRidesAvailableException(NoRidesAvailableException exception) {

		return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);

	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        // Get validation errors from the exception
        List<String> errors = ex.getBindingResult()
                                .getAllErrors()
                                .stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .collect(Collectors.toList());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

	
	


}

