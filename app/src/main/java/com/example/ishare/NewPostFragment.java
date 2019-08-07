package com.example.ishare;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ishare.model.Model;
import com.example.ishare.model.Post;
import com.google.android.material.snackbar.Snackbar;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewPostFragment extends Fragment {

    ImageView imageView;
    TextView postTv;
    Button shareBtn;
    Bitmap imageBitmap;

    // edited post
    Post post;

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
                dispatchTakePictureIntent();
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShare();
            }
        });

        return view;
    }

    private void onShare() {
        Utility.hideKeyboard(getActivity());
        if (post == null && imageBitmap == null && postTv.getText().toString().isEmpty())
        {
            Utility.showAlert("Can't share an empty post.", getView());
            return;
        }

        Utility.showSpinner(getContext());

        if (imageBitmap != null) {
            Model.instance.saveImage(imageBitmap, new Model.SaveImageListener() {
                @Override
                public void onComplete(String url) {
                    sharePost(url);
                }
            });
        }
        else if (post != null)
        {
            sharePost(post.image);
        }
        else{
            sharePost("");
        }

    }

    private void sharePost(String url) {
        String id = UUID.randomUUID().toString();

        if (post != null)
        {
            id = post.id;
        }

        Post post = new Post(id, postTv.getText().toString(), Model.instance.getUserId(), url);

        Model.instance.addPost(post, new Model.AddPostListener() {
            @Override
            public void onComplete(boolean success) {
                Navigation.findNavController(getView()).navigateUp();
                Utility.hideSpinner();
            }
        });
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int GALLERY_REQUEST_CODE = 100;

    private void dispatchTakePictureIntent() {
        boolean hasPermission = (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);

        if (!hasPermission) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
        }

        Intent takePictureIntent = new Intent(Intent.ACTION_PICK);
        takePictureIntent.setType("image/*");


        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, GALLERY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
//        }

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            //data.getData return the content URI for the selected Image
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            // Get the cursor
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();
            //Get the column index of MediaStore.Images.Media.DATA
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            //Gets the String value in the column
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            // Set the Image in ImageView after decoding the String
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 8;
            imageBitmap = BitmapFactory.decodeFile(imgDecodableString);
            imageView.setImageBitmap(imageBitmap);
        }

            super.onActivityResult(requestCode, resultCode, data);
    }
}
