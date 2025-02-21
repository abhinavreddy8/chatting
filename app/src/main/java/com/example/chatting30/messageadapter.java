package com.example.chatting30;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class messageadapter extends RecyclerView.Adapter<messageadapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;

    FirebaseUser fuser;

    public messageadapter(Context mContext, List<Chat> mChat, String imageurl) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public messageadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.itemright, parent, false);
            return new messageadapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.itemleft, parent, false);
            return new messageadapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull messageadapter.ViewHolder holder, int position) {

        Chat chat = mChat.get(position);

        holder.show_message.setText(chat.getMessage());

        if (imageurl.equals("default")) {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }

        if (position == mChat.size() - 1) {
            if (chat.isIsseen()) {
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Delivered");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }

        // Set listener for delete action
        /*holder.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                // Handle delete action here
                deleteMessage(position);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);

            // Set long click listener
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            // Show dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete Message");
            builder.setMessage("Are you sure you want to delete this message?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                       // listener.onDeleteClick(position);
                        deleteMessage(position);
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

            return true; // Consume the long click
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    // Interface for handling delete clicks
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    // Listener field
    //private OnDeleteClickListener listener;

    // Method to set the listener
    /*public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.listener = listener;
    }*/

    private void deleteMessage(int position) {
        // Remove the message from the list and notify adapter
        mChat.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mChat.size());
        // Perform additional actions as needed, e.g., delete from database
    }
}
