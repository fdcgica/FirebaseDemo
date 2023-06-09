package com.example.firebasedemo.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.example.firebasedemo.R;
public class LoadingDialog {

    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity mActivity){
        activity = mActivity;
    }

    public void show(int layoutID){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(layoutID, null);
        builder.setView(view);
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void dismiss(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
