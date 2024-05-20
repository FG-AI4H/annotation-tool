package org.fgai4h.ap.helpers;

import org.fgai4h.ap.domain.catalog.model.DataCatalogModel;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.utils.IoUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AWSS3 {


    private AWSS3() {
        throw new IllegalStateException("Utility class");
    }

    public static void putObject(DataCatalogModel dataCatalogModel, String fileType, String keyName, byte[] fileContent) {

        try {

            S3Presigner presigner = S3Presigner.builder()
                    .region(Region.of(dataCatalogModel.getAwsRegion()))
                    //.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                    .build();

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(dataCatalogModel.getBucketName())
                    .key(keyName)
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
            connection.getOutputStream().write(fileContent);
            connection.getResponseCode();

        } catch (S3Exception | IOException e) {
            e.getStackTrace();
        }
    }

    public static String getJsonFromBucket(DataCatalogModel dataCatalogModel) {

        String json = "";
        try {

            S3Presigner presigner = S3Presigner.builder()
                    .region(Region.of(dataCatalogModel.getAwsRegion()))
                    //.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                    .build();

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(dataCatalogModel.getBucketName())
                    .key(dataCatalogModel.getName())
                    .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(60))
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
            String theUrl = presignedGetObjectRequest.url().toString();
            HttpURLConnection connection = (HttpURLConnection) presignedGetObjectRequest.url().openConnection();
            presignedGetObjectRequest.httpRequest().headers().forEach((header, values) -> {
                values.forEach(value -> {
                    connection.addRequestProperty(header, value);
                });
            });

            // Send any request payload that the service needs (not needed when isBrowserExecutable is true).
            if (presignedGetObjectRequest.signedPayload().isPresent()) {
                connection.setDoOutput(true);

                try (InputStream signedPayload = presignedGetObjectRequest.signedPayload().get().asInputStream()) {
                    try (OutputStream httpOutputStream = connection.getOutputStream()) {
                        IoUtils.copy(signedPayload, httpOutputStream);
                    }
                }
            }

            // Download the result of executing the request.
            try (BufferedReader content  = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = content.readLine()) != null) {
                    json += inputLine;
                }
            }

        } catch (S3Exception | IOException e) {
            e.getStackTrace();
        }

        return json;
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
