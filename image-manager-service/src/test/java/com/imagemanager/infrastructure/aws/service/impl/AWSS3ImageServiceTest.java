package com.imagemanager.infrastructure.aws.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.imagemanager.core.dto.ImageInfoDTO;

/**
 * @author Bruno Okafor 2020-08-09
 */
class AWSS3ImageServiceTest {

	@InjectMocks
	private AWSS3ImageServiceImpl awss3ImageService;

	@Mock
	private AmazonS3 amazonS3Client;

	private String bucketName = "bucketName";

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(awss3ImageService, "bucketName", bucketName);
	}

	@Test
	void Given_S3ImageUploadSuccessful_When_UploadImage_Then_ReturnTrue() {
		//Arrange
		final ImageInfoDTO imageInfoDTO = buildImageInfoDTO();
		final MockMultipartFile image = getMockMultipartFile();

		when(amazonS3Client.doesBucketExistV2(bucketName)).thenReturn(true);

		//Act
		boolean response = awss3ImageService.uploadImage(imageInfoDTO, image);

		//Assert
		verify(this.amazonS3Client, times(1)).doesBucketExistV2(bucketName);
		verify(this.amazonS3Client, times(1)).putObject(any(PutObjectRequest.class));
		assertTrue(response);
	}

	@Test
	void Given_S3ImageDeleteSuccessful_When_DeleteImage_Then_ThrowNoException() {
		//Arrange
		final String fileName = "fileName";

		//Act
		awss3ImageService.deleteImage(fileName);

		//Assert
		verify(this.amazonS3Client, times(1)).deleteObject(bucketName, fileName);
	}

	private MockMultipartFile getMockMultipartFile() {

		return new MockMultipartFile("data", "file1.png", "text/plain", new byte[10]);
	}

	private ImageInfoDTO buildImageInfoDTO() {

		final ImageInfoDTO imageInfo = new ImageInfoDTO();
		imageInfo.setImageName("imageName");
		imageInfo.setSize(4000);
		imageInfo.setFileType("png");
		imageInfo.setDescription("description");

		return imageInfo;
	}
}
