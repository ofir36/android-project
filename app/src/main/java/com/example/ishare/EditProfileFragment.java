package com.example.ishare;


import android.Manifest;
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
import com.example.ishare.model.User;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    User user;

    ImageView imageView;
    TextView userNameTv;
    TextView aboutTv;
    Button chooseImageBtn;
    Button updateButton;

    Bitmap imageBitmap;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Edit Profile");
        user = EditProfileFragmentArgs.fromBundle(getArguments()).getUser();

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_profile, container, false);

        imageView = view.findViewById(R.id.edit_profile_image);
        userNameTv = view.findViewById(R.id.edit_profile_name);
        aboutTv = view.findViewById(R.id.edit_profile_about);
        chooseImageBtn = view.findViewById(R.id.edit_profile_choose_image_btn);
        updateButton = view.findViewById(R.id.edit_profile_update_btn);

        if (user != null)
        {
            userNameTv.setText(user.name);
            aboutTv.setText(user.about);

            if (!user.image.isEmpty())
            {
                Model.instance.getImage(user.image, imageView);
            }
        }

        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        return view;
    }

    private void updateUser() {
        Utility.hideKeyboard(getActivity());

        if (userNameTv.getText().toString().isEmpty())
        {
            Utility.showAlert("Name can't be empty.", getView());
        }
        else
        {
            Utility.showSpinner(getContext());

            if (imageBitmap != null)
            {
                Model.instance.saveImage(imageBitmap, new Model.SaveImageListener() {
                    @Override
                    public void onComplete(String url) {
                        saveDetails(url);
                    }
                });
            }
            else
            {
                saveDetails(user.image);
            }
        }
    }

    private void saveDetails(String url) {
        User user = new User(Model.instance.getUserId(), userNameTv.getText().toString(), url, aboutTv.getText().toString(), null);
        Model.instance.updateUser(user);
        Navigation.findNavController(getView()).popBackStack();
        Utility.hideSpinner();
    }

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
