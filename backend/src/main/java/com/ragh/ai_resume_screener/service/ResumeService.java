package com.ragh.ai_resume_screener.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ragh.ai_resume_screener.model.Resume;

import lombok.RequiredArgsConstructor;
import com.ragh.ai_resume_screener.repository.ResumeRepository;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final MLClientService mlClientService;

    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    public UUID uploadResume(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty!");
        }

        Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Safe file save
        file.transferTo(filePath.toFile());

        Map<String, Object> mlResponse = mlClientService.extractResumeData(filePath.toFile());

        String resumeText = (String) mlResponse.get("text");
        @SuppressWarnings("unchecked")
        List<String> skills = (List<String>) mlResponse.get("skills");

        Resume resume = new Resume();
        resume.setFilename(file.getOriginalFilename());
        resume.setFilepath(filePath.toString());
        resume.setContentType(file.getContentType());
        resume.setResumeText(resumeText);
        resume.setFileSize(file.getSize());
        resume.setUploadedAt(LocalDateTime.now());
        resume.setSkills(String.join(",", skills));

        resumeRepository.save(resume);

        return resume.getId();
    }
}
