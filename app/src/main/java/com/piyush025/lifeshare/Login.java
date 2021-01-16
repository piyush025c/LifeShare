package com.piyush025.lifeshare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class Login extends AppCompatActivity {

    TextView newUser,bregister;

    EditText Lemail,Lpassword;

    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        newUser=(TextView)findViewById(R.id.newuser);
        Lemail=(EditText)findViewById(R.id.username);
        Lpassword=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.submit);
        bregister=(TextView)findViewById(R.id.bankRegister);

        progressDialog = new ProgressDialog(this);

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLogin();
            }
        });

        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Login.this,BankRegister.class);
                startActivity(intent);
            }
        });
    }

    private void userLogin()
    {
        String email = Lemail.getText().toString().trim();
        String pass  = Lpassword.getText().toString().trim();




        //checking if email and passwords are empty
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()==false){
            Toast.makeText(this,"Please enter a valid email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Logging in, Please Wait...");
        progressDialog.show();

        //logging in the user
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //if the task is successfull
                        if(task.isSuccessful()){
                            final FirebaseUser user=task.getResult().getUser();
                            //start the profile activity
                            //finish();
                            if(!user.isEmailVerified())
                            startActivity(new Intent(getApplicationContext(), Verification.class));

                            else
                            {
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                final String RegisteredUserID = currentUser.getUid();


                               // Toast.makeText(Login.this, RegisteredUserID, Toast.LENGTH_SHORT).show();

                                DatabaseReference jLoginDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(RegisteredUserID);


                                    jLoginDatabase.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                             Object type = dataSnapshot.child("type").getValue();

                                             progressDialog.dismiss();

                                             if(type==null)
                                             {
                                                 finish();
                                                 Intent intent = new Intent(Login.this, BankHome.class);
                                                 startActivity(intent);
                                             }

                                             else

                                             {
                                                 if (type.equals("user")) {

                                                     finish();
                                                     Intent intent = new Intent(Login.this, Home.class);
                                                     startActivity(intent);
                                                 }
                                             }



                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                            }
                        }

                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }
}
