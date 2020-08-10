package com.imagemanager.infrastructure.persistence.service;

import javax.persistence.PersistenceException;

import org.springframework.stereotype.Service;

import com.imagemanager.core.dto.ImageInfoDTO;
import com.imagemanager.core.service.AWSS3ImageService;
import com.imagemanager.core.service.ImageInfoPersistenceService;
import com.imagemanager.infrastructure.persistence.entity.ImageInfo;
import com.imagemanager.infrastructure.persistence.repository.ImageInfoRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Bruno Okafor 2020-08-07
 */
@Service
@RequiredArgsConstructor
public class ImageInfoPersistenceServiceImpl implements ImageInfoPersistenceService {

	private final ImageInfoRepository imageInfoRepository;

	private final AWSS3ImageService awss3ImageService;

	@Override
	public long saveImageInfo(final ImageInfoDTO imageInfoDTO) {

		final ImageInfo imageInfo = buildImageInfo(imageInfoDTO);

		try {

			ImageInfo createdImageInfo = imageInfoRepository.save(imageInfo);

			return createdImageInfo.getId();

		} catch (final Exception ex) {

			awss3ImageService.deleteImage(imageInfoDTO.getImageName());

			throw new PersistenceException("Error persisting Image Info record for image "+ imageInfoDTO.getImageName(), ex);
		}
	}

	private ImageInfo buildImageInfo(final ImageInfoDTO imageInfoDTO) {
		final ImageInfo imageInfo = new ImageInfo();
		imageInfo.setDescription(imageInfoDTO.getDescription());
		imageInfo.setFileType(imageInfoDTO.getFileType());
		imageInfo.setSize(imageInfoDTO.getSize());
		imageInfo.setName(imageInfoDTO.getImageName());

		return imageInfo;
	}
}
