package com.example.android.tflitecamerademo.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PoseList {
    private String title;
    private ArrayList<Double> Angles;

    public PoseList() {
        this.Angles = new ArrayList<>();
        this.Angles.add(60.25);
        this.Angles.add(80.25);
        this.Angles.add(90.25);
        this.Angles.add(120.25);
        this.Angles.add(135.25);
        this.Angles.add(80.25);
        this.title = "sameple pose";
    }

    public ArrayList<Double> getAngles() {
        return this.Angles;
    }
    public String getTitle() {
        return this.title;
    }

}
