# Body Pose Estimation - Android

Copyright (C) 2019 여름방학에는 인턴해야지

## Features

Perform pose matching by inferencing body pose estimation model on Android device

- **Lightweight Model**. Inference Tensorflow Lite model that can be used lightly on mobile devices.
- **Pose Matching**. Perform pose matching by calculating the angle of each joint point.
- **Pose Registration**. Register a pose that will be used on pose matching.

## DEMOs

<img src=".\resource\screenshot.png" width=800 />

## Requirements

- Android Studio 3.1.x
- Gradle 3.1.x
- Android Support Library, revision 21 or newer

## Algorithm

### Pose Matching

The angles of the six edges of the input upper body are extracted and the angle and accuracy of the skeleton to be matched are measured.

<img src="./resource/skeleton-b716e4ec-d644-4af5-b493-eab736d88aa3.png" width=200 />

**Angle calculation**

```java
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
```

**Accuracy calculation**

```java
private double getPersentage(double compareNumber, double betweenAngle) {
    return (((HALF_DEGREE - Math.abs(betweenAngle - compareNumber))) / HALF_DEGREE) * 100;
}
```

## Contribution

1. Fork it (http://github.com/koomg9599/BodyPoseEstimationAndroid)
2. Create your feature branch (git checkout -b feature/fooBar)
3. Commit your changes (git commit -am 'Add some fooBar')
4. Push to the branch (git push origin feature/fooBar)
5. Create a new Pull Request

## Reference
- [https://github.com/dongseokYang/Body-Pose-Estimation-Android-gpu](https://github.com/dongseokYang/Body-Pose-Estimation-Android-gpu)
- [https://github.com/edvardHua/PoseEstimationForMobile](https://github.com/edvardHua/PoseEstimationForMobile)
- [https://www.tensorflow.org/lite/models/pose_estimation/overview](https://www.tensorflow.org/lite/models/pose_estimation/overview)

## License

[Apache License 2.0](LICENSE)
