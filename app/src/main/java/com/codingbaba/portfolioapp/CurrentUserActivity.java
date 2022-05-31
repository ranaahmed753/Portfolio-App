package com.codingbaba.portfolioapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CurrentUserActivity extends AppCompatActivity {
    private CardView aboutCardView,servicesCardView,skillCardView,workExperienceCardView;
    private EditText aboutEditText,servicesEditText,skillEditText,workExperienceEditText;
    private TextView aboutSaveButton,aboutCancelButton;
    private TextView servicesTextView,dashboardTextView;
    private ConstraintLayout mConstraintLayout;
    private DatabaseReference aboutRef,serviceRef,skillRef,workRef,mRef;
    private FirebaseAuth mAuth;
    private CircleImageView currentImageView;
    private ImageButton backPressButton;
    private TextView workExperienceSaveButton,workExperienceCancelButton;
    private TextView skillSaveButton,skillCancelButton;
    private TextView servicesSaveButton,servicesCancelButton;
    private ImageView mBackButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_current_user);

        mAuth = FirebaseAuth.getInstance();
        aboutCardView = findViewById(R.id.about_cardID);
        servicesCardView = findViewById(R.id.services_cardID);
        skillCardView = findViewById(R.id.skills_cardID);
        skillCardView = findViewById(R.id.skills_cardID);
        workExperienceCardView = findViewById(R.id.work_experience_cardID);
        currentImageView = findViewById(R.id.portfolio_page_user_image_id);
        backPressButton = findViewById(R.id.back_button_id);
        dashboardTextView = findViewById(R.id.dashboardTextID);
        mConstraintLayout = findViewById(R.id.constraintLayout);

        backPressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                startActivity(new Intent(CurrentUserActivity.this,MainActivity.class));
                finish();
            }
        });


        mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentUserImage = snapshot.child("image").getValue().toString();
                    try {
                        if(currentUserImage.equals("default"))
                        {
                            currentImageView.setImageResource(R.drawable.avatar);
                        }else {

                            Glide.with(getApplicationContext()).load(currentUserImage).into(currentImageView);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        workExperienceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workExperienceCardView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                showWorkExperienceDialog();
            }
        });

        skillCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skillCardView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                showSkillDialog();
            }
        });

        servicesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicesCardView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                showServicesDialog();
            }
        });

        aboutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutCardView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                showAboutDialog();
            }
        });
    }


    private void showWorkExperienceDialog()
    {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.custom_work_experience_dialog);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        workExperienceEditText = bottomSheetDialog.findViewById(R.id.work_experience_edittextID);
        workExperienceSaveButton = bottomSheetDialog.findViewById(R.id.work_experience_save_buttonID);
        workExperienceCancelButton = bottomSheetDialog.findViewById(R.id.work_experience_cancel_buttonID);
        mBackButton = bottomSheetDialog.findViewById(R.id.backButtton);
        bottomSheetDialog.show();
        workExperienceSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workExperienceSaveButton.startAnimation(AnimationUtils.loadAnimation(CurrentUserActivity.this, android.R.anim.fade_in));
                ProgressDialog progressDialog = new ProgressDialog(CurrentUserActivity.this,R.style.custom_style);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();

                workRef = FirebaseDatabase.getInstance().getReference().child("Work").child(mAuth.getCurrentUser().getUid());
                HashMap<String,String> workHashmap = new HashMap<>();
                workHashmap.put("work",workExperienceEditText.getText().toString());
                if(TextUtils.isEmpty(workExperienceEditText.getText().toString()))
                {
                    progressDialog.dismiss();
                    workExperienceEditText.setError("field is empty");
                }else {
                    workRef.setValue(workHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                bottomSheetDialog.cancel();
                            }
                        }
                    });
                }

            }
        });

        workExperienceCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workExperienceCancelButton.startAnimation(AnimationUtils.loadAnimation(CurrentUserActivity.this, android.R.anim.fade_in));
                bottomSheetDialog.cancel();
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackButton.startAnimation(AnimationUtils.loadAnimation(CurrentUserActivity.this,R.anim.fade_in_animation));
                bottomSheetDialog.cancel();
            }
        });
    }

    private void showSkillDialog()
    {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.custom_skill_dialog);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();

        skillEditText = bottomSheetDialog.findViewById(R.id.skill_edittextID);
        skillSaveButton = bottomSheetDialog.findViewById(R.id.skill_save_buttonID);
        skillCancelButton = bottomSheetDialog.findViewById(R.id.skill_cancel_buttonID);
        mBackButton = bottomSheetDialog.findViewById(R.id.backButtton);

        skillSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(CurrentUserActivity.this,R.style.custom_style);
                progressDialog.setCancelable(false);
                progressDialog.show();

                skillRef = FirebaseDatabase.getInstance().getReference().child("Skill").child(mAuth.getCurrentUser().getUid());
                HashMap<String,String> skillHashmap = new HashMap<>();
                skillHashmap.put("skill",skillEditText.getText().toString());
                if(TextUtils.isEmpty(skillEditText.getText().toString()))
                {
                    progressDialog.dismiss();
                    skillEditText.setError("field is empty");
                }else {
                    skillRef.setValue(skillHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                bottomSheetDialog.cancel();
                            }
                        }
                    });
                }

            }
        });

        skillCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackButton.startAnimation(AnimationUtils.loadAnimation(CurrentUserActivity.this,R.anim.fade_in_animation));
                bottomSheetDialog.cancel();
            }
        });
    }

    private void showServicesDialog()
    {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.custom_services_dialog);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();

        servicesEditText = bottomSheetDialog.findViewById(R.id.services_edittextID);
        servicesTextView = bottomSheetDialog.findViewById(R.id.write_servicesText);
        servicesSaveButton = bottomSheetDialog.findViewById(R.id.services_save_buttonID);
        servicesCancelButton = bottomSheetDialog.findViewById(R.id.services_cancel_buttonID);
        mBackButton = bottomSheetDialog.findViewById(R.id.backButtton);

        servicesSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicesSaveButton.startAnimation(AnimationUtils.loadAnimation(CurrentUserActivity.this, android.R.anim.fade_in));
                ProgressDialog progressDialog = new ProgressDialog(CurrentUserActivity.this,R.style.custom_style);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();

                serviceRef = FirebaseDatabase.getInstance().getReference().child("Service").child(mAuth.getCurrentUser().getUid());
                HashMap<String,String> serviceHashmap = new HashMap<>();
                if(TextUtils.isEmpty(servicesEditText.getText().toString()))
                {
                    progressDialog.dismiss();
                    servicesEditText.setError("field is empty");
                }else{
                    serviceHashmap.put("service",servicesEditText.getText().toString());
                    serviceRef.setValue(serviceHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                bottomSheetDialog.cancel();
                            }
                        }
                    });
                }

            }
        });

        servicesCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicesCancelButton.startAnimation(AnimationUtils.loadAnimation(CurrentUserActivity.this, android.R.anim.fade_in));
                bottomSheetDialog.cancel();
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackButton.startAnimation(AnimationUtils.loadAnimation(CurrentUserActivity.this,R.anim.fade_in_animation));
                bottomSheetDialog.cancel();
            }
        });
    }

    private void showAboutDialog()
    {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.custom_about_dialog);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        aboutEditText = bottomSheetDialog.findViewById(R.id.about_edittextID);
        aboutSaveButton = bottomSheetDialog.findViewById(R.id.about_save_buttonID);
        aboutCancelButton = bottomSheetDialog.findViewById(R.id.about_cancel_buttonID);
        mBackButton = bottomSheetDialog.findViewById(R.id.backButtton);

        bottomSheetDialog.show();
        aboutSaveButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ProgressDialog progressDialog = new ProgressDialog(CurrentUserActivity.this,R.style.custom_style);
            progressDialog.setCancelable(false);
            progressDialog.show();

            aboutRef = FirebaseDatabase.getInstance().getReference().child("About").child(mAuth.getCurrentUser().getUid());
            HashMap<String,String> aboutHashmap = new HashMap<>();
            aboutHashmap.put("about",aboutEditText.getText().toString());
            if(TextUtils.isEmpty(aboutEditText.getText().toString()))
            {
                progressDialog.dismiss();
                aboutEditText.setError("field is empty");
            }else {
                aboutRef.setValue(aboutHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            bottomSheetDialog.cancel();
                        }
                    }
                });
            }


        }
    });

        aboutCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackButton.startAnimation(AnimationUtils.loadAnimation(CurrentUserActivity.this,R.anim.fade_in_animation));
                bottomSheetDialog.cancel();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CurrentUserActivity.this,MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mConstraintLayout.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
    }
}