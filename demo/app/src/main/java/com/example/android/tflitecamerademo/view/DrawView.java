package com.example.android.tflitecamerademo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import com.example.android.tflitecamerademo.Camera2BasicFragment;
import com.example.android.tflitecamerademo.R;

public class DrawView extends View {
    private static final int HEATMAPWIDTH = 96;
    private static final int HEATMAPHEIGHT = 96;
    private static final int NUMJOINT = 14;

    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    public static int MAX_PREVIEW_HEIGHT;

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    public static int MAX_PREVIEW_WIDTH;
    private static int count = 0;
    private static int count1 = 0;

    static View myView;
    private Paint paint[] = new Paint[14];
    private Paint ppaint;
    private static float[][] arr = new float[14][2];

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        myView = DrawView.this;
        ppaint = new Paint();
        ppaint.setStrokeWidth(7);
        ppaint.setColor(Color.CYAN);

        for (int i = 0; i < NUMJOINT; i++) {
            paint[i] = new Paint();
            paint[i].setStyle(Paint.Style.STROKE);
            paint[i].setStrokeWidth(15);
        }
        paint[0].setColor(Color.BLACK);
        paint[1].setColor(Color.RED);
        paint[2].setColor(Color.GREEN);
        paint[3].setColor(Color.YELLOW);
        paint[4].setColor(Color.BLUE);
        paint[5].setColor(Color.DKGRAY);
        paint[6].setColor(Color.LTGRAY);
        paint[7].setColor(Color.MAGENTA);
        paint[8].setColor(Color.WHITE);
        paint[9].setColor(Color.BLUE);
        paint[10].setColor(Color.RED);
        paint[11].setColor(Color.GREEN);
        paint[12].setColor(Color.YELLOW);
        paint[13].setColor(Color.BLUE);

        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println("count Check, onDraw 호출");
//        canvas.drawText("토끼",250, 250, ppaint);
        count++;

        canvas.drawLine(arr[0][1], arr[0][0], arr[1][1], arr[1][0], ppaint);
        canvas.drawLine(arr[1][1], arr[1][0], arr[2][1], arr[2][0], ppaint);
        canvas.drawLine(arr[1][1], arr[1][0], arr[5][1], arr[5][0], ppaint);
        canvas.drawLine(arr[2][1], arr[2][0], arr[3][1], arr[3][0], ppaint);
        canvas.drawLine(arr[3][1], arr[3][0], arr[4][1], arr[4][0], ppaint);
        canvas.drawLine(arr[5][1], arr[5][0], arr[6][1], arr[6][0], ppaint);
        canvas.drawLine(arr[6][1], arr[6][0], arr[7][1], arr[7][0], ppaint);

        /*float x, y;
        x = (arr[8][1] + arr[11][1]) / 2 + 5;
        y = (arr[8][0] + arr[11][0]) / 2 + 5;*/

        /*canvas.drawLine(arr[1][1],arr[1][0], x, y, ppaint);
        canvas.drawLine(x, y, arr[8][1], arr[8][0], ppaint);
        canvas.drawLine(x, y, arr[11][1], arr[11][0], ppaint);*/
        canvas.drawLine(arr[1][1], arr[1][0], arr[8][1], arr[8][0], ppaint);
        canvas.drawLine(arr[1][1], arr[1][0], arr[11][1], arr[11][0], ppaint);

        canvas.drawLine(arr[8][1], arr[8][0], arr[9][1], arr[9][0], ppaint);
        canvas.drawLine(arr[9][1], arr[9][0], arr[10][1], arr[10][0], ppaint);
        canvas.drawLine(arr[11][1], arr[11][0], arr[12][1], arr[12][0], ppaint);
        canvas.drawLine(arr[12][1], arr[12][0], arr[13][1], arr[13][0], ppaint);

        for (int i = 0; i < NUMJOINT; i++) {
            Log.d("check1", i + " : " + arr[i][0] + " " + arr[i][1]);
            canvas.drawPoint(arr[i][1], arr[i][0], paint[i]);
        }
    }


    public static void setArr(int index, int[] inputArr) {
        arr[index][0] = (float) inputArr[0] / HEATMAPHEIGHT * MAX_PREVIEW_HEIGHT;
        arr[index][1] = (float) inputArr[1] / HEATMAPWIDTH * MAX_PREVIEW_WIDTH;

        if (index == 13) {
            Log.d("count Check", "count: " + count + ", count1: " + count1++);
            Log.d("line", "--------------");
            for (int i = 0; i < NUMJOINT; i++) {
                Log.d("check", arr[i][0] + " " + arr[i][1]);
            }
            Log.d("line", "--------------");
            myView.invalidate();
        }
    }
}
