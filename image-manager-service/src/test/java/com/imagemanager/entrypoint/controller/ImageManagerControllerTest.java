package com.imagemanager.entrypoint.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.imagemanager.core.service.ImageService;

/**
 * @author Bruno Okafor 2020-08-08
 */
class ImageManagerControllerTest {

	@InjectMocks
	private ImageManagerController imageManagerController;

	@Mock
	private ImageService imageService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testUploadImagePost_withImageFile_returnsImageResponse() {
		//Arrange
		final String description = "description";
		final long imageId = 1;
		final MockMultipartFile image = new MockMultipartFile("data", "file1.png", "text/plain", new byte[10]);

		when(imageService.uploadImage(image, description)).thenReturn(imageId);

		//Act
		ResponseEntity<?> responseEntity = imageManagerController.uploadImage(image, description);

		//Assert
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertTrue(responseEntity.getHeaders().containsKey("location"));
	}
}
