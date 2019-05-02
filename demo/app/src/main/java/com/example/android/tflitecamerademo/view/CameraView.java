package com.example.android.tflitecamerademo.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.android.tflitecamerademo.Camera2BasicFragment;
import com.example.android.tflitecamerademo.R;

public class CameraView extends View {
    public CameraView(Context context) {
        super(context);
        init();
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    FragmentManager fragmentManager;

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

        Camera2BasicFragment myFragment = (Camera2BasicFragment) getFragmentManager().findFragmentByTag("camera");
    private void init() {
//        Fragment fragment = new PropertyFragment();

        if (fragmentManager == null) {
//            FragmentManager fragmentManager = fragment.getFragmentManager();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.container, Camera2BasicFragment.newInstance());
//            fragmentTransaction.replace(R.id.container, myFragment, "camera");
            fragmentTransaction.commit();
//
        }
//            getFragmentManager().beginTransaction()
//                .replace(R.id.container, Camera2BasicFragment.newInstance()).commit();
    }
}
