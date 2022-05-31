package com.codingbaba.portfolioapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codingbaba.portfolioapp.viewholder.PortfolioViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;


import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private CircleImageView userCircleImageView;
    private DatabaseReference myRef;
    private FirebaseRecyclerOptions<Portfolio> options;
    private FirebaseRecyclerAdapter<Portfolio, PortfolioViewHolder> adapter;
    private TextView changePhotoButton,gotodashboardButton,logoutButton,editProfileButton;
    private ConstraintLayout mConstraintLayout;
    private static final int GALLERY_PIC = 1;
    private Uri imageUri;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;
    private ProgressBar mProgressbar;
    private Boolean isScrolling = false;
    private int currentItems,totalItems,scrollOutItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView = findViewById(R.id.recyclerviewID);
        userCircleImageView = findViewById(R.id.user_image_id);
        mConstraintLayout = findViewById(R.id.constraintLayout);
        mProgressbar = findViewById(R.id.scroolingProgressbarID);
        userCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCircleImageView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                circleImageDecisionMaking();
            }
        });
            myRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                       String image = snapshot.child("image").getValue().toString();
                        if(image.equals("default"))
                        {
                            userCircleImageView.setImageResource(R.drawable.avatar);
                        }else {
                            Glide.with(getApplicationContext()).load(image).into(userCircleImageView);
                        }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),"database operation failed!!",Toast.LENGTH_SHORT).show();
                }
            });
    }
    private void circleImageDecisionMaking()
    {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(R.layout.circle_image_decision);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        changePhotoButton = bottomSheetDialog.findViewById(R.id.change_photo_buttonID);
        gotodashboardButton = bottomSheetDialog.findViewById(R.id.goto_dashboard_buttonID);
        logoutButton = bottomSheetDialog.findViewById(R.id.logout_buttonID);
        editProfileButton = bottomSheetDialog.findViewById(R.id.edit_profile_buttonID);
        bottomSheetDialog.show();
        changePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhotoButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                updatePhoto();
            }
        });
        gotodashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotodashboardButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                startActivity(new Intent(MainActivity.this,CurrentUserActivity.class));
                finish();
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                myRef.child(mAuth.getCurrentUser().getUid()).child("isLogin").setValue("notLoggedIn");
                startActivity(new Intent(MainActivity.this,SignupActivity.class));
                finish();
            }
        });
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                startActivity(new Intent(MainActivity.this,EditProfileActivity.class));
                finish();
            }
        });
    }
    private void updatePhoto()
    {
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
                progressDialog = new ProgressDialog(MainActivity.this,R.style.custom_style);
                progressDialog.setCancelable(false);
                progressDialog.show();
                mStorageRef = FirebaseStorage.getInstance().getReference().child("PersonImage").child(mAuth.getCurrentUser().getUid());
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
                                        myRef.child(mAuth.getCurrentUser().getUid()).child("image").setValue(downloadImageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        mConstraintLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left));
        if(mAuth.getCurrentUser() == null)
        {
            startActivity(new Intent(MainActivity.this,SignupActivity.class));
            finish();
        }else{
            myRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String loginStatus = snapshot.child("isLogin").getValue().toString();
                    if(loginStatus.equals("notLoggedIn")){
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                    }else{
                        getUser();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    private void getUser()
    {
        options = new FirebaseRecyclerOptions.Builder<Portfolio>().setQuery(myRef,Portfolio.class).build();
        adapter = new FirebaseRecyclerAdapter<Portfolio, PortfolioViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PortfolioViewHolder holder, int position, @NonNull Portfolio model) {
                String userID = getRef(position).getKey();
                holder.cardView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in_animation));
                holder.userName.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
                holder.userProfessionName.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
                holder.userImage.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
                holder.userName.setText(model.getName());
                holder.userProfessionName.setText(model.getProfession());

                if(!model.getImage().equals("default"))
                {
                    Glide.with(getApplicationContext()).load(model.getImage()).into(holder.userImage);

                }else {
                    holder.userImage.setImageResource(R.drawable.avatar);
                }
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.cardView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                        Intent intent = new Intent(MainActivity.this,PortfolioActivity.class);
                        intent.putExtra("userID",userID);
                        startActivity(intent);
                        finish();
                    }
                });

            }

            @NonNull
            @Override
            public PortfolioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new PortfolioViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.child_item,parent,false));
            }
        };

        adapter.startListening();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        scrollListner();
    }

    private void scrollListner (){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
                if(isScrolling && !recyclerView.canScrollVertically(View.SCROLL_INDICATOR_BOTTOM))
                {
                    //&& (currentItems + scrollOutItems == totalItems)
                    isScrolling = false;
                    mProgressbar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressbar.setVisibility(View.GONE);
                        }
                    }, 800);


                }else{
                    isScrolling = true;
                    mProgressbar.setVisibility(View.GONE);
                }

            }
        });

    }
}