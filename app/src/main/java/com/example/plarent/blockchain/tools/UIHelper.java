package com.example.plarent.blockchain.tools;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by plarent on 02/03/2018.
 */

public class UIHelper {

    public static void showDialog(Activity activity, String title, String message, String buttonText,
                                  DialogInterface.OnClickListener buttonListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(buttonText, buttonListener);
        builder.setCancelable(false);
        builder.show();
    }
}
