package com.inspirelegacyventures.gathersocial.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspirelegacyventures.gathersocial.model.Restaurant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class YelpService {

    @Value("${yelp.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public YelpService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String searchRestaurants(String location, String term) {
        String url = "https://api.yelp.com/v3/businesses/search?location=" + location + "&term=" + term;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    private List<Restaurant> parseRestaurants(String responseBody) {
        List<Restaurant> restaurants = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode businesses = root.path("businesses");

            for (JsonNode business : businesses) {
                Restaurant restaurant = new Restaurant();
                restaurant.setId(business.path("id").asText());
                restaurant.setName(business.path("name").asText());
                restaurant.setImageUrl(business.path("image_url").asText());
                restaurant.setClosed(business.path("is_closed").asBoolean());
                restaurant.setUrl(business.path("url").asText());
                restaurant.setReviewCount(business.path("review_count").asInt());
                restaurant.setRating(business.path("rating").asDouble());
                restaurant.setAddress(business.path("location").path("address1").asText());
                restaurant.setCity(business.path("location").path("city").asText());
                restaurant.setZipCode(business.path("location").path("zip_code").asText());
                restaurant.setCountry(business.path("location").path("country").asText());
                restaurant.setState(business.path("location").path("state").asText());
                restaurant.setPhone(business.path("phone").asText());
                restaurant.setLatitude(business.path("coordinates").path("latitude").asDouble());
                restaurant.setLongitude(business.path("coordinates").path("longitude").asDouble());
                restaurant.setDistance(business.path("distance").asDouble());

                restaurants.add(restaurant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restaurants;
    }
}
