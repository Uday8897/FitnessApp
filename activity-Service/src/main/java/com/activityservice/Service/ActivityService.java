package com.activityservice.Service;

import com.activityservice.DTO.ActivityRequest;
import com.activityservice.DTO.ActivityResponse;
import com.activityservice.Model.Activity;
import com.activityservice.Repsitory.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {
    @Autowired
   private final ActivityRepository activityRepository;
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    UserValidationService userValidationService;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse trackActivity(ActivityRequest req) {

        boolean isValidUser=userValidationService.validateUser(req.getUserId());
        log.info("uuser id {} {}",req.getUserId(),isValidUser);

        if(!isValidUser){
            throw  new RuntimeException("User is not valid"+req.getUserId());
        }
        Activity activity=Activity.builder().userId(req.getUserId())
                .activityType(req.getActivityType()).duration(req.getDuration())
                .startTime(req.getStartTime()).caloriesBurned(req.getCaloriesBurned()).additionalMetrics(req.getAdditionalMetrics())
                .build();
        Activity save = activityRepository.save(activity);
try{
    rabbitTemplate.convertAndSend(exchange,routingKey,save);
}catch (Exception e){
    log.error("Exception occurred while sending data to queue");
    e.printStackTrace();
}

return mapToResponse(save);
    }
    private ActivityResponse mapToResponse(Activity activity){
        ActivityResponse response=new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setActivityType(activity.getActivityType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }

    public List<ActivityResponse> getActivities(String userId) {
        List<Activity> byUserId = activityRepository.findByUserId(userId);
        return byUserId.stream().map(a->mapToResponse(a)).collect(Collectors.toList());
    }

    public ActivityResponse findById(String activityId) {
        Optional<Activity> result = activityRepository.findById(activityId);
        if (result.isEmpty()) {
            throw new RuntimeException("Not found: " + activityId);
        }

        return mapToResponse(result.get());
    }

}
