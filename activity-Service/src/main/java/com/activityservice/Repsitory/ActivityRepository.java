package com.activityservice.Repsitory;

import com.activityservice.DTO.ActivityResponse;
import com.activityservice.Model.Activity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends MongoRepository<Activity,String> {

    List<Activity> findByUserId(String userId);
    Optional<Activity> findById(String activityId);
}
