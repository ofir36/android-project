package com.example.ishare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ishare.model.Model;
import com.example.ishare.model.Post;
import com.example.ishare.model.User;

import java.text.SimpleDateFormat;
import java.util.List;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.PostViewHolder> {
    List<Post> mData;
    ProfilePostsAdapter.PostClickListener listener;

    interface PostClickListener {
        void onPostClick(Post post);
    }

    public ProfilePostsAdapter(List<Post> mData, ProfilePostsAdapter.PostClickListener listener ) {
        this.mData = mData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProfilePostsAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_post_row, parent, false);
        ProfilePostsAdapter.PostViewHolder vh = new ProfilePostsAdapter.PostViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfilePostsAdapter.PostViewHolder holder, int position) {
        Post post = mData.get(position);
        holder.bind(post, listener);
    }

    public void deleteItem(final int position, final View view) {
        Post post = mData.get(position);
        Utility.showSpinner(view.getContext());
        Model.instance.deletePost(post, new Model.DeletePostListener() {
            @Override
            public void onComplete(boolean success) {
                Utility.hideSpinner();
                if (!success) {
                    Utility.showAlert("Error deleting post, please try again.", view);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView dateTv;
        TextView postTv;
        ImageView postImage;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTv = itemView.findViewById(R.id.profile_post_row_date);
            postTv = itemView.findViewById(R.id.profile_post_row_text);
            postImage = itemView.findViewById(R.id.profile_post_row_image);
        }

        public void bind(final Post post, final ProfilePostsAdapter.PostClickListener listener) {
            postTv.setText(post.text);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            dateTv.setText(formatter.format(post.lastUpdate));

            if (post.image != "")
                Model.instance.getImage(post.image, postImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPostClick(post);
                }
            });
        }
    }
}
