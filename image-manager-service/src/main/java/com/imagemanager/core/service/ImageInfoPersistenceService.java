package com.imagemanager.core.service;

import com.imagemanager.core.dto.ImageInfoDTO;

/**
 * @author Bruno Okafor 2020-08-07
 */
public interface ImageInfoPersistenceService {

	long saveImageInfo(ImageInfoDTO imageInfoDTO);

}
