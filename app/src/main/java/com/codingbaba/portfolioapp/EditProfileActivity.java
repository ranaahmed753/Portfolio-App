package com.codingbaba.portfolioapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
   private ConstraintLayout mConstraintLayout,mNameConstraintLayout,mEmailConstraintLayout,mPhoneConstraintLayout,mProfessionConstraintLayout,mBioConstraintLayout,mCityConstraintLayout,mCountryConstraintLayout,mDeleAccountConstraintLayout;
   private ImageView mBackButtonIcon;
   private TextView mChangePhotoText,mUserName,mUserEmail,mUserPhone,mUserProfession,mUserBio,mUserCity,mUserCountry;
   private DatabaseReference mUserRef;
   private FirebaseAuth mUserAuth;
   private CircleImageView mUserImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_profile);

        mUserAuth = FirebaseAuth.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserAuth.getCurrentUser().getUid());

        mConstraintLayout = findViewById(R.id.constraintLayout);
        mNameConstraintLayout = findViewById(R.id.nameConstraintLayout);
        mEmailConstraintLayout = findViewById(R.id.emailConstraintLayout);
        mPhoneConstraintLayout = findViewById(R.id.phoneConstraintLayout);
        mProfessionConstraintLayout = findViewById(R.id.professionConstraintLayout);
        mBioConstraintLayout = findViewById(R.id.bioConstraintLayout);
        mCityConstraintLayout = findViewById(R.id.cityConstraintLayout);
        mCountryConstraintLayout = findViewById(R.id.countryConstraintLayout);
        mDeleAccountConstraintLayout = findViewById(R.id.deleteAccountConstraintLayout);
        mUserImage = findViewById(R.id.userImage);
        mUserName = findViewById(R.id.userName);
        mUserEmail = findViewById(R.id.userEmail);
        mUserPhone = findViewById(R.id.userPhone);
        mUserProfession = findViewById(R.id.userProfession);
        mUserBio = findViewById(R.id.userBio);
        mUserCity = findViewById(R.id.userCity);
        mUserCountry = findViewById(R.id.userCountry);


        mBackButtonIcon = findViewById(R.id.backButtonIcon);

        mChangePhotoText = findViewById(R.id.changePhotoText);

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String image = snapshot.child("image").getValue().toString();
                String name = snapshot.child("name").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String phone = snapshot.child("phone").getValue().toString();
                String profession = snapshot.child("profession").getValue().toString();
                String bio = snapshot.child("bio").getValue().toString();
                String city = snapshot.child("city").getValue().toString();
                String country = snapshot.child("country").getValue().toString();

                mUserName.setText(name);
                mUserEmail.setText(email);
                mUserPhone.setText(phone);
                mUserProfession.setText(profession);
                if(bio.equals("default")){
                    mUserBio.setText("xxyyzz");
                }else{
                    mUserBio.setText(bio);
                }
                mUserCity.setText(city);
                mUserCountry.setText(country);

                if(image.equals("default")){
                    mUserImage.setImageResource(R.drawable.avatar);
                }else{
                    Glide.with(getApplicationContext()).load(image).into(mUserImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mChangePhotoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChangePhotoText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                updatePhoto();
            }
        });

        mBackButtonIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackButtonIcon.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                startActivity(new Intent(EditProfileActivity.this,MainActivity.class));
                finish();
            }
        });

        mNameConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mNameEditText;
                TextView mCancelText,mSaveText;
                Dialog dialog = new Dialog(EditProfileActivity.this);
                dialog.setContentView(R.layout.edit_name);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                mNameEditText = dialog.findViewById(R.id.editName);
                mCancelText = dialog.findViewById(R.id.cancelText);
                mSaveText = dialog.findViewById(R.id.saveText);
                dialog.setCancelable(false);
                dialog.show();
                mCancelText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCancelText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                        dialog.cancel();
                    }
                });
                mSaveText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSaveText.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, android.R.anim.fade_in));
                        ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this,R.style.custom_style);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        mUserRef.child("name").setValue(mNameEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.cancel();
                                    dialog.cancel();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                dialog.cancel();
                            }
                        });
                    }
                });
            }
        });

        mEmailConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mPhoneConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mProfessionConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mProfessionEditText;
                TextView mCancelText,mSaveText;
                Dialog dialog = new Dialog(EditProfileActivity.this);
                dialog.setContentView(R.layout.edit_profession);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                mProfessionEditText = dialog.findViewById(R.id.editProfession);
                mCancelText = dialog.findViewById(R.id.cancelText);
                mSaveText = dialog.findViewById(R.id.saveText);
                dialog.setCancelable(false);
                dialog.show();
                mCancelText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCancelText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                        dialog.cancel();
                    }
                });
                mSaveText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSaveText.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, android.R.anim.fade_in));
                        ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this,R.style.custom_style);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        mUserRef.child("profession").setValue(mProfessionEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.cancel();
                                    dialog.cancel();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                dialog.cancel();
                            }
                        });
                    }
                });
            }
        });

        mBioConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mBioEditText;
                TextView mCancelText,mSaveText;
                Dialog dialog = new Dialog(EditProfileActivity.this);
                dialog.setContentView(R.layout.edit_bio);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                mBioEditText = dialog.findViewById(R.id.editBio);
                mCancelText = dialog.findViewById(R.id.cancelText);
                mSaveText = dialog.findViewById(R.id.saveText);
                dialog.setCancelable(false);
                dialog.show();
                mCancelText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCancelText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                        dialog.cancel();
                    }
                });

                mSaveText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSaveText.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, android.R.anim.fade_in));
                        ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this,R.style.custom_style);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        mUserRef.child("bio").setValue(mBioEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.cancel();
                                    dialog.cancel();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                dialog.cancel();
                            }
                        });
                    }
                });
            }
        });

        mCityConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mCountryConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mDeleAccountConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutAccount();
            }
        });



    }

    private void logoutAccount() {
        mUserRef.child("isLogin").setValue("notLoggedIn");
    }

    private void updatePhoto(){
    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
    galleryIntent.setType("image/*");
    startActivityForResult(galleryIntent,1);
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 )
        {
            if(resultCode == RESULT_OK)
            {
                ProgressDialog progressDialog;
                StorageReference mStorageRef;
                Uri imageUri;
                progressDialog = new ProgressDialog(EditProfileActivity.this,R.style.custom_style);
                progressDialog.setCancelable(false);
                progressDialog.show();
                mStorageRef = FirebaseStorage.getInstance().getReference().child("PersonImage").child(mUserAuth.getCurrentUser().getUid());
                imageUri = data.getData();
                if(imageUri != null)
                {
                    StorageReference filePath = mStorageRef.child(imageUri.getLastPathSegment());
                    final UploadTask uploadTask = filePath.putFile(imageUri);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Error:-"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                                    if(!task.isSuccessful())
                                    {

                                        throw task.getException();
                                    }
                                    return filePath.getDownloadUrl();

                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    if(task.isSuccessful())
                                    {
                                        Uri downloadUri = task.getResult();
                                        String downloadImageUrl=  downloadUri.toString();
                                        mUserRef.child("image").setValue(downloadImageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(),"image updated",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                }
                            });
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mConstraintLayout.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditProfileActivity.this,MainActivity.class));
        finish();
    }


}