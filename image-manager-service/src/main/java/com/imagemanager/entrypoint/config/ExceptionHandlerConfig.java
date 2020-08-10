package com.imagemanager.entrypoint.config;

import javax.persistence.PersistenceException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.amazonaws.services.cloudtrail.model.S3BucketDoesNotExistException;
import com.imagemanager.core.exception.ImageUploadFailureException;
import lombok.extern.log4j.Log4j2;

/**
 * @author Bruno Okafor 2020-08-05
 */
@ControllerAdvice
@Log4j2
public class ExceptionHandlerConfig extends ResponseEntityExceptionHandler {


	@ExceptionHandler(value = {IllegalArgumentException.class})
	ResponseEntity<Object> handleIllegalArgumentException(final Exception ex) {

		return buildResponseEntity(createErrorResponse(
				ex.getMessage(),
				ex
		), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = {ImageUploadFailureException.class})
	ResponseEntity<Object> handleImageUploadFailureException(final Exception ex) {

		return buildResponseEntity(createErrorResponse(
				ex.getMessage(),
				ex
		), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({PersistenceException.class})
	public ResponseEntity<Object> handleMessagePersistenceException(final PersistenceException ex) {

		return buildResponseEntity(
				createErrorResponse(
						ex.getMessage(),
						ex
				),
				HttpStatus.INTERNAL_SERVER_ERROR
		);
	}

	@ExceptionHandler(value = {S3BucketDoesNotExistException.class})
	ResponseEntity<Object> handleS3BucketDoesNotExistException(final Exception ex) {

		return buildResponseEntity(
				createErrorResponse(
						ex.getMessage(),
						ex
				),
				HttpStatus.INTERNAL_SERVER_ERROR
		);
	}

	private static ResponseEntity<Object> buildResponseEntity(final Object response, final HttpStatus httpStatus) {
		return new ResponseEntity<>(response, httpStatus);
	}

	private String createErrorResponse(final String message, final Exception ex) {

		log.warn(
				"An exception {} has occurred",
				ex.getClass().getSimpleName(),
				ex
		);

		return message;
	}

}
