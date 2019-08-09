package com.example.ishare;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ishare.model.Model;
import com.example.ishare.model.Post;
import com.example.ishare.model.User;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    User user;

    ImageView userImageView;
    TextView userNameTv;
    TextView aboutTv;

    ProfileViewModel viewModel;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProfilePostsAdapter adapter;

    List<Post> mData = new LinkedList<>();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Profile");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userImageView = view.findViewById(R.id.profile_image);
        userNameTv = view.findViewById(R.id.profile_name_tv);
        aboutTv = view.findViewById(R.id.profile_about_tv);

        mRecyclerView = view.findViewById(R.id.profile_posts_rv);
        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new ProfilePostsAdapter(mData, new ProfilePostsAdapter.PostClickListener() {
            @Override
            public void onPostClick(Post post) {

            }
        });
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        viewModel.getUserDetails().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User _user) {
                user = _user;

                userNameTv.setText(user.name);
                aboutTv.setText(user.about);

                if (!user.image.isEmpty())
                {
                    Model.instance.getImage(user.image, userImageView);
                }
            }
        });
        viewModel.getUserPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                mData = posts;
                adapter.mData = posts;
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_menu_logout:
                logout();
                return true;

            case R.id.profile_menu_edit:
                ProfileFragmentDirections.ActionProfileFragmentToEditProfileFragment action =
                        ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(user);
                Navigation.findNavController(getView()).navigate(action);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        boolean result = Model.instance.signOut();

        if (!result)
        {
            Utility.showAlert("Error logging out, please try again.", getView());
        }
        else
        {
            Intent intent = new Intent(getContext(), StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
