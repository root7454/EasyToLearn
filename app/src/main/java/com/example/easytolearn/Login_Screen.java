package com.example.easytolearn;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login_Screen extends AppCompatActivity {
    Button login;
    TextView signup,forget,admin;
    TextInputLayout logemail, logpass;
    ProgressDialog progressDialog;
    private FirebaseAuth Auth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    ImageView GoogleSigIn;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);



        login = findViewById(R.id.login_bt);
        forget = findViewById(R.id.forgetpass);
        signup = findViewById(R.id.signup_link);
        admin = findViewById(R.id.Admin_Login);
        logemail = findViewById(R.id.Log_email);
        logpass = findViewById(R.id.Log_password);
        progressDialog = new ProgressDialog(this);
        Auth = FirebaseAuth.getInstance();
        user = Auth.getCurrentUser();
        GoogleSigIn = findViewById(R.id.Google_signIn);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = logemail.getEditText().getText().toString();
                String password = logpass.getEditText().getText().toString();

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

//                            progressDialog.setMessage("please wait while SignIn....");
//                            progressDialog.setTitle("Login");
//                            progressDialog.setCanceledOnTouchOutside(false);
//                            progressDialog.show();
//                            Auth.signInWithEmailAndPassword(email, password)
//                                    .addOnCompleteListener(Login_Screen.this, new OnCompleteListener<AuthResult>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<AuthResult> task) {
//                                            if (task.isSuccessful()) {
//                                                goTonextactivity();
//                                            } else {
//                                                progressDialog.dismiss();
//                                                Toast.makeText(Login_Screen.this, "Login Failed...!", Toast.LENGTH_SHORT).show();
//
//                                            }
//                                        }
//                                    });
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                            Query checkemail = reference.orderByChild("email").equalTo(email);
                            checkemail.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        logemail.setError(null);
                                        Query checkpass = reference.orderByChild("password").equalTo(password);
                                        checkpass.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    logemail.setError(null);
                                                    goTonextactivity();
                                                }
                                                else {
                                                    progressDialog.dismiss();
                                                    logpass.setError("Invalid Credentials");
                                                    logpass.requestFocus();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    else {
                                        progressDialog.dismiss();
                                        logemail.setError("user not found");
                                        logemail.requestFocus();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

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

        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                startActivity(new Intent(Login_Screen.this,Registration_Screen.class));
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login_Screen.this,Admin_Login.class));
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login_Screen.this,Video_view.class));
            }
        });

        GoogleSigIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressDialog.setMessage("please wait while Google SignIn....");
                progressDialog.setTitle("GoogleSigIn");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                signIn();
            }
        });



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth
        Auth = FirebaseAuth.getInstance();

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = Auth.getCurrentUser();
        updateUI(currentUser);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }



    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        Auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = Auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void updateUI(FirebaseUser user) {

        if (user!=null){
            startActivity(new Intent(this, Home_Screen.class));

        }

    }

    private void goTonextactivity() {
        progressDialog.dismiss();
        Intent intent = new Intent(Login_Screen.this, Home_Screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }


}