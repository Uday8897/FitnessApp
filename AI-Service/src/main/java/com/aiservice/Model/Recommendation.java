package com.aiservice.Model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "recommendations")
@Data
@Builder
public class Recommendation {
    private String id;
    private String activityId;
    private String userId;
    private String activityType;
    private  String recommendations;
    private List<String> improvements;
    private  List<String > suggestions;
     private  List<String> safety;
     @CreatedDate
     private LocalDateTime createdAt;



}
