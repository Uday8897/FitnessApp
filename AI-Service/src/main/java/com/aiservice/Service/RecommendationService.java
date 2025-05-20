package com.aiservice.Service;

import com.aiservice.Model.Recommendation;
import com.aiservice.Repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public List<Recommendation> getUserRecommendation(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public Optional<Recommendation> getActivity(String activityId) {
        return recommendationRepository.findByActivityId(activityId);
    }
}
