package vagabounds.repositories;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class StorageRepository {
    private final S3Client s3Client;
    private final S3Presigner presigner;
    private static final String BUCKET_NAME = "resumes";

    public StorageRepository(S3Client s3Client, S3Presigner presigner) {
        this.s3Client = s3Client;
        this.presigner = presigner;
    }

    public String store(MultipartFile file, String key) throws IOException {
        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .contentType(file.getContentType())
                .build(),
            RequestBody.fromBytes(file.getBytes())
        );

        return key;
    }

    public void clone(String originalKey, String targetKey) {
        s3Client.copyObject(
            CopyObjectRequest.builder()
                .copySource(BUCKET_NAME + "/" + originalKey)
                .destinationBucket(BUCKET_NAME)
                .destinationKey(targetKey)
                .build()
        );
    }

    /**
     * Gera uma URL pré‐assinada válida por {@code expireInSeconds}.
     */
    public String generatePresignedUrl(String key, long expireInSeconds) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(BUCKET_NAME)
            .key(key)
            .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(expireInSeconds))
            .getObjectRequest(getObjectRequest)
            .build();

        PresignedGetObjectRequest presigned = presigner.presignGetObject(presignRequest);

        return presigned.url().toString();
    }

    public static String buildResumeFileName(String candidateName, String originalFileName) {
        // 1. Normaliza o nome (por ex. remove espaços, acentos, etc.)
        String safeName = candidateName
            .toLowerCase()
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("(^-|-$)", "");

        // 2. Formata timestamp sem caracteres inválidos (yyyyMMddHHmmss)
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // 3. Extrai extensão original do arquivo
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
        }

        // 4. Monta a key completa
        return String.format("resume-%s-%s.%s",
            safeName,
            timestamp,
            extension
        );
    }
}
