package org.fgai4h.ap.helpers;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AWSS3 {


    private AWSS3() {
    }

    public static void signBucket(String fileType, byte[] filecontent) {

        try {

            S3Presigner presigner = S3Presigner.builder()
                    .region(Region.EU_CENTRAL_1)
                    .build();

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket("bucketName")
                    .key("keyName")
                    .contentType(fileType)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

            // Upload content to the Amazon S3 bucket by using this URL
            URL url = presignedRequest.url();

            // Create the connection and use it to upload the new object by using the presigned URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",fileType);
            connection.setRequestMethod("PUT");
            connection.getOutputStream().write(filecontent);
            connection.getResponseCode();

        } catch (S3Exception | IOException e) {
            e.getStackTrace();
        }
    }

    public static List<S3Object> listBucketObjects(S3Client s3, String bucketName, String prefix ) {

        try {

            ListObjectsV2Response res = s3.listObjectsV2(ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build());
            return res.contents();

        } catch (S3Exception e) {
            e.getStackTrace();
        }

        return new ArrayList<>();
    }

}
