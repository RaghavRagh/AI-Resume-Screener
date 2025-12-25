package com.ragh.ai_resume_screener.repository;

import com.ragh.ai_resume_screener.model.JobDescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobDescriptionRepository extends JpaRepository<JobDescription, UUID> {
}
