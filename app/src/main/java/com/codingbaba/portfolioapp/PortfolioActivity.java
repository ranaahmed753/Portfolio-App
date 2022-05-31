package com.codingbaba.portfolioapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class PortfolioActivity extends AppCompatActivity {
    private ImageView backButtonIcon;
    private CircleImageView mUserImage;
    private TextView mUserName,mUserProfession,mUserBio,mUserAbout;
    private ConstraintLayout mConstraintLayout,mWorkExperienceConstraintLayout,mServicesConstraintLayout,mSkillsConstraintLayout;
    private ConstraintLayout mFollowButton;
    private ImageView mHeartIcon;
    private TextView mServices,mSkills,mWorkExperience,mFollowText;
    private ImageView mServicesBackButton,mSkillBackButton,mWorkExperienceBackButton;
    private BottomSheetDialog bottomSheetDialog;
    private String userId;
    private DatabaseReference mUserRef,mAboutRef,mServicesRef,mSkillRef,mWorkExperienceRef;
    private String dummyBioText = "I love to code with android";
    private String dummyServiceText = "UI/UX design,graphic design,mockup design";
    private String dummySkillText = "adobe xd,adobe photoshop,adobe illustrator,canva.com,character design";
    private String dummyWorkExperienceText = "I'm fresher.I have no industry experience";
    private String dummyText = "Hi I am professional UI/UX designer.I have many professionals industry experience";
    private AdView mAdView;
    private AdRequest adRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_portfolio);
        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);
        userId = getIntent().getStringExtra("userID");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mAboutRef = FirebaseDatabase.getInstance().getReference().child("About").child(userId);
        mServicesRef = FirebaseDatabase.getInstance().getReference().child("Service").child(userId);
        mSkillRef = FirebaseDatabase.getInstance().getReference().child("Skill").child(userId);
        mWorkExperienceRef = FirebaseDatabase.getInstance().getReference().child("Work").child(userId);

        mConstraintLayout = findViewById(R.id.constraintLayout);
        mServicesConstraintLayout = findViewById(R.id.servicesConstraintLayout);
        mWorkExperienceConstraintLayout = findViewById(R.id.workExperienceConstraintLayout);
        mSkillsConstraintLayout = findViewById(R.id.skillsConstraintLayout);
        backButtonIcon = findViewById(R.id.backButtonIcon);
        mFollowButton = findViewById(R.id.followButton);
        mHeartIcon = findViewById(R.id.heartIcon);
        mUserImage = findViewById(R.id.userImage);
        mUserName = findViewById(R.id.userName);
        mUserProfession = findViewById(R.id.userProfession);
        mUserBio = findViewById(R.id.userBio);
        mUserAbout = findViewById(R.id.about);
        mFollowText = findViewById(R.id.followText);


        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = snapshot.child("name").getValue().toString();
                String image = snapshot.child("image").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String city = snapshot.child("city").getValue().toString();
                String country = snapshot.child("country").getValue().toString();
                String phone = snapshot.child("phone").getValue().toString();
                String profession = snapshot.child("profession").getValue().toString();
                String isLike = snapshot.child("isLike").getValue().toString();
                String isFollowing = snapshot.child("isFollowing").getValue().toString();
                String bio = snapshot.child("bio").getValue().toString();

                mUserName.setText(name);
                mUserProfession.setText(profession);
                mFollowText.setText(isFollowing);
                if(isLike.equals("Like")){
                    mHeartIcon.setImageResource(R.drawable.heart_outline);
                }else{
                    mHeartIcon.setImageResource(R.drawable.heart_filled);
                }
                if(image.equals("default")){
                    mUserImage.setImageResource(R.drawable.avatar);
                }else{
                    Glide.with(getApplicationContext()).load(image).into(mUserImage);
                }

                if(bio.equals("default")){
                    mUserBio.setText(dummyBioText);
                }else{
                    mUserBio.setText(bio);
                }

                mHeartIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mHeartIcon.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                        if(isLike.equals("Like")){
                            mUserRef.child("isLike").setValue("Liked");
                            //mHeartIcon.setImageResource(R.drawable.heart_filled);
                        }else{
                            mUserRef.child("isLike").setValue("Like");
                            //mHeartIcon.setImageResource(R.drawable.heart_outline);

                        }
                    }
                });

                mFollowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFollowButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in_animation));
                        if(isFollowing.equals("Follow")){
                            mUserRef.child("isFollowing").setValue("Following");
                            mFollowText.setText(isFollowing);
                        }else{
                            mUserRef.child("isFollowing").setValue("Follow");
                            mFollowText.setText(isFollowing);
                        }
                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mAboutRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String about = snapshot.child("about").getValue().toString();
                if(about.equals("default")){
                    mUserAbout.setText(dummyText);
                }else{
                    mUserAbout.setText(about);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        backButtonIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonIcon.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                startActivity(new Intent(PortfolioActivity.this,MainActivity.class));
                finish();
            }
        });


        mServicesConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mServicesConstraintLayout.setMinHeight(80);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PortfolioActivity.this);
                bottomSheetDialog.setContentView(R.layout.services);
                bottomSheetDialog.setCanceledOnTouchOutside(false);
                mServices = bottomSheetDialog.findViewById(R.id.services);
                mServicesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String service = snapshot.child("service").getValue().toString();
                        if(service.equals("default")){
                            mServices.setText(dummyServiceText);
                        }else{
                            mServices.setText(service);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                mServicesBackButton = bottomSheetDialog.findViewById(R.id.servicesBackButton);
                bottomSheetDialog.show();
                mServicesBackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mServicesBackButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                        bottomSheetDialog.cancel();
                    }
                });
            }
        });


        mWorkExperienceConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PortfolioActivity.this);
                bottomSheetDialog.setContentView(R.layout.work_experience);
                bottomSheetDialog.setCanceledOnTouchOutside(false);
                mWorkExperience = bottomSheetDialog.findViewById(R.id.work);
                mWorkExperienceBackButton = bottomSheetDialog.findViewById(R.id.workBackButton);

                mWorkExperienceRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String workexperience = snapshot.child("work").getValue().toString();
                        if(workexperience.equals("default")){
                            mWorkExperience.setText(dummyWorkExperienceText);
                        }else{
                           mWorkExperience.setText(workexperience);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                bottomSheetDialog.show();
                mWorkExperienceBackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWorkExperienceBackButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                        bottomSheetDialog.cancel();
                    }
                });
            }
        });


        mSkillsConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(PortfolioActivity.this);
                bottomSheetDialog.setContentView(R.layout.skills);
                bottomSheetDialog.setCanceledOnTouchOutside(false);
                mSkills = bottomSheetDialog.findViewById(R.id.skills);
                mSkillBackButton = bottomSheetDialog.findViewById(R.id.skillBackButton);

                mSkillRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String skill = snapshot.child("skill").getValue().toString();
                        if(skill.equals("default")){
                            mSkills.setText(dummySkillText);
                        }else{
                            mSkills.setText(skill);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                bottomSheetDialog.show();
                mSkillBackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSkillBackButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                        bottomSheetDialog.cancel();
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mConstraintLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left));
    }
}