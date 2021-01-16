package com.piyush025.lifeshare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BloodCamp extends AppCompatActivity {

    EditText org,time,contact,email,address;
    TextView date;
    Button selectDate,generate;
    int mYear,mMonth,mDay;


    //Code for Notification

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA8hkH3D4:APA91bHwVyqGvgtLLZZBZmYs_TtHA2HX6BaLuCLfDVCMNwXn91tYQABCJ05ytVIwQjCk8zh1wedbveUcU9lim_elTneqmT8WMtAniCR3WpI0GG2qi29DwpXeo7sZ_0Jvz8Nu94g43USW";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;


    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_camp);

        org=(EditText)findViewById(R.id.org);
        time=(EditText)findViewById(R.id.time);
        contact=(EditText)findViewById(R.id.contact);
        email=(EditText)findViewById(R.id.campemail);
        address=(EditText)findViewById(R.id.address);

        date=(TextView)findViewById(R.id.date);

        selectDate=(Button)findViewById(R.id.datebtn);
        generate=(Button)findViewById(R.id.generate);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(BloodCamp.this,R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = org.getText().toString().trim();
                String eventTime = time.getText().toString().trim();
                String eventDate = date.getText().toString().trim();
                String contactDetail = contact.getText().toString().trim();
                String  emailID= email.getText().toString().trim();
                String eventAddress= address.getText().toString().trim();

                if(name.isEmpty())
                {
                    org.setError("Enter Parent Organization Name!");
                    org.requestFocus();
                    return;
                }

                if(eventTime.isEmpty())
                {
                    time.setError("Enter Event Time!");
                    time.requestFocus();
                    return;
                }

                if(eventDate.isEmpty())
                {
                    Toast.makeText(BloodCamp.this, "Enter Event Date!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(contactDetail.isEmpty())
                {
                    contact.setError("Enter Contact Number!");
                    contact.requestFocus();
                    return;
                }

                String regx = "^[6789][0-9]{9}$";
                Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(contactDetail);
                if(!matcher.find())
                {
                    contact.setError("Enter a Valid phone number!");
                    contact.requestFocus();
                    return;
                }

                if(emailID.isEmpty())
                {
                    email.setError("Email is Required!");
                    email.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(emailID).matches())
                {
                    email.setError("Enter a valid email!");
                    email.requestFocus();
                    return;
                }

                if(eventAddress.isEmpty())
                {
                    email.setError("Enter Event Address!");
                    email.requestFocus();
                    return;
                }

                //
                TOPIC="/topics/user";
                NOTIFICATION_TITLE = "Blood Camp Notice";
                NOTIFICATION_MESSAGE =name+" is organising a Blood Camp on "+eventDate+","+eventTime+" at "+eventAddress+"."+"Contact:"+contactDetail+",Email:"+emailID;

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

                Toast.makeText(BloodCamp.this, "Event Created!", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(BloodCamp.this,BankHome.class);
                startActivity(intent);


            }
        });


    }


    private void sendNotification(JSONObject notification) {
        Log.e("TAG","Send Notification");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(BloodCamp.this, "", Toast.LENGTH_SHORT).show();
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

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }
}
