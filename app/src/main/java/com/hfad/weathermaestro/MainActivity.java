package com.hfad.weathermaestro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.hfad.models.Coord;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /**
     *  Returns a Location object with the longitude and latitude of the user's last known location.
     *  (important methods for Location: getLongitude(), getLatitude())
     *
     *  @param locationClient   FusedLocationProviderClient to use to get the location.
     *  @return                 Location object containing coordinates, or null in case of failure.
     */
    @Nullable
    public Location getLocation(@NotNull FusedLocationProviderClient locationClient) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        try {

            return Tasks.await(fusedLocationClient.getLastLocation());

        } catch (ExecutionException | InterruptedException e) {
            // if this were a commercial release or something,
            // printing the stack trace would probably be a bad move
            e.printStackTrace();
        }

        return null;
    }
}