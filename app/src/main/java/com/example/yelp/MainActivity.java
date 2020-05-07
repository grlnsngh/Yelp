package com.example.yelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yelp.model.Restaurant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity implements RestaurantItemClickListener, AdapterView.OnItemClickListener {

    private RecyclerView ResRecyclerView;
    private static final String TAG = "<>";
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    public static LatLng MYLOCATION = null;
    private double longitute, latitude;

    //    TextView textView;
    //Declare a private RequestQueue variable
    private RequestQueue requestQueue;
    private static MainActivity mInstance;
    private String BASE_URL = "https://api.yelp.com/v3/";
    private String API_KEY = "QxKM00qEBt0u0H3EhK-ESjcVMcRloJQXMyAe5wG2W40_SjS_X4GdIHlBoanTWn7sIJTAYfOmBxDQCuqhYRcIeVlAcvVrGbUIRMfM4Y4D5emaOiFknGEIoRwKeoeKXnYx";

    private static final String[] COUNTRIES = new String[]{
            "Afganistan", "Albania", "Algeria", "Andorra", "Angola"
    };
    private static final String[] COUNTRIESS = new String[]{
            "AAfganistan", "AAlbania", "AAlgeria", "AAndorra", "AAngola"
    };
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Log.d(TAG, "onCreate");

        setStatusBarColor(this, Color.parseColor("#d32323"));


        mInstance = this;

        // Initialize the FusedLocationClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(
                this);
        ResRecyclerView = findViewById(R.id.resRV);
        getLastLocation();

        //autocomplete
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autotv);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, COUNTRIES);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(this);

//        handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message msg) {
//                Log.d(TAG, autoCompleteTextView.getText().toString());
//                if (msg.what == TRIGGER_AUTO_COMPLETE) {
//                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
//                        makeApiCall(autoCompleteTextView.getText().toString());
//                        Toast.makeText(MainActivity.this, autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//                return false;
//            }
//        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // fetch the user selected value
        String item = parent.getItemAtPosition(position).toString();

        // create Toast with user selected value
        Toast.makeText(MainActivity.this, "Selected Item is: \t" + item, Toast.LENGTH_LONG).show();

//        if (position == 2) {
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_list_item_1, COUNTRIESS);
//            autoCompleteTextView.setAdapter(adapter);
//            autoCompleteTextView.setOnItemClickListener(this);
//        }
    }


    public static void setStatusBarColor(Activity activity, int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // If both system bars are black, we can remove these from our layout,
            // removing or shrinking the SurfaceFlinger overlay required for our views.
            Window window = activity.getWindow();
            if (statusBarColor == Color.BLACK && window.getNavigationBarColor() == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setStatusBarColor(Color.parseColor("#d32323"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);

        } else {

            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {

                                mLastLocation = location;
                                longitute = mLastLocation.getLongitude();
                                latitude = mLastLocation.getLatitude();
                                Log.d("Location Coordinates", "Lat: " + mLastLocation.getLatitude() + "<<<<<>>>>> Long:" + mLastLocation.getLongitude());

                                showYelpData(mLastLocation.getLongitude(), mLastLocation.getLatitude());
                            } else {
                                Log.d("Location Coordinates", "Nothing");
                            }
                        }

                    }
            );
        }
    }

    public static synchronized MainActivity getInstance() {
        return mInstance;
    }

    /*
    Create a getRequestQueue() method to return the instance of
    RequestQueue.This kind of implementation ensures that
    the variable is instatiated only once and the same
    instance is used throughout the application
    */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        return requestQueue;
    }

    /*
    public method to add the Request to the the single
    instance of RequestQueue created above.Setting a tag to every
    request helps in grouping them. Tags act as identifier
    for requests and can be used while cancelling them
    */
    public void addToRequestQueue(Request request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    /*
    Cancel all the requests matching with the given tag
    */
    public void cancelAllRequests(String tag) {
        getRequestQueue().cancelAll(tag);
    }

    private void showYelpData(double longitude, double latitude) {
        String POST_URL = "businesses/search?latitude=" + latitude + "&longitude=" + longitude;
        String url = BASE_URL + POST_URL;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            setupRecyclerView(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println("Error: " + error.toString());
                    }
                }) {
            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + API_KEY);
                System.out.println("Header: " + headers);
                return headers;
            }
        };

        MainActivity.getInstance().addToRequestQueue(jsonObjectRequest, "headerRequest");

    }

    private void setupRecyclerView(JSONObject response) throws JSONException {
        JSONArray jsonArray = response.getJSONArray("businesses");
        System.out.println("Response: " + response.toString());

        List<Restaurant> lstRestaurants = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject businesses = jsonArray.getJSONObject(i);

            String name = businesses.getString("name");

            JSONObject lc = businesses.getJSONObject("location");
            String address = lc.getString("address1");
            if (!(lc.getString("address2").equals("") || lc.getString("address2").equalsIgnoreCase("null"))) {
                address += ", " + lc.getString("address2");
            }
            if (!(lc.getString("address3").equals("") || lc.getString("address3").equalsIgnoreCase("null"))) {
                address += ", " + lc.getString("address3");
            }

            JSONArray categories = businesses.getJSONArray("categories");
            String categoryFinal = "";
            for (int j = 0; j < categories.length(); j++) {
                JSONObject category = categories.getJSONObject(j);
                if (j > 0) {
                    categoryFinal += ", ";
                }
                categoryFinal += category.getString("title");
            }

            String price = "Not Available";
            if (businesses.has("price")) {
                price = businesses.getString("price");
            }

            String reviewCount = businesses.getString("review_count");
            float rating = (float) businesses.getDouble("rating");

            String distance = businesses.getString("distance");
            double milesPerMeter = 0.000621371;
            double distanceInMiles = milesPerMeter * Double.parseDouble(distance);
            double roundedOfDistance = Math.round(distanceInMiles * 10) / 10.0;

            String url = "Not Available";
            if (businesses.has("image_url") && !(businesses.getString("image_url").isEmpty())) {
                url = businesses.getString("image_url");
            }

            String phone = "Not Available";
            if (businesses.has("display_phone") && !(businesses.getString("display_phone").isEmpty())) {
                phone = businesses.getString("display_phone");
            }


            lstRestaurants.add(new Restaurant(name, address, categoryFinal, price, reviewCount + " Reviews", rating, roundedOfDistance + " mi", url, phone));
        }

        RestaurantAdapter restaurantAdapter = new RestaurantAdapter(this, lstRestaurants, this);
        ResRecyclerView.setAdapter(restaurantAdapter);
        ResRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onRestaurantClick(Restaurant restaurant, ImageView restaurantImageView) {
//        Toast.makeText(this, "item clicked: " + restaurant.getName(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, RestaurantDetail.class);
        intent.putExtra("name", restaurant.getName());
        intent.putExtra("detail_image", restaurant.getImage());
        intent.putExtra("detail_phone", restaurant.getPhone());
        intent.putExtra("detail_rating", String.valueOf(restaurant.getRating()));
        intent.putExtra("detail_reviewCount", restaurant.getReviewCount());

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                restaurantImageView, "sharedName");

        startActivity(intent, options.toBundle());


    }


}