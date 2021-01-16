package com.piyush025.lifeshare;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    TextView goToLogin,date;
    Button birth,register;
    int mYear,mMonth,mDay;
    EditText regName,regBloodgroup,regMob,regEmail,regPassword;
    RadioGroup rg;
    RadioButton rb;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        goToLogin=(TextView)findViewById(R.id.gotoLogin);
        birth=(Button)findViewById(R.id.dobbtn);
        register=(Button)findViewById(R.id.register);
        date=(TextView)findViewById(R.id.dob);
        regName=(EditText)findViewById(R.id.name);
        regBloodgroup=(EditText)findViewById(R.id.bloodgroup);
        regMob=(EditText)findViewById(R.id.mob);
        regEmail=(EditText)findViewById(R.id.regEmail);
        regPassword=(EditText)findViewById(R.id.regPassword);

        rg=(RadioGroup)findViewById(R.id.gender);

        databaseReference= FirebaseDatabase.getInstance().getReference("User");

        progressDialog = new ProgressDialog(this);


        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this,R.style.DialogTheme,
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

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();
            }
        });
    }

    private void registerUser()
    {
        final String name = regName.getText().toString().trim();
        final String bloodgroup = regBloodgroup.getText().toString().trim();
        final String dob = date.getText().toString().trim();
        final String email = regEmail.getText().toString().trim();
        final String mob=regMob.getText().toString().trim();
        final String password  = regPassword.getText().toString().trim();
        final String gender;

        if(name.isEmpty())
        {
            regName.setError("Name is Required!");
            regName.requestFocus();
            return;
        }

        if(bloodgroup.isEmpty())
        {
            regBloodgroup.setError("Blood Group is Required!");
            regBloodgroup.requestFocus();
            return;
        }

        Boolean t1=bloodgroup.equals("A+")||bloodgroup.equals("B+")||bloodgroup.equals("AB+")||bloodgroup.equals("O+");
        Boolean t2=bloodgroup.equals("A-")||bloodgroup.equals("B-")||bloodgroup.equals("AB-")||bloodgroup.equals("O-");

        if(!(t1||t2))
        {
            regBloodgroup.setError("Enter a Blood Group!");
            regBloodgroup.requestFocus();
            return;

        }

        if(mob.isEmpty())
        {
            regMob.setError("Mobile Numeber is Required!");
            regMob.requestFocus();
            return;
        }

        String regx = "^[6789][0-9]{9}$";
        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mob);
        if(!matcher.find())
        {
            regMob.setError("Enter a Valid phone number!");
            regMob.requestFocus();
            return;
        }

        int selectedId = rg.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(selectedId);
        if(selectedId==-1){
            Toast.makeText(this, "Please Select gender!", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            gender=rb.getText().toString().trim();
        }
        if(dob.isEmpty())
        {
            Toast.makeText(this, "Please Select a valid date of birth!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(email.isEmpty())
        {
            regEmail.setError("Email is Required!");
            regEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            regEmail.setError("Enter a valid email!");
            regEmail.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            regPassword.setError("Password is Required!");
            regPassword.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            regPassword.setError("Password Length is too short!");
            regPassword.requestFocus();
            return;
        }


       // Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here

                            try {
                                saveUserInfo(name,bloodgroup,mob,dob,email,password,gender,"user");
                                Thread.sleep(1000);
                                Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Verification.class);
                                startActivity(intent);
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(Register.this, "Internal Error Occured", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            //display some message here
                            Toast.makeText(getApplicationContext(),"Registration Error: " +task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });




    }

    private void saveUserInfo(String name,String bloodgroup,String mob,String dob,String email,String password,String gender,String type)
    {
        UserInformation userInformation=new UserInformation(name,bloodgroup,mob,dob,email,password,gender,type);
        FirebaseUser user=mAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(userInformation);

    }
}
