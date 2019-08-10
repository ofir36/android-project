package com.example.ishare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ishare.model.Model;
import com.example.ishare.model.Post;
import com.example.ishare.model.User;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.PostViewHolder> {

    List<Post> mData;
    PostClickListener listener;

    interface PostClickListener {
        void onPostClick(Post post);
    }

    public FeedAdapter(List<Post> mData, PostClickListener listener ) {
        this.mData = mData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        PostViewHolder vh = new PostViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position) {
        Post post = mData.get(position);
        holder.bind(post, listener);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView userName;
        TextView dateTv;
        TextView postTv;
        ImageView postImage;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.post_row_avatar);
            userName = itemView.findViewById(R.id.post_row_name);
            dateTv = itemView.findViewById(R.id.post_row_date);
            postTv = itemView.findViewById(R.id.post_row_text);
            postImage = itemView.findViewById(R.id.post_row_image);
        }

        public void bind(final Post post, final PostClickListener listener) {
            postTv.setText(post.text);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            dateTv.setText(formatter.format(post.lastUpdate));
            userName.setText(post.userId);

            if (post.image != "")
                Model.instance.getImage(post.image, postImage);

            userName.setTag(post.id);
            Model.instance.getUserDetails(post.userId, new Model.GetUserDetailsListener() {
                @Override
                public void onComplete(User user) {
                    if (userName.getTag() == post.id) {
                        userName.setText(user.name);

                        if (!user.image.isEmpty())
                            Model.instance.getImage(user.image, avatar);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPostClick(post);
                }
            });
        }
    }
}
