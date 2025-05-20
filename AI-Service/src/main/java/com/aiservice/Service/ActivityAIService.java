package com.aiservice.Service;

import com.aiservice.Model.Activity;
import com.aiservice.Model.Recommendation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;

    private  final ObjectMapper objectMapper;
    public Recommendation generateRecommendation(Activity activity) {
        String prompt = createPrompt(activity);

        String answer = geminiService.getAnswer(prompt);
        return parseToRecommendation(activity,answer);

    }
    private Recommendation parseToRecommendation(Activity activity,String res){
        try{
            JsonNode rootNode = objectMapper.readTree(res);
            JsonNode textNode=rootNode.path("candidates").get(0).path("content")
                    .path("parts").get(0).path("text");
            String jsonContent=textNode.asText().replaceAll("```json\\n","")
                    .replaceAll("\\n```","").trim();
            JsonNode analysisJson=objectMapper.readTree(jsonContent);
            JsonNode analysisNode=analysisJson.path("analysis");
            StringBuilder fullAnalysis=new StringBuilder();
            addAnalysisSection(fullAnalysis,analysisNode,"overall","Overall: ");
            addAnalysisSection(fullAnalysis,analysisNode,"pace","Pace: ");
            addAnalysisSection(fullAnalysis,analysisNode,"heartRate","HeartRate: ");
            addAnalysisSection(fullAnalysis,analysisNode,"caloriesBurned","CaloriesBurned: ");
            List<String> improvements=extractImprovements(analysisJson.path("improvements"));
            List<String > suggestions=extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety=extractSafetyGuidelines(analysisJson.path("safety"));

return  Recommendation.builder().activityId(activity.getId()).userId(activity.getUserId())
        .activityType(String.valueOf(activity.getActivityType())).recommendations(fullAnalysis.toString())
        .safety(safety).improvements(improvements).suggestions(suggestions).createdAt(LocalDateTime.now())
        .build();



        }catch (Exception E){
            E.printStackTrace();
            return defaultRecommendation(activity);
        }
           
    }

    private Recommendation defaultRecommendation(Activity activity) {
        String fallbackAnalysis = "AI was unable to process the activity data. Please review manually or try again later.";

        List<String> defaultImprovements = Collections.singletonList("No specific improvements provided");
        List<String> defaultSuggestions = Collections.singletonList("No specific suggestions provided");
        List<String> defaultSafety = Collections.singletonList("No specific safety measures provided");

        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(String.valueOf(activity.getActivityType()))
                .recommendations(fallbackAnalysis)
                .improvements(defaultImprovements)
                .suggestions(defaultSuggestions)
                .safety(defaultSafety)
                .createdAt(LocalDateTime.now())
                .build();
    }
    private List<String> extractSafetyGuidelines(JsonNode safety) {
        List<String > safetyMeasures=new ArrayList<>();
        if(safety.isArray()){
            safety.forEach(saf->{
                safetyMeasures.add(saf.asText());
                    }
                    );
        }
        return safetyMeasures.isEmpty()? Collections.singletonList("No specific safety Measures  provided"):safetyMeasures;


    }

    private List<String> extractSuggestions(JsonNode suggNode) {
        List<String > suggestions=new ArrayList<>();
        if(suggNode.isArray()){
            suggNode.forEach(suggestion->{
                String wo=suggestion.path("workout").asText();
                String des=suggestion.path("description").asText();
                suggestions.add(String.format("%s: %s",wo,des));
            });

        }
        return suggestions.isEmpty()? Collections.singletonList("No specific suggestions  provided"):suggestions;
    }

    private List<String> extractImprovements(JsonNode impNode) {
        List<String > improvements=new ArrayList<>();
     if(impNode.isArray()){
         impNode.forEach(improvement->{
             String area=improvement.path("area").asText();
             String details=improvement.path("recommendation").asText();
             improvements.add(String.format("%s: %s",area,details));
         });

     }
     return improvements.isEmpty()? Collections.singletonList("No specific imvprovemnts provided"):improvements;

    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix).append(analysisNode.path(key).asText()).append("\n\n");
        }
    }

    private String createPrompt(Activity activity) {
        return String.format("""
            Analyze this fitness activity and provide detailed recommendations in the following format
            {
                "analysis" : {
                    "overall": "Overall analysis here",
                    "pace": "Pace analysis here",
                    "heartRate": "Heart rate analysis here",
                    "CaloriesBurned": "Calories Burned here"
                },
                "improvements": [
                    {
                        "area": "Area name",
                        "recommendation": "Detailed Recommendation"
                    }
                ],
                "suggestions" : [
                    {
                        "workout": "Workout name",
                        "description": "Detailed workout description"
                    }
                ],
                "safety": [
                    "Safety point 1",
                    "Safety point 2"
                ]
            }

            Analyze this activity:
            Activity Type: %s
            Duration: %d minutes
            Calories Burned: %d
            Additional Metrics: %s

            Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
            Ensure the response follows the EXACT JSON format shown above.
            """,
                activity.getActivityType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }
}
