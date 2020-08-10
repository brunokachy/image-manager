package com.imagemanager.core.dto;

import lombok.Data;

/**
 * @author Bruno Okafor 2020-08-06
 */
@Data
public class ImageInfoDTO {

	private String imageName;

	private long id;

	private long size;

	private String description;

	private String fileType;
}
