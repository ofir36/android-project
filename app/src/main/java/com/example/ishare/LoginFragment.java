package com.example.ishare;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
public class LoginFragment extends Fragment {

    TextView emailTv;
    TextView passwordTv;
    Button loginBtn;
    Button registerBtn;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailTv = view.findViewById(R.id.login_email);
        passwordTv = view.findViewById(R.id.login_password);
        loginBtn = view.findViewById(R.id.login_login_btn);
        registerBtn = view.findViewById(R.id.login_register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });

        return view;
    }

    void login() {
        Utility.hideKeyboard(getActivity());
        if (emailTv.getText().toString().isEmpty() || passwordTv.getText().toString().isEmpty())
        {
            Utility.showAlert("Please fill all the details.", getView());
        }
        else
        {
            Utility.showSpinner(getContext());

            Model.instance.signIn(emailTv.getText().toString(), passwordTv.getText().toString(), new Model.SignInListener() {
                @Override
                public void OnComplete(boolean success) {
                    Utility.hideSpinner();

                    if (!success)
                    {
                        Utility.showAlert("Error in login, please check your details.", getView());
                    }
                    else
                    {
                      navigateToApp();
                    }
                }
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Model.instance.isSignedIn())
        {
            navigateToApp();
        }
    }

    void navigateToApp(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
