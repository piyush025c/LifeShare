package com.piyush025.lifeshare;

import android.location.Address;
import android.location.Geocoder;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TabHost;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double lat,lang;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        //places = new ArrayList<String>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Blood_Bank");
        final Geocoder gc = new Geocoder(this);


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String address=dataSnapshot.child("address").getValue(String.class);

                if(address.isEmpty())
                    Log.e("TAG","problem");

                else
                    Log.e("TAG","OK" + address);

               // Log.e("TAG",String.valueOf(places.size())+ " child");
                //Log.e("TAG",places.get(0));

                //
                List<Address> list = null;
                try {
                    list = gc.getFromLocationName(address, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Address add = list.get(0);
                //String locality = add.getLocality();
                //Toast.makeText(this,locality,Toast.LENGTH_LONG).show();

                lat = add.getLatitude();
                lang = add.getLongitude();
                //
                Log.d("Tag", add.toString());
                //String str = "lat: " + String.valueOf(lat) + " long : " + String.valueOf(lang);
                //txt.setText(str);

                // Add a marker in Sydney and move the camera
                LatLng loc = new LatLng(lat, lang);
                mMap.addMarker(new MarkerOptions().position(loc).title("Marker in " + address));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                list.clear();
                //
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //


        //Log.e("TAG1",String.valueOf(places.size()));

        /*
        Geocoder gc = new Geocoder(this);
        for(int i=0;i<places.size();i++)
        {
            List<Address> list = null;
            try {
                list = gc.getFromLocationName(places.get(i), 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Address add = list.get(0);
            //String locality = add.getLocality();
            //Toast.makeText(this,locality,Toast.LENGTH_LONG).show();

            lat = add.getLatitude();
            lang = add.getLongitude();
            //
            Log.d("Tag", add.toString());
            //String str = "lat: " + String.valueOf(lat) + " long : " + String.valueOf(lang);
            //txt.setText(str);

            // Add a marker in Sydney and move the camera
            LatLng loc = new LatLng(lat, lang);
            mMap.addMarker(new MarkerOptions().position(loc).title("Marker in " + places.get(i)));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            list.clear();
        }

        */

        //set camera to coordinates of jaipur
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(26.9124, 75.7873));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(10);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

    }
}
