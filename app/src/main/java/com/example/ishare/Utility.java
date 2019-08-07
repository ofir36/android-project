package com.example.ishare;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class Utility {

    static ProgressDialog dialog;

    public static void showSpinner(Context context){
        dialog = ProgressDialog.show(context, "", "");
    }

    public static void hideSpinner() {
        dialog.dismiss();
    }

    public static void showAlert(String text, View view)
    {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }
}
