package com.ragh.ai_resume_screener.controller;

import com.ragh.ai_resume_screener.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadResume(@RequestParam("file") MultipartFile file) throws IOException {

        System.out.println("File received: " + file);
        UUID resumeId = resumeService.uploadResume(file);

        return ResponseEntity.ok(Map.of("resumeId", resumeId, "message", "Resume uploaded successfully!"));
    }
}
