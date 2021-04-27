package com.example.chatalllimitted;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentViewHolder extends RecyclerView.ViewHolder {

    CircleImageView profileImage;
    TextView username,comment;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage=itemView.findViewById(R.id.profileImage_comment);
        username=itemView.findViewById(R.id.usernameComment);
        comment=itemView.findViewById(R.id.commentsTv);

    }
}
