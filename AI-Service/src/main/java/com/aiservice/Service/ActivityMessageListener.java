package com.aiservice.Service;

import com.aiservice.Model.Activity;
import com.aiservice.Model.Recommendation;
import com.aiservice.Repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
//AIzaSyA09PlGCJNNo_xwG6KdSt_MsTNQuLDLE2s
@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {
    private final ActivityAIService activityAIService;
    private  final RecommendationRepository recommendationRepository;
    @Value("${rabbitmq.queue.name")
    private String queue;
    @RabbitListener(queues ="activity.queue")
    public void processActivity(Activity activity){
        log.info("Received Obj is {}",activity);
        Recommendation recommendation= activityAIService.generateRecommendation(activity);
        recommendationRepository.save(recommendation);

    }
}
