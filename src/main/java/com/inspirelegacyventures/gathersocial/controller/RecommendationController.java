package com.inspirelegacyventures.gathersocial.controller;

import com.inspirelegacyventures.gathersocial.model.Activity;
import com.inspirelegacyventures.gathersocial.model.ActivityType;
import com.inspirelegacyventures.gathersocial.model.User;
import com.inspirelegacyventures.gathersocial.service.RecommendationService;
import com.inspirelegacyventures.gathersocial.repository.ActivityRepository;
import com.inspirelegacyventures.gathersocial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{activityId}/{userId}/{activityType}")
    public ResponseEntity<List<String>> getRecommendations(
            @PathVariable Long activityId,
            @PathVariable Long userId,
            @PathVariable ActivityType activityType,
            @RequestParam(defaultValue = "10") int radius) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Ensure the user is the host of the activity
        if (!activity.getHost().getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<String> recommendations = recommendationService.getRecommendations(activity.getGroup().getId(), activityType, radius);
        return new ResponseEntity<>(recommendations, HttpStatus.OK);
    }
}
