package com.example.yelp.model;

public class Restaurant {
    private String name;
    private String location;
    private String phone;
    private String URL;
    private String category;
    private String price;
    private float rating;
    private String reviewCount;
    private String image;
    private String distance;


    public Restaurant(String name, String address, String categoryFinal, String price, String reviewCount, float rating, String distance, String image, String phone) {
        this.name = name;
        this.location = address;
        this.category = categoryFinal;
        this.price = price;
        this.reviewCount = reviewCount;
        this.rating = rating;
        this.distance = distance;
        this.image = image;
        this.phone = phone;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
