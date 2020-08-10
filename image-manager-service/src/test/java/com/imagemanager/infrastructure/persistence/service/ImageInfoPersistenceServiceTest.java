package com.imagemanager.infrastructure.persistence.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.PersistenceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.imagemanager.core.dto.ImageInfoDTO;
import com.imagemanager.core.service.AWSS3ImageService;
import com.imagemanager.infrastructure.persistence.entity.ImageInfo;
import com.imagemanager.infrastructure.persistence.repository.ImageInfoRepository;

/**
 * @author Bruno Okafor 2020-08-08
 */
class ImageInfoPersistenceServiceTest {

	@InjectMocks
	ImageInfoPersistenceServiceImpl imageInfoPersistenceService;

	@Mock
	private ImageInfoRepository imageInfoRepository;

	@Mock
	private AWSS3ImageService awss3ImageService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void Given_ImageInfoUnableToPersist_When_SaveImageInfo_Then_ThrowPersistenceException() {
		//Arrange
		final ImageInfo imageInfo = buildImageInfo();
		final ImageInfoDTO imageInfoDTO = buildImageInfoDTO();

		when(imageInfoRepository.save(imageInfo)).thenReturn(null);

		//Assert
		assertThatExceptionOfType(PersistenceException.class).isThrownBy(() ->
				this.imageInfoPersistenceService.saveImageInfo(imageInfoDTO))
				.withMessageContaining("Error persisting Image Info record for image " + imageInfoDTO.getImageName());
	}

	@Test
	void Given_ImageInfoPersistSuccessfully_When_SaveImageInfo_Then_ReturnImageId() {
		//Arrange
		final ImageInfo imageInfo = buildImageInfo();
		final ImageInfoDTO imageInfoDTO = buildImageInfoDTO();

		when(imageInfoRepository.save(imageInfo)).thenReturn(imageInfo);

		//Act
		final long imageId = imageInfoPersistenceService.saveImageInfo(imageInfoDTO);

		//Assert
		verify(this.imageInfoRepository, times(1)).save(any(ImageInfo.class));
		assertThat(imageId);
	}

	private ImageInfoDTO buildImageInfoDTO() {

		final ImageInfoDTO imageInfo = new ImageInfoDTO();
		imageInfo.setImageName("imageName");
		imageInfo.setSize(4000);
		imageInfo.setFileType("png");
		imageInfo.setDescription("description");

		return imageInfo;
	}

	private ImageInfo buildImageInfo() {

		final ImageInfo imageInfo = new ImageInfo();
		imageInfo.setName("imageName");
		imageInfo.setSize(4000);
		imageInfo.setFileType("png");
		imageInfo.setDescription("description");

		return imageInfo;
	}
}
