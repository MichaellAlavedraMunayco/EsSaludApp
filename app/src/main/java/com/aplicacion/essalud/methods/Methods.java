package com.aplicacion.essalud.methods;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.aplicacion.essalud.R;
import com.google.android.material.snackbar.Snackbar;

public class Methods {

    public static void showSnackBar(Snackbar snackbar) {
        snackbar.show();
        View view = snackbar.getView();
        TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
    }
}
