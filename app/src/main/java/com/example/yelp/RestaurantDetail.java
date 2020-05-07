package com.example.yelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class RestaurantDetail extends AppCompatActivity {

    private ImageView detail_image;
    private TextView detail_phone;
    private TextView detail_name;
    private RatingBar detail_rating;
    private TextView detail_reviewCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        String img = getIntent().getExtras().getString("detail_image");
        String phone = getIntent().getExtras().getString("detail_phone");
        String name = getIntent().getExtras().getString("name");
        float rating = Float.parseFloat(getIntent().getExtras().getString("detail_rating"));
        String count = getIntent().getExtras().getString("detail_reviewCount");

        detail_image = findViewById(R.id.detail_image);
        detail_phone = findViewById(R.id.detail_phone);
        detail_name = findViewById(R.id.detail_name);
        detail_rating = (RatingBar) findViewById(R.id.detail_ratingBar);
        detail_name = findViewById(R.id.detail_name);
        detail_reviewCount = findViewById(R.id.detail_reviewCount);

        if (img.equals("Not Available")) {
            Picasso.get().load(R.drawable.notavailable).resize(1000, 1000).centerCrop().into(detail_image);
        } else {
            Picasso.get().load(img).resize(1000, 1000).centerCrop().into(detail_image);
        }

        detail_phone.setText(phone);
        detail_name.setText(name);
        detail_rating.setRating(rating);
        detail_reviewCount.setText(count);
    }
}
