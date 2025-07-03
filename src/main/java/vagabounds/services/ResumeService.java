package vagabounds.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vagabounds.models.Application;
import vagabounds.repositories.ApplicationRepository;
import vagabounds.repositories.CandidateRepository;
import vagabounds.repositories.StorageRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ResumeService {

    @Autowired
    StorageRepository storageRepository;

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    public String storeCandidateResume(Long accountId, MultipartFile file) {
        var candidate = candidateRepository.findByAccountId(accountId).orElse(null);

        if (candidate == null || candidate.getIsDeleted()) {
            throw new RuntimeException("Candidate not found!");
        }

        var key = generateKey(candidate.getId(), candidate.getName(), file.getOriginalFilename());

        try {
            storageRepository.store(file, key);
        } catch(IOException e) {
            throw new RuntimeException("Failure to upload resume to storage. Error: " + e.getMessage());
        }

        candidate.setResumeUrl(key);

        candidateRepository.save(candidate);

        return key;
    }

    public String loadCandidateResume(Long accountId) {
        var candidate = candidateRepository.findByAccountId(accountId).orElse(null);

        if (candidate == null || candidate.getIsDeleted()) {
            throw new RuntimeException("Candidate not found!");
        }

        return storageRepository.generatePresignedUrl(candidate.getResumeUrl(), 15 * 60);
    }

    @Transactional
    public void cloneCurrentCandidateResumeToApplication(Application application, String originalResumePath) {
        // Extrai o filename da key original
        String filename = originalResumePath.substring(originalResumePath.lastIndexOf('/') + 1);

        // Monta a nova key dentro de "application/{applicationId}/"
        String targetKey = String.format("application/%d/%s", application.getId(), filename);

        try {
            storageRepository.clone(originalResumePath, targetKey);
        } catch(Exception e) {
            throw new RuntimeException("Failure to upload resume to storage. Error: " + e.getMessage());
        }

        application.setResumeUrl(targetKey);

        applicationRepository.save(application);
    }

    private String generateKey(Long candidateId, String candidateName, String originalFileName) {
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
        return String.format("candidate/%d/resume-%s-%s.%s",
            candidateId,
            safeName,
            timestamp,
            extension
        );
    }
}
