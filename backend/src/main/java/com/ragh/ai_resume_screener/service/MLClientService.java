package com.ragh.ai_resume_screener.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MLClientService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final MlCacheService cache;

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    @Value("${ml.service.similarity-url}")
    private String similarityUrl;

    public Map<String, Object> extractResumeData(File pdfFile) {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(pdfFile));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(mlServiceUrl, request, Map.class);

        return response.getBody();
    }

    public List<String> extractSkillsFromText(String text) {

        Map<String, String> payload = Map.of("text", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(
                        "http://localhost:8001/api/extract_text",
                        request,
                        Map.class
                );

//        System.out.println("Response --> " + response);

        return (List<String>) response.getBody().get("skills");
    }
    
    public Double computeSemanticSimilarity(String resumeText, String jobDescription) {
        String key = Integer.toHexString(Objects.hash(resumeText, jobDescription));

        Double cached = cache.get(key);
        if (cached != null) return cached;

        Map<String, String> payload = Map.of("resume_text", resumeText, "job_description", jobDescription);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(similarityUrl, request, Map.class);

        Double score = ((Number) response.getBody().get("semantic_score")).doubleValue();
        cache.put(key, score);
        
        return score;
    }
}
