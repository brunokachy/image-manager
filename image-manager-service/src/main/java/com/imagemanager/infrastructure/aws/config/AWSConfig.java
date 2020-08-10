package com.imagemanager.infrastructure.aws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.log4j.Log4j2;

/**
 * @author Bruno Okafor 2020-08-08
 */
@Log4j2
@Configuration
public class AWSConfig {

	private static final String PROFILE_NAME = "Personal";

	@Bean
	public AmazonS3 amazonS3Client() {
		return AmazonS3ClientBuilder
				.standard()
				.withCredentials(new ProfileCredentialsProvider(PROFILE_NAME))
				.withRegion(Regions.EU_WEST_2)
				.build();
	}
}

