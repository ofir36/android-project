package com.example.ishare;

import android.app.ProgressDialog;
import android.content.Context;

public class Utility {

    static ProgressDialog dialog;

    public static void showSpinner(Context context){
        dialog = ProgressDialog.show(context, "", "");
    }

    public static void hideSpinner() {
        dialog.dismiss();
    }
}
