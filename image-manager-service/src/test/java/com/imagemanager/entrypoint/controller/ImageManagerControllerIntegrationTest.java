package com.imagemanager.entrypoint.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.imagemanager.ImageManagerApplication;
import com.imagemanager.core.exception.ImageUploadFailureException;
import com.imagemanager.core.service.ImageService;

/**
 * @author Bruno Okafor 2020-08-09
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {ImageManagerApplication.class})
public class ImageManagerControllerIntegrationTest {

	private static final String ENDPOINT = "/api/v1/image-manager";

	private static final String UPLOAD_IMAGE_ENDPOINT = ENDPOINT + "/upload";

	@MockBean
	private ImageService imageService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@BeforeAll
	public void setUp() {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void should_UploadImage_When_ValidFormData() throws Exception {

		//Arrange
		final MockMultipartFile image = getMockMultipartFile();
		String description = "description";
		final long imageId = 1;

		when(imageService.uploadImage(image, description)).thenReturn(imageId);

		//Act //Assert
		mockMvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_IMAGE_ENDPOINT)
				.file(image)
				.param("description", description))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", ENDPOINT + "/" + imageId));
	}

	@Test
	public void should_ThrowBadRequestException_When_FormDataNotContainingImageFile() throws Exception {

		//Arrange
		String description = "description";

		//Act //Assert
		mockMvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_IMAGE_ENDPOINT)
				.param("description", description))
				.andExpect(status().isBadRequest());

	}

	@Test
	public void should_ThrowBadRequestException_When_FormDataNotContainingDescription() throws Exception {

		//Arrange
		final MockMultipartFile image = getMockMultipartFile();
		String description = "";

		when(imageService.uploadImage(image, description)).thenThrow(IllegalArgumentException.class);

		//Act //Assert
		mockMvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_IMAGE_ENDPOINT)
				.file(image)
				.param("description", description))
				.andExpect(status().isBadRequest());

	}

	@Test
	public void should_ThrowImageUploadFailureException_When_UploadImage() throws Exception {

		//Arrange
		final MockMultipartFile image = getMockMultipartFile();
		String description = "description";

		when(imageService.uploadImage(image, description)).thenThrow(ImageUploadFailureException.class);

		//Act //Assert
		mockMvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_IMAGE_ENDPOINT)
				.file(image)
				.param("description", description))
				.andExpect(status().isInternalServerError());

	}

	private MockMultipartFile getMockMultipartFile() {
		return new MockMultipartFile("image", "file1.png", "text/plain", new byte[10]);
	}

}
