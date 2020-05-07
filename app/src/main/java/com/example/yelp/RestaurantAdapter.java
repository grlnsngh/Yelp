package com.example.yelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yelp.model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {

    Context context;
    List<Restaurant> mData;
    RestaurantItemClickListener restaurantItemClickListener;

    public RestaurantAdapter(Context context, List<Restaurant> mData, RestaurantItemClickListener restaurantItemClickListener) {
        this.context = context;
        this.mData = mData;
        this.restaurantItemClickListener = restaurantItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getName());
        holder.numReviews.setText(mData.get(position).getReviewCount());
        holder.address.setText(mData.get(position).getLocation());
        holder.category.setText(mData.get(position).getCategory());
        holder.price.setText(mData.get(position).getPrice());
        holder.rating.setRating(mData.get(position).getRating());
        holder.distance.setText(mData.get(position).getDistance());
        if (mData.get(position).getImage().equals("Not Available")) {
            Picasso.get().load(R.drawable.notavailable).transform(new CropCircleTransformation()).into(holder.image);
        } else {
            Picasso.get().load(mData.get(position).getImage()).transform(new CropCircleTransformation()).into(holder.image);
        }
        holder.cs.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView numReviews;
        private TextView address;
        private TextView category;
        private TextView price;
        private RatingBar rating;
        private TextView distance;
        private ImageView image;
        private ConstraintLayout cs;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            numReviews = itemView.findViewById(R.id.tvNumReviews);
            address = itemView.findViewById(R.id.tvAddress);
            category = itemView.findViewById(R.id.tvCategory);
            price = itemView.findViewById(R.id.tvPrice);
            rating = (RatingBar) itemView.findViewById(R.id.ratingBar);
            distance = itemView.findViewById(R.id.tvDistance);
            image = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restaurantItemClickListener.onRestaurantClick(mData.get(getAdapterPosition()), image);
                }
            });
            cs = itemView.findViewById(R.id.row);
        }
    }
}
