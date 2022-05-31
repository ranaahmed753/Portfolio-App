package com.codingbaba.portfolioapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText,passwordEditText;
    private TextView loginButton;
    private FirebaseAuth mAuth;
    private ProgressDialog progress;
    private DatabaseReference mRef;
    private TextView signupText;
    private ConstraintLayout mConstraintLayout;
    private AdView mAdView;
    private AdRequest adRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        emailEditText = findViewById(R.id.emailID);
        passwordEditText = findViewById(R.id.passwordID);
        mConstraintLayout = findViewById(R.id.constraintLayout);

                
        signupText = findViewById(R.id.signup_textID);

        loginButton = findViewById(R.id.loginbuttonID);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loginProcess(email,password);
            }
        });
    }

    private void loginProcess(String email, String password)
    {
        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
        {
            emailEditText.setError("Email field are empty");
            passwordEditText.setError("Password field are empty");
        }else {
            login(email,password);
        }
    }

    private void login(String email, String password)
    {
        progress = new ProgressDialog(this,R.style.custom_style);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    mRef.child("isLogin").setValue("loggedIn");
                    progress.dismiss();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }else {
                    progress.dismiss();
                    emailEditText.setError("Invalid Email");
                    passwordEditText.setError("Invalid Password");
//
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mConstraintLayout.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
    }
}