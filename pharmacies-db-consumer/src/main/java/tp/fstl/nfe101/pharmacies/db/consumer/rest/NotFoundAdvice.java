package tp.fstl.nfe101.pharmacies.db.consumer.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tp.fstl.nfe101.pharmacies.db.consumer.exception.NotFoundException;

@RestControllerAdvice
public class NotFoundAdvice {
    
    @ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String notFoundHandler(NotFoundException ex) {
		return ex.getMessage();
	}
}
