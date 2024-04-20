package com.example.easytolearn;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Registration_Screen extends AppCompatActivity {

    Button register;
    TextInputLayout signupname, signupemail, signuppassword, signupcnfpassword;
    ProgressDialog progressDialog;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    ImageView GoogleSigIn,img;
    Bitmap bitmap;
    Uri filepath;
    FirebaseAuth Auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);

        //finding id's
        signupname = findViewById(R.id.Reg_name);
        signupemail = findViewById(R.id.Reg_email);
        signuppassword = findViewById(R.id.Reg_password);
        signupcnfpassword = findViewById(R.id.Reg_cnf_password);
        progressDialog = new ProgressDialog(this);
        register = findViewById(R.id.Register_bt);
        GoogleSigIn = findViewById(R.id.Google_signIn);


        //Register Button code
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = signupname.getEditText().getText().toString();
                String email = signupemail.getEditText().getText().toString();
                String password = signuppassword.getEditText().getText().toString();
                String cnfpassword = signupcnfpassword.getEditText().getText().toString();


                //getting Instance
//
//                Auth = FirebaseAuth.getInstance();
//                user = Auth.getCurrentUser();

                if (!name.isEmpty()) {
                    signupname.setError(null);
                    signupname.setErrorEnabled(false);

                    if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        signupemail.setError(null);
                        signupemail.setErrorEnabled(false);

                        if (!password.isEmpty()) {
                            signuppassword.setError(null);
                            signuppassword.setErrorEnabled(false);

                            if (!cnfpassword.isEmpty()) {
                                signupcnfpassword.setError(null);
                                signupcnfpassword.setErrorEnabled(false);
                                if (email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                                    signupemail.setError(null);
                                    signupemail.setErrorEnabled(false);

                                    if (password.equals(cnfpassword)) {


                                        progressDialog.setMessage("please wait while SignUp....");
                                        progressDialog.setTitle("Signup");
                                        progressDialog.setCanceledOnTouchOutside(false);
                                        progressDialog.show();

                                        stored_data data = new stored_data(name, email, password, 0);
                                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = db.getReference("users");
                                        reference.child(name).setValue(data);
                                        progressDialog.dismiss();
                                        goTonextactivity();


//                                        Auth
//                                                .createUserWithEmailAndPassword(email, password)
//                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//
//
//                                                    @Override
//                                                     public void onComplete(@NonNull Task<AuthResult> task) {
//                                                        if (task.isSuccessful()) {
//                                                            goTonextactivity();
//
//                                                        } else {
//                                                            progressDialog.dismiss();
//                                                            Toast.makeText(Registration_Screen.this, " Failed...!", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    }
//
//                                                });

                                    } else {
                                        signupcnfpassword.setError("Password Not match");
                                    }


                                } else {
                                    signupemail.setError("Please Enter Valid Email");
                                }
                            } else {
                                signupcnfpassword.setError("Please Enter Conform Password");
                            }

                        } else {
                            signuppassword.setError("Please Enter Password");
                        }
                    } else {
                        signupemail.setError("Please Enter Valid Email");
                    }

                } else {
                    signupname.setError("Please Enter Your Name");
                }
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

        if (user != null)
            startActivity(new Intent(this, Home_Screen.class));


    }

    private void goTonextactivity() {

        progressDialog.dismiss();
        Intent intent = new Intent(Registration_Screen.this, Login_Screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }
}

