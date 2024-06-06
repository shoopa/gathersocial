package com.inspirelegacyventures.gathersocial.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class YelpServiceTest {

    @InjectMocks
    private YelpService yelpService;

    @Mock
    private RestTemplate restTemplate;

    @Value("${yelp.api.key}")
    private String apiKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchRestaurants() {
        String location = "San Francisco";
        String term = "pizza";
        String responseBody = "{ \"businesses\": [ { \"name\": \"Restaurant 1\", \"image_url\": \"\", \"is_closed\": false, \"url\": \"\", \"review_count\": 10, \"rating\": 4.5, \"location\": { \"address1\": \"\", \"city\": \"\", \"zip_code\": \"\", \"country\": \"\", \"state\": \"\" }, \"phone\": \"\", \"coordinates\": { \"latitude\": 0.0, \"longitude\": 0.0 }, \"distance\": 0.0 } ] }";

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));

        String response = yelpService.searchRestaurants(location, term);
        assertNotNull(response);
        verify(restTemplate, times(1)).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class));
    }
}
