package com.inspirelegacyventures.gathersocial.service;

import com.inspirelegacyventures.gathersocial.model.ActivityType;
import com.inspirelegacyventures.gathersocial.model.Group;
import com.inspirelegacyventures.gathersocial.model.User;
import com.inspirelegacyventures.gathersocial.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class RecommendationServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RecommendationService recommendationService;

    @Captor
    private ArgumentCaptor<String> urlCaptor;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> httpEntityCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRecommendations() {
        // Setup mock data
        Long groupId = 1L;
        ActivityType activityType = ActivityType.BREAKFAST;
        int radius = 5;

        Group group = new Group();
        group.setId(groupId);

        User user1 = new User();
        user1.setCurrentLocation("37.7749,-122.4194");
        user1.getActivityPreferences().put(activityType, "pancakes,waffles");

        User user2 = new User();
        user2.setCurrentLocation("37.7749,-122.4194");
        user2.getActivityPreferences().put(activityType, "coffee,bagels");

        group.setMembers(new HashSet<>(Arrays.asList(user1, user2)));

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        YelpResponse yelpResponse = new YelpResponse();
        Business business1 = new Business();
        business1.setName("Pancake House");
        Business business2 = new Business();
        business2.setName("Waffle House");
        Business business3 = new Business();
        business3.setName("Coffee Shop");
        yelpResponse.setBusinesses(Arrays.asList(business1, business2, business3));

        ResponseEntity<YelpResponse> responseEntity = ResponseEntity.ok(yelpResponse);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(YelpResponse.class)))
                .thenReturn(responseEntity);

        // Execute the method
        List<String> recommendations = recommendationService.getRecommendations(groupId, activityType, radius);

        // Verify
        assertEquals(3, recommendations.size());
        assertEquals("Pancake House", recommendations.get(0));
        assertEquals("Waffle House", recommendations.get(1));
        assertEquals("Coffee Shop", recommendations.get(2));
    }
}
