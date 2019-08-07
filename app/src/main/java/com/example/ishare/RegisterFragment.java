package com.example.ishare;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ishare.model.Model;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    TextView emailTv;
    TextView userNameTv;
    TextView passwordTv;
    TextView vPasswordTv;
    Button registerBtn;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        emailTv = view.findViewById(R.id.register_email);
        userNameTv = view.findViewById(R.id.register_name);
        passwordTv = view.findViewById(R.id.register_password);
        vPasswordTv = view.findViewById(R.id.register_vpassword);
        registerBtn = view.findViewById(R.id.register_register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailTv.getText().toString().isEmpty() || userNameTv.getText().toString().isEmpty() ||
                    passwordTv.getText().toString().isEmpty() || vPasswordTv.getText().toString().isEmpty())
                {
                    Utility.showAlert("Please fill all the details.", getView());
                }
                else if (passwordTv.getText().toString().compareTo(vPasswordTv.getText().toString()) != 0)
                {
                    Utility.showAlert("Password do not match.", getView());
                }
                else
                {
                    Utility.showSpinner(getContext());

                    Model.instance.createUser(emailTv.getText().toString(), passwordTv.getText().toString(), userNameTv.getText().toString(), new Model.CreateUserListener() {
                        @Override
                        public void onComplete(boolean success) {
                            Utility.hideSpinner();

                            if (!success)
                            {
                                Utility.showAlert("Error in register, Please try again.", getView());
                            }
                            else
                            {
                                Navigation.findNavController(getView()).navigateUp();
                            }
                        }
                    });
                }
            }
        });

        return view;
    }

}
