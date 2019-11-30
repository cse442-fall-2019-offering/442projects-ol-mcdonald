package com.cse442.olmcdonald;


import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

/**
 * ViewDialog dialog to be displayed when waiting for data to return
 */
public class ViewDialog {

    Activity activity;
    Dialog dialog;

    public ViewDialog(Activity activity) {this.activity = activity;}

    /**
     * Display the loading dialog
     */
    public void showDialog() {
        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_view_dialog);
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);
        Glide.with(activity)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .crossFade()
                .into(imageViewTarget);
        dialog.show();
    }

    /**
     * Return if dialog is showing
     * @return is Dialog showing
     */
    public  boolean isShowing(){
        return dialog.isShowing();
    }
    /**
     * Close the loading dialog
     */
    public void closeDialog(){
        dialog.dismiss();
    }


}