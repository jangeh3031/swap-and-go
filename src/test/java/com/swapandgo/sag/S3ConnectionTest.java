package com.swapandgo.sag;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;

@SpringBootTest
@Tag("integration")
public class S3ConnectionTest {
    @Autowired
    private S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Test
    void S3_연결테스트(){
        HeadBucketRequest request = HeadBucketRequest.builder()
                .bucket(bucket)
                .build();

        s3Client.headBucket(request);
        System.out.println("연결 성공");
    }
}
