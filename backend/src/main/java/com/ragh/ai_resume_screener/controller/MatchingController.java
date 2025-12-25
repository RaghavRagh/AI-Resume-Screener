package com.ragh.ai_resume_screener.controller;

import com.ragh.ai_resume_screener.dto.MatchResultResponse;
import com.ragh.ai_resume_screener.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    @GetMapping
    public ResponseEntity<MatchResultResponse> match(@RequestParam UUID resumeId, @RequestParam UUID jobId) {

        return ResponseEntity.ok(matchingService.match(resumeId, jobId));
    }
}
