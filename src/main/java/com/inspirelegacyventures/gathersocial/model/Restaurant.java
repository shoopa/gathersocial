package com.inspirelegacyventures.gathersocial.model;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Restaurant {

    private String id;
    private String name;
    private String imageUrl;
    private boolean isClosed;
    private String url;
    private int reviewCount;
    private double rating;
    private String address;
    private String city;
    private String zipCode;
    private String country;
    private String state;
    private String phone;
    private double latitude;
    private double longitude;
    private double distance;

}
