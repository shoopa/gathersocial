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

    public List<String> getRecommendations(Long groupId, ActivityType activityType, int radius) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

        // Calculate central location
        String centralLocation = getCentralLocation(group.getMembers());

        // Aggregate preferences
        List<String> allPreferences = new ArrayList<>();
        for (User user : group.getMembers()) {
            String preferences = user.getActivityPreferences().get(activityType);
            if (preferences != null) {
                allPreferences.addAll(Arrays.asList(preferences.split(",")));
            }
        }

        // Count occurrences of each preference
        Map<String, Long> preferenceCounts = allPreferences.stream()
                .collect(Collectors.groupingBy(preference -> preference.trim(), Collectors.counting()));

        // Get the top 3 preferences
        List<String> topPreferences = preferenceCounts.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Get Yelp recommendations
        String yelpCategory = activityTypeToYelpCategory.get(activityType);
        if (yelpCategory == null) {
            throw new RuntimeException("Invalid activity type");
        }

        List<String> recommendations = new ArrayList<>();
        for (String preference : topPreferences) {
            String url = YELP_API_URL + "?categories=" + yelpCategory + "&term=" + preference + "&location=" + centralLocation + "&radius=" + (radius * 1609); // Convert miles to meters
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + yelpApiKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<YelpResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, YelpResponse.class);

            List<String> yelpRecommendations = response.getBody().getBusinesses().stream()
                    .map(Business::getName)
                    .collect(Collectors.toList());

            recommendations.addAll(yelpRecommendations);
        }

        return recommendations.stream().distinct().limit(3).collect(Collectors.toList());
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

    private static class YelpResponse {
        private List<Business> businesses;

        public List<Business> getBusinesses() {
            return businesses;
        }

        public void setBusinesses(List<Business> businesses) {
            this.businesses = businesses;
        }
    }

    private static class Business {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
