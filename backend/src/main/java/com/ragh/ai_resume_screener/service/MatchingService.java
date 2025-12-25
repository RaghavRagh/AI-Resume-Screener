package com.ragh.ai_resume_screener.service;

import com.ragh.ai_resume_screener.dto.MatchResultResponse;
import com.ragh.ai_resume_screener.model.JobDescription;
import com.ragh.ai_resume_screener.model.Resume;
import com.ragh.ai_resume_screener.repository.JobDescriptionRepository;
import com.ragh.ai_resume_screener.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final ResumeRepository resumeRepository;
    private final JobDescriptionRepository jobRepository;

    public MatchResultResponse match(UUID resumeId, UUID jobId) {

        Resume resume = resumeRepository.findById(resumeId).orElseThrow(() -> new RuntimeException("Resume not found."));

        JobDescription job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found."));

        List<String> resumeSkills = Arrays.stream(resume.getSkills().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();

        List<String> jobSkills = Arrays.stream(job.getDescription().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();

        List<String> matched = jobSkills.stream().filter(resumeSkills::contains).toList();
        List<String> missing = jobSkills.stream().filter(skill -> !resumeSkills.contains(skill)).toList();

        int score = (int) (((double) matched.size() / jobSkills.size()) * 100);
        
        return new MatchResultResponse(score, matched, missing);
    }
}
