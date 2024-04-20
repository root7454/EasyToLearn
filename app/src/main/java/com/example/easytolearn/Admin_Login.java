package com.example.easytolearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Admin_Login extends AppCompatActivity {
    TextInputLayout logemail, logpass;
    ProgressDialog progressDialog;
    Button login;
    private FirebaseAuth Auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);

        login = findViewById(R.id.login_bt);
        logemail = findViewById(R.id.Log_email);
        logpass = findViewById(R.id.Log_password);
        progressDialog = new ProgressDialog(this);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = logemail.getEditText().getText().toString();
                String password = logpass.getEditText().getText().toString();
                Auth = FirebaseAuth.getInstance();
                user = Auth.getCurrentUser();

                if (!email.isEmpty()) {
                    logemail.setError(null);
                    logemail.setErrorEnabled(false);
                    if (!password.isEmpty()) {
                        logpass.setError(null);
                        logpass.setErrorEnabled(false);
                        if (email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                            progressDialog.setMessage("please wait while SignIN....");
                            progressDialog.setTitle("SignIn");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            Auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(Admin_Login.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                goTonextactivity();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(Admin_Login.this, "Login Failed...!", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                        }
                        else {
                            logemail.setError("Enter Valid Email");
                            progressDialog.dismiss();
                        }
                    } else {
                        logpass.setError("Enter Password");
                    }
                } else {
                    logemail.setError("Enter Email");
                }
            }
        });


    }

    private void goTonextactivity() {
        progressDialog.dismiss();
        Intent intent = new Intent(Admin_Login.this, Dashbord.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}