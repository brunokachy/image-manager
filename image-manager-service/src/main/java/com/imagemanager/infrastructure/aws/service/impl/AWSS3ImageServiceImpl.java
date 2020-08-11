package com.imagemanager.infrastructure.aws.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.cloudtrail.model.S3BucketDoesNotExistException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.imagemanager.core.dto.ImageInfoDTO;
import com.imagemanager.core.service.AWSS3ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * @author Bruno Okafor 2020-08-08
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class AWSS3ImageServiceImpl implements AWSS3ImageService {

	@Value("${app.awsServices.bucketName}")
	private String bucketName;

	private final AmazonS3 amazonS3Client;

	@Override
	public boolean uploadImage(final ImageInfoDTO imageInfo, final MultipartFile image) {

		try {

			if (!amazonS3Client.doesBucketExistV2(bucketName)) {
				throw new S3BucketDoesNotExistException("The following bucket " + bucketName + " does not exist");
			}

			final File file = convertMultiPartToFile(image);

			final PutObjectRequest request = new PutObjectRequest(bucketName, imageInfo.getImageName(), file);

			request.setCannedAcl(CannedAccessControlList.PublicRead);

			amazonS3Client.putObject(request);

			file.delete();

			return true;

		} catch (final SdkClientException | IOException e) {
			log.warn("Unable to upload image name {} in bucket {} ",
					imageInfo.getImageName(),
					bucketName,
					e
			);
			return false;
		}
	}

	@Override
	public void deleteImage(final String fileName) {

		amazonS3Client.deleteObject(bucketName, fileName);
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {

		final File createdFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

		try (FileOutputStream fos = new FileOutputStream(createdFile)) {

			fos.write(file.getBytes());
		}

		return createdFile;
	}
}
