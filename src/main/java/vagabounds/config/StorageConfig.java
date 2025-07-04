package vagabounds.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class StorageConfig {

    @Value("${storage.endpoint}")
    private String endpoint;

    @Value("${storage.region}")
    private String region;

    @Value("${storage.access-key}")
    private String accessKey;

    @Value("${storage.secret-key}")
    private String secretKey;

    @Value("${storage.bucket}")
    private String bucketName;

    @Value("${storage.path-style-access}")
    private boolean pathStyleAccess;

    @Bean
    public S3Client storageClient() {
        S3Configuration s3Config = S3Configuration.builder()
            .pathStyleAccessEnabled(pathStyleAccess)
            .build();

        return S3Client.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .serviceConfiguration(s3Config)
            .httpClientBuilder(UrlConnectionHttpClient.builder())
            .build();
    }

    @Bean
    public S3Presigner presigner() {
        S3Configuration s3Config = S3Configuration.builder()
            .pathStyleAccessEnabled(pathStyleAccess)
            .build();

        return S3Presigner.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .serviceConfiguration(s3Config)
            .build();
    }

    @Bean
    public boolean initializeBucket(S3Client s3Client) {
        // cria bucket se não existir
        try {
            s3Client.createBucket(CreateBucketRequest.builder()
                .bucket(bucketName)
                .build());
        } catch (Exception e) {
            // se já existir, ignora
        }

        return true;
    }
}
