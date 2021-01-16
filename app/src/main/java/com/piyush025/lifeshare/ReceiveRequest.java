package com.piyush025.lifeshare;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReceiveRequest extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String> name1 = new ArrayList<>();
    ArrayList<String> bg1 = new ArrayList<>();
    ArrayList<String> gender1 = new ArrayList<>();
    ArrayList<String> dob1 = new ArrayList<>();
    ArrayList<String> mob1 = new ArrayList<>();
    ArrayList<String> email1 = new ArrayList<>();

    ArrayList<String> idRequest=new ArrayList<>();


    //Code for Notification
    ArrayList<String> user1 = new ArrayList<>();
    //


    MyListAdapter1 arrayAdapter;
    ListView listView;

    //Code for Notification

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA8hkH3D4:APA91bHwVyqGvgtLLZZBZmYs_TtHA2HX6BaLuCLfDVCMNwXn91tYQABCJ05ytVIwQjCk8zh1wedbveUcU9lim_elTneqmT8WMtAniCR3WpI0GG2qi29DwpXeo7sZ_0Jvz8Nu94g43USW";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;


    //

    public ReceiveRequest()
    { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.receive_request,container,false);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("ReceiverDetail");
        listView = (ListView)v.findViewById(R.id.lv3);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String RegisteredUserID = currentUser.getUid();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String BankID=dataSnapshot.child("bankID").getValue(String.class);

                if(BankID.equals(RegisteredUserID)) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String gender = dataSnapshot.child("gender").getValue(String.class);
                    String bloodgroup = dataSnapshot.child("bloodgroup").getValue(String.class);
                    String dob = dataSnapshot.child("dob").getValue(String.class);
                    String mob = dataSnapshot.child("mob").getValue(String.class);
                    String email = dataSnapshot.child("emailID").getValue(String.class);

                    //Code for Notification
                    String user = dataSnapshot.child("userID").getValue(String.class);
                    //

                    name1.add(name);
                    gender1.add(gender);
                    bg1.add(bloodgroup);
                    email1.add(email);
                    dob1.add(dob);
                    mob1.add(mob);

                    //Code for Notification
                    user1.add(user);
                    //

                    idRequest.add(dataSnapshot.getKey());

                    arrayAdapter = new MyListAdapter1(getActivity(),name1,gender1,bg1,dob1,mob1,email1);
                    listView.setAdapter(arrayAdapter);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final int pos=position;  //

                final String key=idRequest.get(pos); //
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Manage Request");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Blood Units Available?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //
                                TOPIC="/topics/"+user1.get(pos);
                                NOTIFICATION_TITLE = "Response from Blood Bank.";
                                NOTIFICATION_MESSAGE = "Congrats! Requested  Bloodgroup is available";

                                JSONObject notification = new JSONObject();
                                JSONObject notifcationBody = new JSONObject();
                                try {
                                    notifcationBody.put("title", NOTIFICATION_TITLE);
                                    notifcationBody.put("message", NOTIFICATION_MESSAGE);

                                    notification.put("to", TOPIC);
                                    notification.put("data", notifcationBody);
                                } catch (JSONException e) {
                                    Log.e(TAG, "onCreate: " + e.getMessage());
                                }
                                sendNotification(notification);

                                //

                                FirebaseDatabase.getInstance().getReference().child("ReceiverDetail").child(key).removeValue();
                                name1.remove(pos);
                                gender1.remove(pos);
                                bg1.remove(pos);
                                email1.remove(pos);
                                dob1.remove(pos);
                                mob1.remove(pos);
                                idRequest.remove(pos);


                                arrayAdapter = new MyListAdapter1(getActivity(),name1,gender1,bg1,dob1,mob1,email1);
                                listView.setAdapter(arrayAdapter);
                               // Toast.makeText(getActivity(), "You Clicked Accept "+pos, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), "Request Managed.", Toast.LENGTH_SHORT).show();

                                dialog.dismiss();

                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //
                                TOPIC="/topics/"+user1.get(pos);
                                NOTIFICATION_TITLE = "Response from Blood Bank.";
                                NOTIFICATION_MESSAGE = "Sorry! Requested Bloodgroup is unavailable.";

                                JSONObject notification = new JSONObject();
                                JSONObject notifcationBody = new JSONObject();
                                try {
                                    notifcationBody.put("title", NOTIFICATION_TITLE);
                                    notifcationBody.put("message", NOTIFICATION_MESSAGE);

                                    notification.put("to", TOPIC);
                                    notification.put("data", notifcationBody);
                                } catch (JSONException e) {
                                    Log.e(TAG, "onCreate: " + e.getMessage());
                                }
                                sendNotification(notification);

                                //

                                FirebaseDatabase.getInstance().getReference().child("ReceiverDetail").child(key).removeValue();
                                //Toast.makeText(getActivity(), "You Clicked Decline "+pos, Toast.LENGTH_SHORT).show();

                                name1.remove(pos);
                                gender1.remove(pos);
                                bg1.remove(pos);
                                email1.remove(pos);
                                dob1.remove(pos);
                                mob1.remove(pos);
                                idRequest.remove(pos);


                                arrayAdapter = new MyListAdapter1(getActivity(),name1,gender1,bg1,dob1,mob1,email1);
                                listView.setAdapter(arrayAdapter);


                                Toast.makeText(getActivity(), "Request Managed.", Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
        });


        //

        return v;
    }

    private void sendNotification(JSONObject notification) {
        Log.e("TAG","Send Notification");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("TAG","response: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG","error");

            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }



        };

        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }
}
