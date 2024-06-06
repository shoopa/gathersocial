package com.inspirelegacyventures.gathersocial.controller;

import com.inspirelegacyventures.gathersocial.service.YelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestaurantController {

    @Autowired
    private YelpService yelpService;

    @GetMapping("/restaurants")
    public String getRestaurants(@RequestParam String location, @RequestParam String term) {
        return yelpService.searchRestaurants(location, term);
    }
}