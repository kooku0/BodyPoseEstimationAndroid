package com.example.android.tflitecamerademo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

        adapter = new PoseRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }
    private void setData() {
        PoseList pose = new PoseList();
        adapter.addItem(pose);
        adapter.addItem(pose);
        adapter.addItem(pose);
        adapter.notifyDataSetChanged();
    }
}
