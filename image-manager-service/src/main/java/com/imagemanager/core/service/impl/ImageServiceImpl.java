package com.imagemanager.core.service.impl;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.imagemanager.core.dto.ImageInfoDTO;
import com.imagemanager.core.exception.ImageUploadFailureException;
import com.imagemanager.core.service.AWSS3ImageService;
import com.imagemanager.core.service.ImageInfoPersistenceService;
import com.imagemanager.core.service.ImageService;
import lombok.RequiredArgsConstructor;

/**
 * @author Bruno Okafor 2020-08-06
 */
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

	private static final long MAX_FILE_SIZE = 500000;
	private static final String JPG = "JPG";
	private static final String JPEG = "JPEG";
	private static final String PNG = "PNG";

	private final ImageInfoPersistenceService imageInfoPersistenceService;

	private final AWSS3ImageService awss3ImageService;

	@Override
	public long uploadImage(final MultipartFile image, final String description) {

		imageDataValidation(image, description);

		final ImageInfoDTO imageInfoDTO = buildImageInfo(image, description);

		boolean isImageUploadSuccessful = awss3ImageService.uploadImage(imageInfoDTO, image);

		if (!isImageUploadSuccessful) {
			throw new ImageUploadFailureException("Unable to upload image name to S3");
		}

		return imageInfoPersistenceService.saveImageInfo(imageInfoDTO);
	}


	private void imageDataValidation(final MultipartFile image, final String description) {

		if (description == null || description.isBlank()) {
			throw new IllegalArgumentException("Please enter an image description");
		}

		if (image.isEmpty()) {
			throw new IllegalArgumentException("Please select an image file to upload");
		}

		if (image.getSize() > MAX_FILE_SIZE) {
			throw new IllegalArgumentException("Image file exceeded 500KB");
		}

		final String extension = FilenameUtils.getExtension(image.getOriginalFilename());

		if (Stream.of(JPEG, JPG, PNG).noneMatch(extension::equalsIgnoreCase)) {
			throw new IllegalArgumentException("This image file type is not supported");
		}

	}

	private ImageInfoDTO buildImageInfo(final MultipartFile image, final String description) {

		final ImageInfoDTO imageInfoDTO = new ImageInfoDTO();
		imageInfoDTO.setDescription(description);
		imageInfoDTO.setFileType(FilenameUtils.getExtension(image.getOriginalFilename()));
		imageInfoDTO.setSize(image.getSize());
		imageInfoDTO.setImageName(generateFileName(image));

		return imageInfoDTO;
	}

	private String generateFileName(final MultipartFile multiPart) {
		return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
	}

}
