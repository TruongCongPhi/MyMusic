package com.truongcongphi.mymusic.Class;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import com.truongcongphi.mymusic.R;

public class LoadingDialog {
    Context context;
    Dialog dialog;
    public LoadingDialog(Context context){
        this.context = context;
    }
    public void showDialog(){

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_loading);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.create();
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
