package com.example.android.tflitecamerademo;

import java.util.Arrays;
import java.util.List;

public class BaseAngle {
    private int first;
    private int second;
    private int thrid;

    BaseAngle(int first, int second, int thrid) {
        this.first = first;
        this.second = second;
        this.thrid = thrid;
    }

    List<Integer> getPoints() {
        return Arrays.asList(first, second, thrid);
    }
}
