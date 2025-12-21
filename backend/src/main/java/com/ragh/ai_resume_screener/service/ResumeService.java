package com.ragh.ai_resume_screener.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ragh.ai_resume_screener.model.Resume;

import lombok.RequiredArgsConstructor;
import com.ragh.ai_resume_screener.repository.ResumeRepository;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    private static final String UPLOAD_DIR = "uploads/resumes/";

    public UUID uploadResume(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty!");
        }

        Files.createDirectories(Paths.get(UPLOAD_DIR));

        String filePath = UPLOAD_DIR + UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), Paths.get(filePath));

        Resume resume = new Resume();
        resume.setFilename(file.getOriginalFilename());
        resume.setFilepath(filePath);
        resume.setContentType(file.getContentType());
        resume.setFileSize(file.getSize());
        resume.setUploadedAt(LocalDateTime.now());

        resumeRepository.save(resume);

        return resume.getId();
    }
}
