package com.inspirelegacyventures.gathersocial.controller;

import com.inspirelegacyventures.gathersocial.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private RecommendationService recommendationService;

    @PostMapping("/rate")
    public ResponseEntity<String> rateRecommendation(@RequestParam String businessName, @RequestParam int rating) {
        recommendationService.rateRecommendation(businessName, rating);
        return ResponseEntity.ok("Feedback received");
    }

    @PostMapping("/accuracy")
    public ResponseEntity<String> submitFeedback(@RequestParam String businessName, @RequestParam boolean accurate) {
        recommendationService.submitFeedback(businessName, accurate);
        return ResponseEntity.ok("Feedback received");
    }
}
