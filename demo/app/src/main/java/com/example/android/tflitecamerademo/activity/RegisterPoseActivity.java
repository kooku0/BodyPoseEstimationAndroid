package com.example.android.tflitecamerademo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.tflitecamerademo.R;

public class RegisterPoseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pose);

        init();

    }

    private void init() {
        Button poseMatchingButton = findViewById(R.id.poseMatchingButton);
        Button uploadImageButton = findViewById(R.id.uploadImageButton);

        poseMatchingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPoseActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
