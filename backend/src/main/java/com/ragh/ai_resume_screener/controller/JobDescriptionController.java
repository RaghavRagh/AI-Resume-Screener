package com.ragh.ai_resume_screener.controller;

import com.ragh.ai_resume_screener.dto.CreateJobRequest;
import com.ragh.ai_resume_screener.model.JobDescription;
import com.ragh.ai_resume_screener.service.JobDescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobDescriptionController {

    private final JobDescriptionService jobService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createJob(@Valid @RequestBody CreateJobRequest request) {
        UUID jobId = jobService.createJob(request);

        return ResponseEntity.ok(Map.of("jobId", jobId, "message", "Job created successfully!"));
    }

    @GetMapping
    public ResponseEntity<List<JobDescription>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }
}
