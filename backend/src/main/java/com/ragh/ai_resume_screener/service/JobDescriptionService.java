package com.ragh.ai_resume_screener.service;

import com.ragh.ai_resume_screener.dto.CreateJobRequest;
import com.ragh.ai_resume_screener.model.JobDescription;
import com.ragh.ai_resume_screener.repository.JobDescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobDescriptionService {

    private final JobDescriptionRepository jobRepository;

    public UUID createJob(CreateJobRequest request) {

        JobDescription job = new JobDescription();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCreatedAt(LocalDateTime.now());

        jobRepository.save(job);

        return job.getId();
    }

    public List<JobDescription> getAllJobs() {
        return jobRepository.findAll();
    }
}
