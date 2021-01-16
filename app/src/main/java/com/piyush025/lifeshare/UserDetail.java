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

public class UserDetail extends AppCompatActivity {

    TextView cname,cbg,cmob,cdob,cgender,cemail;


    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        cname=(TextView)findViewById(R.id.cname);
        cbg=(TextView)findViewById(R.id.cbg);
        cmob=(TextView)findViewById(R.id.cmob);
        cdob=(TextView)findViewById(R.id.cdob);
        cgender=(TextView)findViewById(R.id.cgender);
        cemail=(TextView)findViewById(R.id.cemail);


        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String RegisteredUserID = currentUser.getUid();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                if (dataSnapshot.getKey().equals(RegisteredUserID)) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String gender = dataSnapshot.child("gender").getValue(String.class);
                    String bloodgroup = dataSnapshot.child("bloodgroup").getValue(String.class);
                    String dob = dataSnapshot.child("dob").getValue(String.class);
                    String mob = dataSnapshot.child("mob").getValue(String.class);
                    String email = dataSnapshot.child("emailID").getValue(String.class);

                    cname.setText(name);
                    cgender.setText(gender);
                    cbg.setText(bloodgroup);
                    cdob.setText(dob);
                    cmob.setText(mob);
                    cemail.setText(email);

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
