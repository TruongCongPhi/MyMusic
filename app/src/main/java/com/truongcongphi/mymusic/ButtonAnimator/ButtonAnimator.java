package com.truongcongphi.mymusic.ButtonAnimator;// ButtonAnimator.java
import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;

public class ButtonAnimator implements View.OnTouchListener {
    private ObjectAnimator scaleDownX;
    private ObjectAnimator scaleDownY;
    private ObjectAnimator scaleUpX;
    private ObjectAnimator scaleUpY;

    private float scaleFactor = 0.95f;

    public ButtonAnimator() {
    }
    private void applyScaleEffect(View view, float scale) {
        scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", scale);
        scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", scale);
        scaleDownX.setDuration(100);
        scaleDownY.setDuration(100);

        scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f);
        scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f);
        scaleUpX.setDuration(100);
        scaleUpY.setDuration(100);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            applyScaleEffect(v, scaleFactor);
            scaleDownX.start();
            scaleDownY.start();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            applyScaleEffect(v, 1f);
            scaleUpX.start();
            scaleUpY.start();
        }
        return false;
    }
}
