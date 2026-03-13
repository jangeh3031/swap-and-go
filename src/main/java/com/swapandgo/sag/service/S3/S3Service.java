package com.swapandgo.sag.service.S3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private static final String ITEM_IMAGE_FOLDER = "items";
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif"
    );
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    //상픔 이미지 업로드
    public List<String> uploadItemImage(List<MultipartFile> images) {
        if(images == null || images.isEmpty()){
            return Collections.emptyList();
        }
        if (images.size() > 5){
            throw new IllegalArgumentException("이미지는 최대 5장까지 업로드 가능합니다.");
        }
        return images.stream()
                .map(this::validateAndUpload)
                .collect(Collectors.toList());
    }


    //파일 검증
    private String validateAndUpload(MultipartFile image){
        if(!ALLOWED_TYPES.contains(image.getContentType())){
            throw new IllegalArgumentException("지원하지 않는 이미지 형식입니다: " + image.getContentType());
        }
        if(image.getSize() > MAX_FILE_SIZE){
            throw new IllegalArgumentException("이미지 크기는 10MB 이하여야 합니다.");
        }
        return upload(image);
    }

    private String upload(MultipartFile image){
        String fileName = "items/" + UUID.randomUUID() + "_" + image.getOriginalFilename();

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(image.getContentType())
                    .build();
            s3Client.putObject(request, RequestBody.fromInputStream(image.getInputStream(), image.getSize()));
        }catch (IOException e){
            throw new RuntimeException("S3 업로드 중 오류가 발생했습니다.", e);
        }

        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
    }

    public void deleteImage(String imageUrl){
        String key = imageUrl.substring(imageUrl.indexOf("items/"));
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build());
    }

}
