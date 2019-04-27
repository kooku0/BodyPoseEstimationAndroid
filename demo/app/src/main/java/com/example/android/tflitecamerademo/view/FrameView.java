package com.example.android.tflitecamerademo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class FrameView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    float cx = 0, cy = 0;
    boolean draw = false;

    public FrameView(Context context) {
        super(context);
        init();
    }

    public FrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FrameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLUE);
        setFocusable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (draw) {
            canvas.drawCircle(cx, cy, 3, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            cx = event.getX();
            cy = event.getY();
            draw = true;
            invalidate();
        } else {
            performClick();
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}