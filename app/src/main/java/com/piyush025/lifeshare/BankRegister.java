package com.piyush025.lifeshare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankRegister extends AppCompatActivity {

    TextView gotoBankLogin;
    EditText regName,regID,regMob,regEmail,regAddress,regPassword;
    Button register;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_register);

        mAuth = FirebaseAuth.getInstance();

        gotoBankLogin=(TextView)findViewById(R.id.gotobankLogin);
        regName=(EditText)findViewById(R.id.bankname);
        regID=(EditText)findViewById(R.id.bankid);
        regMob=(EditText)findViewById(R.id.bankmob);
        regEmail=(EditText)findViewById(R.id.regbankEmail);
        regAddress=(EditText)findViewById(R.id.regbankAddress);
        regPassword=(EditText)findViewById(R.id.regbankPassword);

        register=(Button)findViewById(R.id.registerbank);

        databaseReference= FirebaseDatabase.getInstance().getReference("Blood_Bank");

        progressDialog = new ProgressDialog(this);

        gotoBankLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BankRegister.this,Login.class);
                startActivity(intent);
            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerBank();
            }
        });
    }

    private void registerBank() {
        final String name = regName.getText().toString().trim();
        final String id = regID.getText().toString().trim();
        final String email = regEmail.getText().toString().trim();
        final String mob = regMob.getText().toString().trim();
        final String address = regAddress.getText().toString().trim();
        final String password = regPassword.getText().toString().trim();

        if (name.isEmpty()) {
            regName.setError("Name is Required!");
            regName.requestFocus();
            return;
        }

        if (id.isEmpty()) {
            regID.setError("Bank ID is Required!");
            regID.requestFocus();
            return;
        }

        if (mob.isEmpty()) {
            regMob.setError("Mobile Numeber is Required!");
            regMob.requestFocus();
            return;
        }

        String regx = "^[6789][0-9]{9}$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mob);
        if (!matcher.find()) {
            regMob.setError("Enter a Valid phone number!");
            regMob.requestFocus();
            return;
        }


        if (email.isEmpty()) {
            regEmail.setError("Email is Required!");
            regEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            regEmail.setError("Enter a valid email!");
            regEmail.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            regAddress.setError("Address is Required!");
            regAddress.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            regPassword.setError("Password is Required!");
            regPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            regPassword.setError("Password Length is too short!");
            regPassword.requestFocus();
            return;
        }


        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            //display some message here

                            try {
                                saveBankInfo(name, id, mob, email, address, password, "bank");
                                Thread.sleep(1000);
                                Toast.makeText(BankRegister.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BankRegister.this, Verification.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                Toast.makeText(BankRegister.this, "Internal Error Occured", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //display some message here
                            Toast.makeText(getApplicationContext(), "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });



    }

    private void saveBankInfo(String name,String id,String mob,String email,String address,String password,String type)
    {
        BankInformation bankInformation=new BankInformation(name,id,mob,email,address,password,type);
        FirebaseUser user=mAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(bankInformation);

    }
}
