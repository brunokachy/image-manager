package com.imagemanager.core.service.impl;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.imagemanager.core.dto.ImageInfoDTO;
import com.imagemanager.core.exception.ImageUploadFailureException;
import com.imagemanager.core.service.AWSS3ImageService;
import com.imagemanager.core.service.ImageInfoPersistenceService;

/**
 * @author Bruno Okafor 2020-08-08
 */
class ImageServiceTest {

	@InjectMocks
	private ImageServiceImpl imageService;

	@Mock
	private ImageInfoPersistenceService imageInfoPersistenceService;

	@Mock
	private AWSS3ImageService awss3ImageService;

	private static final String DESCRIPTION = "description";

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void Given_EmptyDescription_When_UploadImage_Then_ThrowIllegalArgumentException() {

		//Arrange
		final MockMultipartFile image = getMockMultipartFile(new byte[10]);
		final String description = "";

		//Act //Assert
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
				this.imageService.uploadImage(image, description))
				.withMessageContaining("Please enter an image description");
	}

	@Test
	void Given_EmptyImage_When_UploadImage_Then_ThrowIllegalArgumentException() throws IOException {

		//Arrange
		final MultipartFile image = new MockMultipartFile("name", InputStream.nullInputStream());

		//Act //Assert
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
				this.imageService.uploadImage(image, DESCRIPTION))
				.withMessageContaining("Please select an image file to upload");
	}

	@Test
	void Given_ImageExceededMaximumSize_When_UploadImage_Then_ThrowIllegalArgumentException() {

		//Arrange
		final MockMultipartFile image = getMockMultipartFile(new byte[1024 * 1024 * 10]);

		//Act //Assert
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
				this.imageService.uploadImage(image, DESCRIPTION))
				.withMessageContaining("Image file exceeded 500KB");
	}

	@Test
	void Given_NotSupportedImageFileType_When_UploadImage_Then_ThrowIllegalArgumentException() {

		//Arrange
		final MockMultipartFile image = new MockMultipartFile("data", "file1.text", "text/plain", new byte[10]);

		//Act //Assert
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
				this.imageService.uploadImage(image, DESCRIPTION))
				.withMessageContaining("This image file type is not supported");
	}

	@Test
	void Given_S3ImageUploadNotSuccessful_When_UploadImage_Then_ThrowImageUploadFailureException() {

		//Arrange
		final MockMultipartFile image = getMockMultipartFile(new byte[10]);

		when(awss3ImageService.uploadImage(any(ImageInfoDTO.class), any(MockMultipartFile.class))).thenReturn(false);

		//Act //Assert
		assertThatExceptionOfType(ImageUploadFailureException.class).isThrownBy(() ->
				this.imageService.uploadImage(image, DESCRIPTION))
				.withMessageContaining("Unable to upload image name to S3");
	}

	@Test
	void Given_S3ImageUploadSuccessful_When_UploadImage_Then_ReturnImageId() {

		//Arrange
		final long id = 1;
		final MockMultipartFile image = getMockMultipartFile(new byte[10]);

		when(awss3ImageService.uploadImage(any(ImageInfoDTO.class), any(MockMultipartFile.class))).thenReturn(true);
		when(imageInfoPersistenceService.saveImageInfo(any(ImageInfoDTO.class))).thenReturn(id);

		//Act
		final long imageId = imageService.uploadImage(image, DESCRIPTION);

		//Assert
		verify(this.awss3ImageService, times(1)).uploadImage(any(ImageInfoDTO.class), any(MockMultipartFile.class));
		verify(this.imageInfoPersistenceService, times(1)).saveImageInfo(any(ImageInfoDTO.class));

		assertEquals(id, imageId);

	}

	private MockMultipartFile getMockMultipartFile(final byte[] bytes) {

		return new MockMultipartFile("data", "file1.png", "text/plain", bytes);
	}
}
