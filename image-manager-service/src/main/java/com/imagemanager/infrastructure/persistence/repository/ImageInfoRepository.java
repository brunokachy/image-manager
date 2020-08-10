package com.imagemanager.infrastructure.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.imagemanager.infrastructure.persistence.entity.ImageInfo;

/**
 * @author Bruno Okafor 2020-08-07
 */
public interface ImageInfoRepository extends CrudRepository<ImageInfo, Long> {
}
