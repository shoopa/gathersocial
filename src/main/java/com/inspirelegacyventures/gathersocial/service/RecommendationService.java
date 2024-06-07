package com.inspirelegacyventures.gathersocial.service;

import com.inspirelegacyventures.gathersocial.model.ActivityType;
import com.inspirelegacyventures.gathersocial.model.Group;
import com.inspirelegacyventures.gathersocial.model.User;
import com.inspirelegacyventures.gathersocial.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${yelp.api.key}")
    private String yelpApiKey;

    private static final String YELP_API_URL = "https://api.yelp.com/v3/businesses/search";
    private static final Map<ActivityType, String> activityTypeToYelpCategory = Map.of(
            ActivityType.BREAKFAST, "breakfast_brunch",
            ActivityType.LUNCH, "restaurants",
            ActivityType.DINNER, "restaurants",
            ActivityType.COFFEE, "coffee",
            ActivityType.DRINKS, "bars"
    );

    private Map<String, YelpResponse.Business> feedbackData = new HashMap<>();

    public Map<String, List<YelpResponse.Business>> getRecommendations(Long groupId, ActivityType activityType, int radius) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

        // Calculate central location
        String centralLocation = getCentralLocation(group.getMembers());

        // Aggregate preferences
        Map<String, Long> preferenceCounts = new HashMap<>();
        for (User user : group.getMembers()) {
            String dynamicPrefs = user.getDynamicPreferences().stream()
                    .filter(p -> p.getActivityType() == activityType)
                    .map(p -> p.getCuisinePreferences())
                    .findFirst()
                    .orElse("");
            String activityPrefs = user.getActivityPreferences().get(activityType);

            // Log user preferences
            System.out.println("User: " + user.getUsername());
            System.out.println("Dynamic Preferences: " + dynamicPrefs);
            System.out.println("Activity Preferences: " + activityPrefs);

            if (dynamicPrefs != null && !dynamicPrefs.isEmpty()) {
                for (String preference : dynamicPrefs.split(",")) {
                    preferenceCounts.merge(preference.trim(), 2L, Long::sum);
                }
            }
            if (activityPrefs != null && !activityPrefs.isEmpty()) {
                for (String preference : activityPrefs.split(",")) {
                    preferenceCounts.merge(preference.trim(), 1L, Long::sum);
                }
            }
        }

        // Get the top 3 preferences
        List<String> topPreferences = preferenceCounts.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        System.out.println("Top Preferences: " + topPreferences);

        // Get Yelp recommendations
        Map<String, List<YelpResponse.Business>> recommendations = new LinkedHashMap<>();
        for (String preference : topPreferences) {
            String yelpCategory = activityTypeToYelpCategory.get(activityType);
            if (yelpCategory == null) {
                throw new RuntimeException("Invalid activity type");
            }

            String url = YELP_API_URL + "?categories=" + yelpCategory + "&term=" + preference + "&location=" + centralLocation + "&radius=" + (radius * 1609); // Convert miles to meters
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + yelpApiKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<YelpResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, YelpResponse.class);

            if (response != null && response.getBody() != null) {
                recommendations.put(preference, response.getBody().getBusinesses().stream()
                        .limit(3)
                        .map(business -> {
                            YelpResponse.Business b = new YelpResponse.Business();
                            b.setName(business.getName());
                            b.setImageUrl(business.getImageUrl());
                            b.setRating(business.getRating());
                            b.setAddress(formatAddress(business.getLocation()));
                            b.setPhone(business.getPhone() != null ? business.getPhone() : "Phone not available");
                            b.setPrice(business.getPrice() != null ? business.getPrice() : "Price not available");
                            return b;
                        })
                        .collect(Collectors.toList()));
            }
        }

        return recommendations;
    }

    private String formatAddress(YelpResponse.Location location) {
        if (location == null) {
            return "Address not available";
        }
        List<String> addressParts = new ArrayList<>();
        if (location.getAddress1() != null) {
            addressParts.add(location.getAddress1());
        }
        if (location.getCity() != null) {
            addressParts.add(location.getCity());
        }
        if (location.getZipCode() != null) {
            addressParts.add(location.getZipCode());
        }
        return addressParts.isEmpty() ? "Address not available" : String.join(", ", addressParts);
    }

    public void rateRecommendation(String businessName, int rating) {
        YelpResponse.Business business = feedbackData.get(businessName);
        if (business != null) {
            business.setUserRating(rating);
        } else {
            YelpResponse.Business newBusiness = new YelpResponse.Business();
            newBusiness.setName(businessName);
            newBusiness.setUserRating(rating);
            feedbackData.put(businessName, newBusiness);
        }
    }

    public void submitFeedback(String businessName, boolean accurate) {
        YelpResponse.Business business = feedbackData.get(businessName);
        if (business != null) {
            business.setAccurate(accurate);
        } else {
            YelpResponse.Business newBusiness = new YelpResponse.Business();
            newBusiness.setName(businessName);
            newBusiness.setAccurate(accurate);
            feedbackData.put(businessName, newBusiness);
        }
    }

    private String getCentralLocation(Set<User> members) {
        double totalLatitude = 0.0;
        double totalLongitude = 0.0;
        int count = 0;

        for (User user : members) {
            String location = user.getCurrentLocation() != null ? user.getCurrentLocation() : user.getHomeLocation();
            if (location != null && !location.isEmpty()) {
                String[] latLong = location.split(",");
                totalLatitude += Double.parseDouble(latLong[0].trim());
                totalLongitude += Double.parseDouble(latLong[1].trim());
                count++;
            }
        }

        if (count == 0) {
            throw new RuntimeException("No valid locations found for group members");
        }

        double averageLatitude = totalLatitude / count;
        double averageLongitude = totalLongitude / count;

        return averageLatitude + "," + averageLongitude;
    }
}
