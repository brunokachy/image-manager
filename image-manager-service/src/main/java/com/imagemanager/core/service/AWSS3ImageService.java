package com.imagemanager.core.service;

import org.springframework.web.multipart.MultipartFile;

import com.imagemanager.core.dto.ImageInfoDTO;

/**
 * @author Bruno Okafor 2020-08-06
 */
public interface AWSS3ImageService {

	boolean uploadImage(ImageInfoDTO imageInfo, MultipartFile image);

	void deleteImage(String fileName);
}
