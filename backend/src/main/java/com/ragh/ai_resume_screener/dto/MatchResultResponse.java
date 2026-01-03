package com.ragh.ai_resume_screener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MatchResultResponse {

    private int matchScore;
    private String confidence;
    private List<String> matchedSkills;
    private List<String> missingSkills;
}
