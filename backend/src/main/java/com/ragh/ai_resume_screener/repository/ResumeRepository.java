package com.ragh.ai_resume_screener.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ragh.ai_resume_screener.model.Resume;

public interface ResumeRepository extends JpaRepository<Resume, UUID> {
}
