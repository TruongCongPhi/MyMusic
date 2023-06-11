package com.truongcongphi.mymusic.ButtonAnimator;

import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class ButtonAnimator implements View.OnTouchListener {
    private ObjectAnimator scaleDownX;
    private ObjectAnimator scaleDownY;
    private ObjectAnimator scaleUpX;
    private ObjectAnimator scaleUpY;

    public ButtonAnimator(Button button) {
        scaleDownX = ObjectAnimator.ofFloat(button, "scaleX", 0.95f);
        scaleDownY = ObjectAnimator.ofFloat(button, "scaleY", 0.95f);
        scaleDownX.setDuration(100);
        scaleDownY.setDuration(100);

        scaleUpX = ObjectAnimator.ofFloat(button, "scaleX", 1f);
        scaleUpY = ObjectAnimator.ofFloat(button, "scaleY", 1f);
        scaleUpX.setDuration(100);
        scaleUpY.setDuration(100);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            scaleDownX.start();
            scaleDownY.start();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            scaleUpX.start();
            scaleUpY.start();
        }
        return false;
    }
}
