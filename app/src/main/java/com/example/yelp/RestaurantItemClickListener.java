package com.example.yelp;

import android.widget.ImageView;

import com.example.yelp.model.Restaurant;

public interface RestaurantItemClickListener {
    void onRestaurantClick(Restaurant restaurant, ImageView restaurantImageView);
}
