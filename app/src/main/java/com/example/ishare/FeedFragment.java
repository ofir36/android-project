package com.example.ishare;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ishare.model.Model;
import com.example.ishare.model.Post;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    FeedAdapter adapter;
    List<Post> mData = new LinkedList<>();

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        mRecyclerView = view.findViewById(R.id.feed_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

//        for (int i =0; i<5; i++)
//        {
//            mData.add(new Post("id", "Post number " + (i+1), "zamir", "image", 12345, 0));
//        }



        adapter = new FeedAdapter(mData);
        mRecyclerView.setAdapter(adapter);

        Model.instance.getAllPosts(new Model.GetAllPostsListener() {
            @Override
            public void onComplete(List<Post> data) {
                mData = data;
                adapter.mData = data;
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

}
