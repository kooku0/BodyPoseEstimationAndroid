/* Copyright 2017 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.example.android.tflitecamerademo;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.example.android.tflitecamerademo.view.DrawView;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.tensorflow.lite.Delegate;
import org.tensorflow.lite.Interpreter;

/**
 * Classifies images with Tensorflow Lite.
 */
public abstract class ImageClassifier {

    // Display preferences
    /**
     * 30% 이상은 강조글로 표현
     */
    //private static final float GOOD_PROB_THRESHOLD = 0.3f;
    private static final int SMALL_COLOR = 0xffddaa88;

    /**
     * zk  * output shape (heatmap shape)
     */

    //model_h
    private static final int HEATMAPWIGHT = 96;
    private static final int HEATMAPHEIGHT = 96;
    private static final int NUMJOINT = 14;
    //이게 점들의수

    /**
     * Tag for the {@link Log}.
     */
    private static final String TAG = "TfLiteCameraDemo";

    /** Number of results to show in the UI.
     * 몇개 결과창으로 보여 줄지 */
    //private static final int RESULTS_TO_SHOW = 3;

    /**
     * Dimensions of inputs.
     */
    private static final int DIM_BATCH_SIZE = 1;

    /**
     * 채널 사이즈 입력 값
     */
    private static final int DIM_PIXEL_SIZE = 3;

    private static final int FULL_DEGREE = 360;
    private static final int HALF_DEGREE = 180;
    private static final int QUARTER_DEGREE = 90;

    private static List<BaseAngle> BASE_ANGLE_LIST = new ArrayList<>();

    static {
        BASE_ANGLE_LIST.add(new BaseAngle(0, 1, 2));
        BASE_ANGLE_LIST.add(new BaseAngle(1, 2, 3));
        BASE_ANGLE_LIST.add(new BaseAngle(2, 3, 4));
        BASE_ANGLE_LIST.add(new BaseAngle(0, 1, 5));
        BASE_ANGLE_LIST.add(new BaseAngle(1, 5, 6));
        BASE_ANGLE_LIST.add(new BaseAngle(5, 6, 7));
    }

    /**
     * Preallocated buffers for storing image data in.
     * 이미지 데이터를 저장하기 위해 사전 할당 된 버퍼
     */
    private int[] intValues = new int[getImageSizeX() * getImageSizeY()];

    /**
     * Options for configuring the Interpreter.
     * 인터프리터 구성 옵션
     */
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();

    /**
     * The loaded TensorFlow Lite model.
     * MappedByteBuffer 파일 자체를 메모리에 적제하여 사용하는 방법의 일종
     */
    private MappedByteBuffer tfliteModel;

    /**
     * An instance of the driver class to run model inference with Tensorflow Lite.
     * Tensorflow Lite로 모델 추론을 실행하는 드라이버 클래스의 인스턴스
     */
    protected Interpreter tflite;

    /** Labels corresponding to the output of the vision model.
     * 결과물의 레이블의 종류가 담기는 곳 */
    //private List<String> labelList;


    /**
     * A ByteBuffer to hold image data, to be feed into Tensorflow Lite as inputs.
     * Tensorflow Lite에 입력으로 제공될 이미지 데이터를 보유하는 ByteBuffer입니다.
     */
    protected ByteBuffer imgData = null;

    /** multi-stage low pass filter *
     * ?? 결과값에 대한 정규화? */
  /*
  private float[][] filterLabelProbArray = null;

  private static final int FILTER_STAGES = 3;
  private static final float FILTER_FACTOR = 0.4f;

  private PriorityQueue<Map.Entry<String, Float>> sortedLabels =
      new PriorityQueue<>(
          RESULTS_TO_SHOW,
          new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
              return (o1.getValue()).compareTo(o2.getValue());
            }
          });
  */
    /**
     * holds a gpu delegate
     */
    Delegate gpuDelegate = null;

    /**
     * Initializes an {@code ImageClassifier}.
     */
    ImageClassifier(Activity activity) throws IOException {
        /** tfliteModel => MappedByteBuffer */
        tfliteModel = loadModelFile(activity);
        /** tflite => Interpreter */
        tflite = new Interpreter(tfliteModel, tfliteOptions);
        //labelList = loadLabelList(activity);
        imgData =
                ByteBuffer.allocateDirect(
                        DIM_BATCH_SIZE
                                * getImageSizeX()
                                * getImageSizeY()
                                * DIM_PIXEL_SIZE
                                * getNumBytesPerChannel());

        //메모리에 적게되는 방식 정리
        // TODO: 자세히 알아볼 필요 있음
        imgData.order(ByteOrder.nativeOrder());

        /** 결과값에ㅐ low pass filter를 적용하기 위한 변수 */
        //filterLabelProbArray = new float[FILTER_STAGES][getNumLabels()];
        Log.d(TAG, "Created a Tensorflow Lite Image Classifier.");
    }

    /**
     * Classifies a frame from the preview stream.
     */
    //TODO: 어디서 활용되는지 확인 필요
    void classifyFrame(Bitmap bitmap, SpannableStringBuilder builder) {
        if (tflite == null) {
            Log.e(TAG, "Image classifier has not been initialized; Skipped.");
            builder.append(new SpannableString("Uninitialized Classifier."));
        }

        convertBitmapToByteBuffer(bitmap);

        // Here's where the magic happens!!!
        long startTime = SystemClock.uptimeMillis();
        runInference();
        long endTime = SystemClock.uptimeMillis();
        Log.d(TAG, "Timecost to run model inference: " + Long.toString(endTime - startTime));

        drawBodyPoint();

        // Smooth the results across frames.
        //applyFilter();

        // Print the results.
    /*
    printTopKLabels(builder);
    long duration = endTime - startTime;
    SpannableString span = new SpannableString(duration + " ms");
    span.setSpan(new ForegroundColorSpan(android.graphics.Color.LTGRAY), 0, span.length(), 0);
    builder.append(span);
    */
    }

    private void drawBodyPoint() {
        int index = 0;
        float[][] arr = new float[14][2];
        for (int k = 0; k < getNumJoint(); k++) {
            float[][] heatmap = new float[getHeatmapWidth()][getHeatmapHeight()];
            for (int i = 0; i < getHeatmapWidth(); i++) {
                for (int j = 0; j < getHeatmapHeight(); j++) {
                    heatmap[i][j] = getProbability(index, i, j, k);
                }
            }
            float[] result;
            result = findMaximumIndex(heatmap);
//            Log.d("Bodypoint", "index[" + k + "] = " + " " + result[0] + " " + result[1] + " ");

            arr[k] = result;
        }
        DrawView.setArr(arr);
        compareAccuracy(arr);
    }

    private static float[] findMaximumIndex(float[][] a) {
        float maxVal = -99999;
        float[] answerArray = new float[2];
        for (int row = 0; row < a.length; row++) {
            for (int col = 0; col < a[row].length; col++) {
                if (a[row][col] > maxVal) {
                    maxVal = a[row][col];
                    answerArray[0] = row;
                    answerArray[1] = col;
                }
            }
        }
        return answerArray;
    }

    private double getAngle(float[][] resultArr, int index) {
        List<Integer> basePoints = BASE_ANGLE_LIST.get(index).getPoints();
        double thetaA, thetaB, thetaC;
        double temp, Angle;
        thetaA = Math.sqrt(Math.pow(resultArr[basePoints.get(0)][1] - resultArr[basePoints.get(2)][1], 2) +
                Math.pow(resultArr[basePoints.get(0)][0] - resultArr[basePoints.get(2)][0], 2));
        thetaB = Math.sqrt(Math.pow(resultArr[basePoints.get(0)][1] - resultArr[basePoints.get(1)][1], 2) +
                Math.pow(resultArr[basePoints.get(0)][0] - resultArr[basePoints.get(1)][0], 2));
        thetaC = Math.sqrt(Math.pow(resultArr[basePoints.get(1)][1] - resultArr[basePoints.get(2)][1], 2) +
                Math.pow(resultArr[basePoints.get(1)][0] - resultArr[basePoints.get(2)][0], 2));

        temp = (Math.pow(thetaB, 2) + Math.pow(thetaC, 2) - Math.pow(thetaA, 2)) / (2 * thetaB * thetaC);
        Angle = Math.acos(temp);
        Angle = Angle * (180.0 / Math.PI);
        return Angle;
    }

    private double getPersentage(double compareNumber, double betweenAngle) {
        return (((HALF_DEGREE - Math.abs(betweenAngle - compareNumber))) / HALF_DEGREE) * 100;
    }

    private void compareAccuracy(float[][] resultArr) {
        float[] compareArr = {124.778F, 145.222F, 64.369F, 114.842F, 114.842F, 180F};
        double totalPercentage = 0.0;
        double betweenAngle;
        double percentage = 0.0;
        List<String> percentageList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            betweenAngle = getAngle(resultArr, i);
            percentage = getPersentage(compareArr[i], betweenAngle);
            totalPercentage += percentage;
            Log.d("Accurancy", "목표 각도 :" + compareArr[i] + "  " + (i + 1) + "번 각도 : " + betweenAngle + "   정확도 : " + percentage);
            percentageList.add("목표 각도 :" + compareArr[i] + "  " + (i + 1) + "번 각도 : " + String.format("%.2f", betweenAngle) + "   정확도 : " + String.format("%.2f", percentage) + "%");
        }
        Camera2BasicFragment.setPercentageText(percentageList, totalPercentage / 6);
        Log.d("TotalAccurancy", "persentage: " + totalPercentage / 6 + " %");
    }

  /*
  void applyFilter() {
    int numLabels = getNumLabels();

    // Low pass filter `labelProbArray` into the first stage of the filter.
    for (int j = 0; j < numLabels; ++j) {
    for (int j = 0; j < numLabesetProbabilityls; ++j) {
      filterLabelProbArray[0][j] +=
          FILTER_FACTOR * (getProbability(j) - filterLabelProbArray[0][j]);
    }
    // Low pass filter each stage into the next.
    for (int i = 1; i < FILTER_STAGES; ++i) {
      for (int j = 0; j < numLabels; ++j) {
        filterLabelProbArray[i][j] +=
            FILTER_FACTOR * (filterLabelProbArray[i - 1][j] - filterLabelProbArray[i][j]);
      }
    }

    // Copy the last stage filter output back to `labelProbArray`.
    for (int j = 0; j < numLabels; ++j) {
      setProbability(j, filterLabelProbArray[FILTER_STAGES - 1][j]);
    }
  }
  */

    private void recreateInterpreter() {
        if (tflite != null) {
            tflite.close();
            // TODO(b/120679982)
            // gpuDelegate.close();
            tflite = new Interpreter(tfliteModel, tfliteOptions);
        }
    }

    public void useGpu() {
        if (gpuDelegate == null && GpuDelegateHelper.isGpuDelegateAvailable()) {
            gpuDelegate = GpuDelegateHelper.createGpuDelegate();
            tfliteOptions.addDelegate(gpuDelegate);
            recreateInterpreter();
        }
    }

    public void useCPU() {
        tfliteOptions.setUseNNAPI(false);
        recreateInterpreter();
    }

    public void useNNAPI() {
        tfliteOptions.setUseNNAPI(true);
        recreateInterpreter();
    }

    public void setNumThreads(int numThreads) {
        tfliteOptions.setNumThreads(numThreads);
        recreateInterpreter();
    }

    /**
     * Closes tflite to release resources.
     */
    public void close() {
        tflite.close();
        tflite = null;
        tfliteModel = null;
    }

    /** Reads label list from Assets. */
  /*
  private List<String> loadLabelList(Activity activity) throws IOException {
    List<String> labelList = new ArrayList<String>();
    BufferedReader reader =
        new BufferedReader(new InputStreamReader(activity.getAssets().open(getLabelPath())));
    String line;
    while ((line = reader.readLine()) != null) {
      labelList.add(line);
    }
    reader.close();
    return labelList;
  }
  */
    //TODO: 세부적으로 볼 필요 있음

    /**
     * Memory-map the model file in Assets.
     */
    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(getModelPath());
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    /**
     * Writes Image data into a {@code ByteBuffer}.
     */
    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // Convert the image to floating point.
        int pixel = 0;
        long startTime = SystemClock.uptimeMillis();
        for (int i = 0; i < getImageSizeX(); ++i) {
            for (int j = 0; j < getImageSizeY(); ++j) {
                final int val = intValues[pixel++];
                addPixelValue(val);
            }
        }
        long endTime = SystemClock.uptimeMillis();
        Log.d(TAG, "Timecost to put values into ByteBuffer: " + Long.toString(endTime - startTime));
    }

    /** Prints top-K labels, to be shown in UI as the results. */
  /*
  private void printTopKLabels(SpannableStringBuilder builder) {
    for (int i = 0; i < getNumLabels(); ++i) {
      sortedLabels.add(
          new AbstractMap.SimpleEntry<>(labelList.get(i), getNormalizedProbability(i)));
      if (sortedLabels.size() > RESULTS_TO_SHOW) {
        sortedLabels.poll();
      }
    }

    final int size = sortedLabels.size();
    for (int i = 0; i < size; i++) {
      Map.Entry<String, Float> label = sortedLabels.poll();
      SpannableString span =
          new SpannableString(String.format("%s: %4.2f\n", label.getKey(), label.getValue()));
      int color;
      // Make it white when probability larger than threshold.
      if (label.getValue() > GOOD_PROB_THRESHOLD) {
        color = android.graphics.Color.WHITE;
      } else {
        color = SMALL_COLOR;
      }
      // Make first item bigger.
      if (i == size - 1) {
        float sizeScale = (i == size - 1) ? 1.25f : 0.8f;
        span.setSpan(new RelativeSizeSpan(sizeScale), 0, span.length(), 0);
      }
      span.setSpan(new ForegroundColorSpan(color), 0, span.length(), 0);
      builder.insert(0, span);
    }
  }
  */

    /**
     * Get the name of the model file stored in Assets.
     * 상속받는 대상에 추상화 클래스 모델 파일 이름과 경로가 리턴됨
     *
     * @return
     */
    protected abstract String getModelPath();

    /**
     * Get the name of the label file stored in Assets.
     *
     * @return
     */
    //protected abstract String getLabelPath();

    /**
     * Get the image size along the x axis.
     *
     * @return
     */
    protected abstract int getImageSizeX();

    /**
     * Get the image size along the y axis.
     *
     * @return
     */
    protected abstract int getImageSizeY();

    /**
     * Get the number of bytes that is used to store a single color channel value.
     *
     * @return
     */
    protected abstract int getNumBytesPerChannel();

    /**
     * Add pixelValue to byteBuffer.
     *
     * @param pixelValue
     */
    protected abstract void addPixelValue(int pixelValue);

    /**
     * Read the probability value for the specified label This is either the original value as it was
     * read from the net's output or the updated value after the filter was applied.
     *
     * @param index
     * @param width
     * @param height
     * @param joint
     * @return
     */
    protected abstract float getProbability(int index, int width, int height, int joint);

    /**
     * Set the probability value for the specified label.
     *
     * @param labelIndex
     * @param labelIndex
     * @param value
     */
    //protected abstract void setProbability(int labelIndex, Number value);

    /**
     * Get the normalized probability value for the specified label. This is the final value as it
     * will be shown to the user.
     *
     * @return
     */
    //protected abstract float getNormalizedProbability(int labelIndex);

    /**
     * Run inference using the prepared input in {@link #imgData}. Afterwards, the result will be
     * provided by getProbability().
     *
     * <p>This additional method is necessary, because we don't have a common base for different
     * primitive data types.
     */
    protected abstract void runInference();

    /**
     * Get the total number of labels.
     *
     * @return
     */
  /*
  protected int getNumLabels() {
    return labelList.size();
  }
  */

    /**
     * Get the shape of output(heatmap) .
     *
     * @return
     */
    protected int getHeatmapWidth() {
        return HEATMAPWIGHT;
    }

    protected int getHeatmapHeight() {
        return HEATMAPHEIGHT;
    }

    protected int getNumJoint() {
        return NUMJOINT;
    }
}
