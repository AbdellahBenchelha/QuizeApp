package com.example.quizapp.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.quizapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MapLocationActivity extends FragmentActivity implements OnMapReadyCallback {
    Location mlocation;
    FusedLocationProviderClient mfusedLocationProviderClient;
    Button btn_start_quiz;
    DatabaseReference mdatabaseref;
    FirebaseAuth firebaseAuth;
    private static final int Request_Cocde=101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);
        btn_start_quiz=findViewById(R.id.btn_start_quiz);
        mfusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        GetLastLocation();

        firebaseAuth = FirebaseAuth.getInstance();
        btn_start_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                String u_email=firebaseAuth.getCurrentUser().getEmail();
                String localisation="https://www.google.com/maps/place/"+mlocation.getLatitude()+","+mlocation.getLongitude();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                HashMap<String, Object> sendhashMap = new HashMap<>();
                sendhashMap.put("user_email", u_email);
                sendhashMap.put("localisation",localisation);
                sendhashMap.put("date", date);
                databaseReference.child("localisation").push().setValue(sendhashMap);

                startActivity(new Intent(MapLocationActivity.this, camra_activity.class));
            }
        });
    }

    private void GetLastLocation() {
        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Request_Cocde);
            return;
        }
        Task<Location> locationTask=mfusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mlocation=location;
                    double wayLatitude = location.getLatitude();
                    double wayLongitude = location.getLongitude();
                    Log.e("location",String.valueOf(wayLatitude)+","+String.valueOf(wayLongitude));
                    SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.current_location);
                    supportMapFragment.getMapAsync(MapLocationActivity.this);




                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng=new LatLng(mlocation.getLatitude(),mlocation.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("your position");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Request_Cocde:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    GetLastLocation();
                }
                break;
        }
    }
}
