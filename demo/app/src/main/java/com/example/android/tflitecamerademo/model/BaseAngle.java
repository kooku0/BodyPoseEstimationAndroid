package com.example.android.tflitecamerademo.model;

import java.util.Arrays;
import java.util.List;

public class BaseAngle {
    private int first;
    private int second;
    private int thrid;

    public BaseAngle(int first, int second, int thrid) {
        this.first = first;
        this.second = second;
        this.thrid = thrid;
    }

    public List<Integer> getPoints() {
        return Arrays.asList(first, second, thrid);
    }
}
