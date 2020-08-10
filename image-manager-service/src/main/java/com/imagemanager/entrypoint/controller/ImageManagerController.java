package com.imagemanager.entrypoint.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imagemanager.core.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * @author Bruno Okafor 2020-08-06
 */
@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping(ImageManagerController.ENDPOINT)
@Api(produces = MediaType.APPLICATION_JSON_VALUE, tags = "Image Manager")
public class ImageManagerController {

	public static final String ENDPOINT = "/api/v1/image-manager";

	private final ImageService imageService;

	@ApiOperation("Upload Image")
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadImage(@RequestParam MultipartFile image, @RequestParam String description) {

		long imageId = imageService.uploadImage(image, description);

		URI location = URI.create(ENDPOINT + "/" + imageId);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setLocation(location);

		return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
	}

}
