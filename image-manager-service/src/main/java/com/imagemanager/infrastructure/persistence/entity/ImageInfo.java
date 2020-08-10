package com.imagemanager.infrastructure.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author Bruno Okafor 2020-08-07
 */
@Data
@Entity
@Table(name = "image_info")
public class ImageInfo extends AuditModel implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "size", nullable = false)
	private long size;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "file_type", nullable = false)
	private String fileType;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

}
