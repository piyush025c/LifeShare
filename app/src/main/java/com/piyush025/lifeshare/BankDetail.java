package com.piyush025.lifeshare;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BankDetail extends AppCompatActivity {

    TextView bname,bid,bmob,baddress,bemail;


    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_detail);


        bname=(TextView)findViewById(R.id.bname);
        bid=(TextView)findViewById(R.id.bid);
        bmob=(TextView)findViewById(R.id.bmob);
        baddress=(TextView)findViewById(R.id.baddress);
        bemail=(TextView)findViewById(R.id.bemail);


        databaseReference = FirebaseDatabase.getInstance().getReference("Blood_Bank");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String RegisteredUserID = currentUser.getUid();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                if (dataSnapshot.getKey().equals(RegisteredUserID)) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String id = dataSnapshot.child("id").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String mob = dataSnapshot.child("mob").getValue(String.class);
                    String email = dataSnapshot.child("emailID").getValue(String.class);

                    bname.setText(name);
                    bid.setText(id);
                    baddress.setText(address);
                    bmob.setText(mob);
                    bemail.setText(email);

                }


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


    }
}
