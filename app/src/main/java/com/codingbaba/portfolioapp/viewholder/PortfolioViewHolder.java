package com.codingbaba.portfolioapp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.codingbaba.portfolioapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PortfolioViewHolder extends RecyclerView.ViewHolder {
    public TextView userName,userProfessionName;
    public CircleImageView userImage;
    public ConstraintLayout cardView;
    public PortfolioViewHolder(@NonNull View itemView)
    {
        super(itemView);

        userName = itemView.findViewById(R.id.user_nameID);
        userProfessionName = itemView.findViewById(R.id.user_professionID);
        userImage = itemView.findViewById(R.id.user_image_id);
        cardView = itemView.findViewById(R.id.cardviewID);


    }
}
