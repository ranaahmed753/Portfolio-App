package com.codingbaba.portfolioapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private EditText nameEditText,phoneEditText,emailEditText,passwordEditText,countryEdittext,cityEdittext,professionEdittext;
    private TextView signupButton;
    private FirebaseAuth mAuth;
    private ProgressDialog progress;
    private DatabaseReference mRef,aboutRef,serviceRef,skillRef,workRef;
    private TextView loginText;
    private ConstraintLayout mConstraintLayout;
    private AdView mAdView;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        nameEditText = findViewById(R.id.nameID);
        phoneEditText = findViewById(R.id.phoneID);
        emailEditText = findViewById(R.id.emailID);
        passwordEditText = findViewById(R.id.passwordID);
        countryEdittext = findViewById(R.id.countryID);
        cityEdittext = findViewById(R.id.cityID);
        professionEdittext = findViewById(R.id.professionID);
        loginText = findViewById(R.id.login_textID);
        mConstraintLayout = findViewById(R.id.constraintLayout);

        signupButton = findViewById(R.id.signupbuttonID);
        mAuth = FirebaseAuth.getInstance();


        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                String  name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String country = countryEdittext.getText().toString();
                String city = cityEdittext.getText().toString();
                String profession = professionEdittext.getText().toString();

                signupProcess(name,phone,email,password,country,city,profession);

            }
        });
    }

    private void signupProcess(String name, String phone, String email, String password,String country,String city,String profession)
    {
        if(TextUtils.isEmpty(name) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(country) && TextUtils.isEmpty(city) && TextUtils.isEmpty(profession))
        {

            nameEditText.setError("Email field are empty");
            phoneEditText.setError("Phone field are empty");
            emailEditText.setError("Email field are empty");
            passwordEditText.setError("Password field are empty");
            countryEdittext.setError("Country field are empty");
            cityEdittext.setError("City field are empty");
            professionEdittext.setError("Profession field are empty");
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("wrong email pattern");
        }else if(!android.util.Patterns.PHONE.matcher(phone).matches()){
            phoneEditText.setError("invalid phone number");
        }else if(password.length()<6){
            passwordEditText.setError("password must be 7 or above ");
        }else {
            createUser(name,phone,email,password,country,city,profession);

        }
    }

    private void createUser(String name,String phone,String email, String password,String country,String city,String profession)
    {
        progress = new ProgressDialog(this,R.style.custom_style);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                    aboutRef = FirebaseDatabase.getInstance().getReference().child("About");
                    HashMap<String,String> aboutMap = new HashMap<>();
                    aboutMap.put("about","default");
                    aboutRef.child(mAuth.getCurrentUser().getUid()).setValue(aboutMap);

                    serviceRef = FirebaseDatabase.getInstance().getReference().child("Service");
                    HashMap<String,String> serviceMap = new HashMap<>();
                    serviceMap.put("service","default");
                    serviceRef.child(mAuth.getCurrentUser().getUid()).setValue(serviceMap);

                    skillRef = FirebaseDatabase.getInstance().getReference().child("Skill");
                    HashMap<String,String> skillMap = new HashMap<>();
                    skillMap.put("skill","default");
                    skillRef.child(mAuth.getCurrentUser().getUid()).setValue(skillMap);

                    workRef = FirebaseDatabase.getInstance().getReference().child("Work");
                    HashMap<String,String> workMap = new HashMap<>();
                    workMap.put("work","default");
                    workRef.child(mAuth.getCurrentUser().getUid()).setValue(workMap);

                    mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("name",name);
                    hashMap.put("image","default");
                    hashMap.put("phone",phone);
                    hashMap.put("email",email);
                    hashMap.put("password",password);
                    hashMap.put("country",country);
                    hashMap.put("city",city);
                    hashMap.put("profession",profession);
                    hashMap.put("isLike","Like");
                    hashMap.put("isFollowing","Follow");
                    hashMap.put("bio","default");
                    hashMap.put("isLogin","notLoggedIn");

                    mRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                progress.dismiss();
                                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                                finish();
                            }
                        }
                    });



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