package com.inspirelegacyventures.gathersocial.service;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class YelpResponse {
    private List<Business> businesses;

    @Data
    public static class Business {
        private String name;
        private String imageUrl;
        private double rating;
        private String phone;
        private String price;
        private Location location;
        private String address;
        private int userRating;
        private boolean accurate;

        public void setLocation(Location location) {
            this.location = location;
            this.address = location != null ? formatAddress(location) : "Address not available";
        }

        private String formatAddress(Location location) {
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
    }

    @Data
    public static class Location {
        private String address1;
        private String city;
        private String zipCode;
        private List<String> displayAddress;
    }
}
