package com.gegosoft.schoolteacherapp.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.gegosoft.schoolteacherapp.Activity.LoginActivity;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;

import java.net.SocketTimeoutException;

public class AppUtils {

    public static void APIFails(Context context, Throwable t){
        if (t instanceof SocketTimeoutException)
        {
            Toast.makeText(context,"Connection Time Out", Toast.LENGTH_LONG).show();
        }
    }

    public static void SessionExpired(final Activity context) {
        final UserDetailsSharedPref userDetailsSharedPref = UserDetailsSharedPref.getInstance(context);

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Session Expired");
        alertDialog.setMessage("Please Log in Again");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        userDetailsSharedPref.logout();
                        context.startActivity(new Intent(context, LoginActivity.class));
                        context.finish();
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }
}
