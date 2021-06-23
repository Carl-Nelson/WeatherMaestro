package com.hfad.weathermaestro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.os.HandlerCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Tasks;
import com.hfad.models.CurrentWeather;
import com.hfad.models.UserLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_CODE = 100;
    // thread stuff
    private final ExecutorService executor = Executors.newFixedThreadPool(1);
    private final Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
    // location stuff
    private FusedLocationProviderClient fusedLocationClient;
    private Location geolocation;
    private final LocationRequest locationRequest = LocationRequest.create();
    // callback method for receiving location updates
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) { // ignore the dumb annotation warning
            if (locationResult == null) {
                System.out.println("No location data");
                return;
            }
            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    geolocation = location;
                    //System.out.println("Received location update");
                    return;
                }
            }
        }
    };
    //DB stuff
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get location provider from Google Play services to make requests
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Configure request
        locationRequest.setInterval(50000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Ask for location permission if needed
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        } else {
            // Request regular location updates
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

            System.out.println("Requested location updates.");
        }
        // Event listener for the search view
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                UserLocation location = new UserLocation();
                location.city = query;
                getWeather(location);

                return true;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        };
        SearchView searchView = findViewById(R.id.fab);
        searchView.setOnQueryTextListener(queryTextListener);

        // Initialize the database
        SQLiteOpenHelper databaseHelper = new WeatherMaestroDatabaseHelper(this);
        try {
            db = databaseHelper.getReadableDatabase();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this,"Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWeather(new UserLocation());
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }

    public void onButtonClick(View view) {
        getWeather(new UserLocation());
    }

    /**
     * Updates the weather display
     *
     * @param weather   CurrentWeather object deserialized from the API response
     */
    private void displayWeather(CurrentWeather weather) {
        if (weather == null) {
            System.out.println("Failed to retrieve weather.");
        } else {
            cursor = db.query("Drawables", new String[]{"Drawable"},"Icon = ?",new String[]{weather.weather.get(0).icon},null,null,null);
            if (cursor.moveToFirst()) {
                ImageView iconView = findViewById(R.id.weather_icon);
                iconView.setImageResource(Integer.parseInt(cursor.getString(0)));
            }
            TextView temp = findViewById(R.id.temperature);
            temp.setText(String.format(Locale.getDefault(),"%.0f",weather.main.temp));
            TextView city = findViewById(R.id.location);
            city.setText(weather.name);
            TextView condition = findViewById(R.id.weather_condition);
            condition.setText(weather.weather.get(0).main);
        }
    }

    /**
     * Asynchronous method that gets the current weather for the user's location
     * and calls displayWeather() to update the UI display. Technically it tells the handler to
     * call displayWeather()...
     *
     * @param location  UserLocation object containing whatever location data is available
     */
    private void getWeather(UserLocation location) {
        // TODO: Update this to get location from database if available, also get units
        executor.execute(() -> {

            Location geolocation = getLocation();
            if (geolocation != null) {
                location.latitude = Double.toString(geolocation.getLatitude());
                location.longitude = Double.toString(geolocation.getLongitude());
            } else {
                System.out.println("Could not retrieve location.");
            }
            // just gonna do imperial units by default for now
            CurrentWeather weather = ApiService.getCurrentWeather(location, ApiService.UNITS_IMPERIAL);

            handler.post(() -> displayWeather(weather));
        });

    }
    /**
     *  Returns a Location object with the longitude and latitude of the user's last known location.
     *  (important methods for Location: getLongitude(), getLatitude())
     *
     *  @return                 Location object containing coordinates, or null in case of failure.
     */
    @Nullable
    private Location getLocation() { //TODO: Add the ability to get location from settings

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);

            return null;
        }

        if (geolocation != null) {
            return geolocation;
        }
        try {
            return Tasks.await(fusedLocationClient.getLastLocation());
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Encountered an exception while retrieving location from the fused location client.");
        }

        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String[] permissions,
                                           @NotNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            }
        } else {
            Toast.makeText(MainActivity.this, "Location Permission Denied", Toast.LENGTH_SHORT) .show();
        }
    }

    public void goToSettings(View view) {
        startActivity(new Intent(MainActivity.this, Settings.class));
    }
}