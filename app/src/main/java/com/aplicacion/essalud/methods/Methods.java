package com.aplicacion.essalud.methods;

import android.util.Base64;
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

    public static String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }
}
