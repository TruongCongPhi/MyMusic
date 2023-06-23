package com.truongcongphi.mymusic.ButtonAnimator;//package com.truongcongphi.mymusic.ButtonAnimator;

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
//import android.animation.ObjectAnimator;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.animation.AlphaAnimation;
//
//public class ButtonAnimator implements View.OnTouchListener {
//    private ObjectAnimator scaleDownX;
//    private ObjectAnimator scaleDownY;
//    private ObjectAnimator scaleUpX;
//    private ObjectAnimator scaleUpY;
//
//    public ButtonAnimator() {
//    }
//
//    public void applyScaleEffect(View view, float scale) {
//        scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", scale);
//        scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", scale);
//        scaleDownX.setDuration(100);
//        scaleDownY.setDuration(100);
//
//        scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f);
//        scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f);
//        scaleUpX.setDuration(100);
//        scaleUpY.setDuration(100);
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            applyScaleEffect(v, 0.95f);
//            scaleDownX.start();
//            scaleDownY.start();
//        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//            applyScaleEffect(v, 1f);
//            scaleUpX.start();
//            scaleUpY.start();
//        }
//        return false;
//    }
//}