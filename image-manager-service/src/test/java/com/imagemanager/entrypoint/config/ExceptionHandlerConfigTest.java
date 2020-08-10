package com.imagemanager.entrypoint.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.persistence.PersistenceException;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.amazonaws.services.cloudtrail.model.S3BucketDoesNotExistException;
import com.imagemanager.core.exception.ImageUploadFailureException;

/**
 * @author Bruno Okafor 2020-08-08
 */
class ExceptionHandlerConfigTest {

	private static final ExceptionHandlerConfig REST_EXCEPTION_HANDLER = new ExceptionHandlerConfig();

	private static final String ERROR = "Error";


	@Test
	void handleMessageImageUploadFailureException_whenImageUploadFailureException_throwsInternalServerError() {

		//Act
		final ResponseEntity<Object> result = REST_EXCEPTION_HANDLER.handleImageUploadFailureException(new ImageUploadFailureException(ERROR));

		//Assert
		assertNotNull(result);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
	}

	@Test
	void handleMessageS3BucketDoesNotExistException_whenS3BucketDoesNotExistException_throwsNotFound() {

		//Act
		final ResponseEntity<Object> result = REST_EXCEPTION_HANDLER.handleS3BucketDoesNotExistException(new S3BucketDoesNotExistException(ERROR));

		//Assert
		assertNotNull(result);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
	}

	@Test
	void handleMessagePersistenceException_whenPersistenceException_throwsNotFound() {

		//Act
		final ResponseEntity<Object> result = REST_EXCEPTION_HANDLER.handleMessagePersistenceException(new PersistenceException(ERROR));

		//Assert
		assertNotNull(result);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
	}

	@Test
	void handleMessageIllegalArgumentException_whenIllegalArgumentException_throwsNotFound() {

		//Act
		final ResponseEntity<Object> result = REST_EXCEPTION_HANDLER.handleIllegalArgumentException(new IllegalArgumentException(ERROR));

		//Assert
		assertNotNull(result);
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
	}
}
