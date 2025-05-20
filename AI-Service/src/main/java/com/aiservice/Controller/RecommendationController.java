package com.aiservice.Controller;

import com.aiservice.Model.Recommendation;
import com.aiservice.Service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationController
{
    private final RecommendationService recommendationService;
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getRecommendationUser(@PathVariable  String userId){
        return ResponseEntity.ok(recommendationService.getUserRecommendation(userId));

    }
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getActivity(@PathVariable  String activityId){
        return ResponseEntity.ok(recommendationService.getActivity(activityId).orElseThrow(()->new RuntimeException("Not found")));

    }
 }
