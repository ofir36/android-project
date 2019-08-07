package com.example.ishare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
