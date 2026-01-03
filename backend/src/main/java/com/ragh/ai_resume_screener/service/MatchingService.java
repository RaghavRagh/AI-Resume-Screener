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
    private final MLClientService mlClientService;

    public MatchResultResponse match(UUID resumeId, UUID jobId) {

        Resume resume = resumeRepository.findById(resumeId).orElseThrow(() -> new RuntimeException("Resume not found."));

        JobDescription job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found."));

        List<String> resumeSkills = Arrays.stream(resume.getSkills().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();

        System.out.println("resume skills matching part m --> " + resumeSkills);

        List<String> jobSkills = mlClientService.extractSkillsFromText(job.getDescription())
                        .stream().map(String::toLowerCase).toList();

        System.out.println("Resume Skills --> " + resumeSkills);
        System.out.println("Job Skills --> " + jobSkills);

        List<String> matched = jobSkills.stream().filter(resumeSkills::contains).toList();
        List<String> missing = jobSkills.stream().filter(skill -> !resumeSkills.contains(skill)).toList();

        double semanticScore = mlClientService.computeSemanticSimilarity(resume.getResumeText(), job.getDescription());
        
        int skillScore = jobSkills.isEmpty() ? 0 : (int) (((double) matched.size() / jobSkills.size()) * 100);

        int finalScore = getFinalScore(job, semanticScore, skillScore);

        String confidence = getConfidence(jobSkills, matched, finalScore);

        return new MatchResultResponse(finalScore, confidence, matched, missing);
    }

    private String getConfidence(List<String> jobSkills, List<String> matched, int finalScore) {
        boolean hasJobSkills = !jobSkills.isEmpty();
        boolean allRequiredSkillsMatched = hasJobSkills && matched.size() == jobSkills.size();
        String confidence;

        if (!hasJobSkills) {
            confidence = "Weak fit";
        } else if (allRequiredSkillsMatched) {
            confidence = "Good fit";
        } else {
            confidence = confidenceLabel(finalScore);
        }

        if (allRequiredSkillsMatched && finalScore >= 80) {
            confidence = "Strong fit";
        }
        return confidence;
    }

    private static int getFinalScore(JobDescription job, double semanticScore, int skillScore) {
        double semanticWeight;
        double skillWeight;

        switch(job.getRole()) {
            case FRONTEND -> {
                semanticWeight = 0.50;
                skillWeight = 0.50;
            }

            case BACKEND -> {
                semanticWeight = 0.60;
                skillWeight = 0.40;
            }

            case ML -> {
                semanticWeight = 0.70;
                skillWeight = 0.40;
            }

            default -> {
                semanticWeight = 0.55;
                skillWeight = 0.45;
            }
        }

        return (int) ((semanticScore * semanticWeight * 100) + (skillScore * skillWeight));
    }

    private String confidenceLabel(int score) {
        if (score >= 85) return "Strong fit";
        if (score >= 65) return "Good fit";
        if (score >= 40) return "Partial fit";
        return "Weak fit";
    }
}
