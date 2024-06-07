//package com.inspirelegacyventures.gathersocial.service;
//
//import com.inspirelegacyventures.gathersocial.model.ActivityType;
//import com.inspirelegacyventures.gathersocial.model.Group;
//import com.inspirelegacyventures.gathersocial.model.User;
//import com.inspirelegacyventures.gathersocial.repository.GroupRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.http.*;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//
//class RecommendationServiceTest {
//
//    @Mock
//    private GroupRepository groupRepository;
//
//    @Mock
//    private RestTemplate restTemplate;
//
//    @InjectMocks
//    private RecommendationService recommendationService;
//
//    @Captor
//    private ArgumentCaptor<String> urlCaptor;
//
//    @Captor
//    private ArgumentCaptor<HttpEntity<String>> httpEntityCaptor;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetRecommendations() {
//        // Setup mock data
//        Long groupId = 1L;
//        ActivityType activityType = ActivityType.BREAKFAST;
//        int radius = 5;
//
//        Group group = new Group();
//        group.setId(groupId);
//
//        User user1 = new User();
//        user1.setCurrentLocation("37.7749,-122.4194");
//        user1.getActivityPreferences().put(activityType, "pancakes,waffles");
//
//        User user2 = new User();
//        user2.setCurrentLocation("37.7749,-122.4194");
//        user2.getActivityPreferences().put(activityType, "coffee,bagels");
//
//        group.setMembers(new HashSet<>(Arrays.asList(user1, user2)));
//
//        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
//
//        YelpResponse yelpResponse = new YelpResponse();
//        YelpResponse.Business business1 = new YelpResponse.Business();
//        business1.setName("Pancake House");
//        business1.setImageUrl("http://example.com/image1.jpg");
//        business1.setRating(4.5);
//        business1.setPhone("+1234567890");
//        YelpResponse.Location location1 = new YelpResponse.Location();
//        location1.setAddress1("123 Main St");
//        location1.setCity("San Francisco");
//        location1.setState("CA");
//        location1.setZipCode("94105");
//        business1.setLocation(location1);
//
//        YelpResponse.Business business2 = new YelpResponse.Business();
//        business2.setName("Waffle House");
//        business2.setImageUrl("http://example.com/image2.jpg");
//        business2.setRating(4.0);
//        business2.setPhone("+1234567891");
//        YelpResponse.Location location2 = new YelpResponse.Location();
//        location2.setAddress1("456 Market St");
//        location2.setCity("San Francisco");
//        location2.setState("CA");
//        location2.setZipCode("94104");
//        business2.setLocation(location2);
//
//        YelpResponse.Business business3 = new YelpResponse.Business();
//        business3.setName("Coffee Shop");
//        business3.setImageUrl("http://example.com/image3.jpg");
//        business3.setRating(4.2);
//        business3.setPhone("+1234567892");
//        YelpResponse.Location location3 = new YelpResponse.Location();
//        location3.setAddress1("789 Mission St");
//        location3.setCity("San Francisco");
//        location3.setState("CA");
//        location3.setZipCode("94103");
//        business3.setLocation(location3);
//
//        yelpResponse.setBusinesses(Arrays.asList(business1, business2, business3));
//
//        ResponseEntity<YelpResponse> responseEntity = ResponseEntity.ok(yelpResponse);
//        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(YelpResponse.class)))
//                .thenReturn(responseEntity);
//
//        // Execute the method
//        List<YelpResponse.Business> recommendations = recommendationService.getRecommendations(groupId, activityType, radius);
//
//        // Verify
//        assertEquals(3, recommendations.size());
//        assertEquals("Pancake House", recommendations.get(0).getName());
//        assertEquals("Waffle House", recommendations.get(1).getName());
//        assertEquals("Coffee Shop", recommendations.get(2).getName());
//        assertEquals("http://example.com/image1.jpg", recommendations.get(0).getImageUrl());
//        assertEquals("http://example.com/image2.jpg", recommendations.get(1).getImageUrl());
//        assertEquals("http://example.com/image3.jpg", recommendations.get(2).getImageUrl());
//        assertEquals("123 Main St, San Francisco, CA 94105", recommendations.get(0).getLocation().getFullAddress());
//        assertEquals("456 Market St, San Francisco, CA 94104", recommendations.get(1).getLocation().getFullAddress());
//        assertEquals("789 Mission St, San Francisco, CA 94103", recommendations.get(2).getLocation().getFullAddress());
//    }
//}
