package com.example.ishare;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ishare.model.Model;
import com.example.ishare.model.Post;
import com.example.ishare.model.PostAsyncDao;

import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewPostFragment extends Fragment {

    ImageView imageView;
    TextView postTv;
    Button shareBtn;

    public NewPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("New Post");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_post, container, false);

        imageView = view.findViewById(R.id.new_post_image);
        postTv = view.findViewById(R.id.new_post_text);
        shareBtn = view.findViewById(R.id.new_post_share_btn);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = UUID.randomUUID().toString();
                Post post = new Post(id, postTv.getText().toString(), "ofir", "image", 1234, 0);
                Model.instance.addPost(post, new Model.AddPostListener() {
                    @Override
                    public void onComplete(boolean success) {
                        Navigation.findNavController(getView()).navigateUp();
                    }
                });
            }
        });

        return view;
    }

}
