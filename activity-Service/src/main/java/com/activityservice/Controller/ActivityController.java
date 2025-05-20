package com.activityservice.Controller;

import com.activityservice.DTO.ActivityRequest;
import com.activityservice.DTO.ActivityResponse;
import com.activityservice.Enums.ActivityType;
import com.activityservice.Service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@Slf4j
public class ActivityController {
    @Autowired
    ActivityService activityService;
    @PostMapping
    ResponseEntity<ActivityResponse> getResponse(@RequestBody  ActivityRequest req){
        return ResponseEntity.ok(activityService.trackActivity(req));


    }
    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getAllActivities(@RequestHeader("X-User-ID") String userId){
        return ResponseEntity.ok(activityService.getActivities(userId));
    }
    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getById(@PathVariable String activityId){
        log.info("Activity id is:{}",activityId);
        return ResponseEntity.ok(activityService.findById(activityId));


    }
}
