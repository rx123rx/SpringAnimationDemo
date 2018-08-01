package com.example.renxiao.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * @author renxiao
 */
public class MainActivity extends AppCompatActivity {
    float scaleFactor = 1f;
    private RelativeLayout rootLayout;
    private int xDelta;
    private int yDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final View img = findViewById(R.id.textView);
        rootLayout = findViewById(R.id.root);
        SpringForce springForce = new SpringForce(60)
                .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
                .setStiffness(SpringForce.STIFFNESS_LOW);

        final SpringAnimation springAnim = new SpringAnimation(img, DynamicAnimation.TRANSLATION_Y);
        springAnim.setSpring(springForce);

        final ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleFactor *= detector.getScaleFactor();
                img.setScaleX(img.getScaleX() * scaleFactor);
                img.setScaleY(img.getScaleY() * scaleFactor);
                return true;
            }
        });


        img.setOnTouchListener((view, event) -> {
            final int rawX = (int) event.getRawX();
            final int rawY = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    xDelta = rawX - layoutParam.leftMargin;
                    yDelta = rawY - layoutParam.topMargin;
                    break;
                case MotionEvent.ACTION_UP:
                    springAnim.start();
                    view.performClick();
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    springAnim.cancel();
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.leftMargin = rawX - xDelta;
                    layoutParams.topMargin = rawY - yDelta;
                    view.setLayoutParams(layoutParams);
                    springAnim.setStartValue(img.getTop());
                    break;
                default:
                    springAnim.cancel();
                    break;
            }
            scaleGestureDetector.onTouchEvent(event);
            rootLayout.invalidate();
            return true;

        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        ScreenUtils.resetDensity(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ScreenUtils.resetDensity(this.getApplicationContext());
    }
}
