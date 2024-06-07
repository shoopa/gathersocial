package com.inspirelegacyventures.gathersocial.controller;

import com.inspirelegacyventures.gathersocial.model.ActivityType;
import com.inspirelegacyventures.gathersocial.service.RecommendationService;
import com.inspirelegacyventures.gathersocial.service.YelpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/{activityId}/{userId}/{activityType}")
    public ResponseEntity<Map<String, List<YelpResponse.Business>>> getRecommendations(
            @PathVariable Long activityId,
            @PathVariable Long userId,
            @PathVariable ActivityType activityType,
            @RequestParam(defaultValue = "10") int radius) {

        Map<String, List<YelpResponse.Business>> recommendations = recommendationService.getRecommendations(activityId, activityType, radius);
        return ResponseEntity.ok(recommendations);
    }
}
