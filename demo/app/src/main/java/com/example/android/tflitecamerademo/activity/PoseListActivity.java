package com.example.android.tflitecamerademo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.android.tflitecamerademo.R;
import com.example.android.tflitecamerademo.model.PoseList;
import com.example.android.tflitecamerademo.model.PoseRecyclerAdapter;

import java.util.List;

public class PoseListActivity extends Activity {
    private PoseRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pose_list);

        init();

        setData();
    }

    private void init() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        Button poseRegisterButton = findViewById(R.id.poseRegisterButton);
        adapter = new PoseRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        poseRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GetImageActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setData() {
        PoseList pose = new PoseList();
        adapter.addItem(pose);
        adapter.addItem(pose);
        adapter.addItem(pose);
        adapter.notifyDataSetChanged();
    }
}
