package com.momodo.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    public final static String DEFAULT_IMAGE_NAME = "default.png";

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String uploadFileName = UUID.randomUUID() + "-" + fileName.substring(0, fileName.indexOf('.'));

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        amazonS3.putObject(bucket, uploadFileName, file.getInputStream(), objectMetadata);

        return uploadFileName;
    }

    public void deleteFile(String fileName){
        try{
            // 기본 이미지는 삭제하지 않는다.
            if(fileName.equals(DEFAULT_IMAGE_NAME)){
                return;
            }

            boolean isExist = amazonS3.doesObjectExist(bucket, fileName);
            if(isExist){
                amazonS3.deleteObject(bucket, fileName);
            }else{
                log.info("File Not Found");
            }
        }catch (Exception e){
            log.info("Delete File Failed", e);
        }
    }

    public String getS3Url(String fileName){
        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
