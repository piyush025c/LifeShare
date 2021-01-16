package com.piyush025.lifeshare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Donate extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String> name1 = new ArrayList<>();
    ArrayList<String> address1 = new ArrayList<>();
    ArrayList<String> email1 = new ArrayList<>();
    MyListAdapter arrayAdapter;
    ListView listView;

    ArrayList<String> key= new ArrayList<>();;

    public Donate()
    { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.donate,container,false);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Blood_Bank");
        listView = (ListView)v.findViewById(R.id.lv);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String name=dataSnapshot.child("name").getValue(String.class);
                String address=dataSnapshot.child("address").getValue(String.class);
                String email=dataSnapshot.child("emailID").getValue(String.class);

                key.add(dataSnapshot.getKey());
                name1.add(name);
                address1.add(address);
                email1.add(email);
                arrayAdapter = new MyListAdapter(getActivity(),name1,address1,email1);
                listView.setAdapter(arrayAdapter);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
               // key.get(position);

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                final String RegisteredUserID = currentUser.getUid();
                //final String ;

                DatabaseReference jLoginDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(RegisteredUserID);

                jLoginDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final String name= dataSnapshot.child("name").getValue().toString();
                        final String bg= dataSnapshot.child("bloodgroup").getValue().toString();
                        final String gender= dataSnapshot.child("gender").getValue().toString();
                        final String email= dataSnapshot.child("emailID").getValue().toString();
                        final String mob= dataSnapshot.child("mob").getValue().toString();
                        final String dob= dataSnapshot.child("dob").getValue().toString();

                        final String bankID=key.get(position);

                        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setTitle("Conformation");
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("Your Personal details will be shared with the bank, are you sure you want to continue?");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        saveDonorDetail(name,bg,email,mob,bankID,gender,dob,RegisteredUserID);
                                        dialog.dismiss();

                                        Intent intent=new Intent(getContext(),Thankyou.class);
                                        startActivity(intent);
                                    }
                                });
                        alertDialog.show();





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return v;
    }

    public void saveDonorDetail(String name, String bg, String email, String mob, String bankID, String gender, String dob,String donor) {
        DonorInformation donorInformation=new DonorInformation(name,bg,email,mob,bankID,gender,dob,donor);


        DatabaseReference df=FirebaseDatabase.getInstance().getReference("DonorDetail").push();
        df.setValue(donorInformation);
    }
}
