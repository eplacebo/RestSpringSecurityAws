package com.eplacebo.springapi.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.eplacebo.springapi.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 s3client;
    private final String bucketName = "";

    public void createBucket(String newBucket) {
        if (s3client.doesBucketExistV2(newBucket)) {
            log.info("Bucket {} already exists, use a different name", newBucket);
            return;
        }
        s3client.createBucket(newBucket);
    }

    public List<S3ObjectSummary> listFiles() {
        ObjectListing objects = s3client.listObjects(bucketName);
        for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
            log.info("File name: {}", objectSummary.getKey());
        }
        return objects.getObjectSummaries();
    }

    public List<Bucket> listBuckets() {
        return s3client.listBuckets();
    }

    public void deleteFile(String keyFile) {
        s3client.deleteObject(bucketName, keyFile);
    }

    public String getUrlFile(MultipartFile file) {
        String url = String.valueOf(s3client.getUrl(bucketName, file.getOriginalFilename()));
        return url;
    }

    @SneakyThrows
    public void uploadFile(MultipartFile file) {
        s3client.putObject(bucketName, file.getOriginalFilename(), file.getInputStream(), new ObjectMetadata());
    }

    @SneakyThrows
    public void downloadFile(String key) {
        S3Object s3object = s3client.getObject(bucketName, key);
        S3ObjectInputStream inputStream = s3object.getObjectContent();

        File file = new File("src\\main\\resources\\" + key);
        FileCopyUtils.copy(inputStream, new FileOutputStream(file));
    }
}
