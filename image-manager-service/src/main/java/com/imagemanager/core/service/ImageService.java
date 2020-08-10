package com.imagemanager.core.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Bruno Okafor 2020-08-08
 */
public interface ImageService {

	long uploadImage(MultipartFile image, String description);
}
