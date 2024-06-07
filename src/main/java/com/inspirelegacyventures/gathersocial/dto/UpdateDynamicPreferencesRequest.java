package com.inspirelegacyventures.gathersocial.dto;

import com.inspirelegacyventures.gathersocial.model.ActivityType;
import lombok.Data;

@Data
public class UpdateDynamicPreferencesRequest {
    private ActivityType activityType;
    private String cuisinePreferences;
    private String priceRange;
}
