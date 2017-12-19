package org.robbojunior.android;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.KeyEvent;

/**
 * Created by yarila on 11.08.17.
 */

public class RobotProgressDialog extends ProgressDialog {

     private boolean BackPressed = false;

    Activity activity;


    public RobotProgressDialog(Context context) {
        super(context);

        BackPressed = false;

        activity = (Activity)context;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        activity.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_BACK));
        BackPressed = true;


    }

    public boolean getBackPressed(){

               return BackPressed;

    }
}
