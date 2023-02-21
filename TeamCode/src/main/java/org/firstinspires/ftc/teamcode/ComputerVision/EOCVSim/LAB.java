package org.firstinspires.ftc.teamcode.ComputerVision.EOCVSim;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class LAB extends OpenCvPipeline {
    Mat base = new Mat();
    Mat processed = new Mat();
    List<Mat> lab = new ArrayList<>();

    Mat L;
    Mat a;
    Mat b;

    private final Object sync = new Object();

    public static Scalar lowerBoundsB = new Scalar(0,46,42);
    public static Scalar upperBoundsB = new Scalar(255, 255, 144);

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, base, Imgproc.COLOR_RGB2Lab);
        Core.split(base, lab);
        L = lab.get(0);
        a = lab.get(1);
        b = lab.get(2);

        Imgproc.threshold(a, processed, 127, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY_INV);
        Core.merge(lab, processed);

        Imgproc.cvtColor(processed, processed, Imgproc.COLOR_Lab2BGR);
        Imgproc.cvtColor(processed, processed, Imgproc.COLOR_BGR2YCrCb);

        Core.inRange(base, lowerBoundsB, upperBoundsB, processed);

        Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_OPEN, new Mat());
        Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_CLOSE, new Mat());


        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(processed, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);



        synchronized (sync) {
            for (MatOfPoint contour : contours) {
                Point[] contourArray = contour.toArray();

                if (contourArray.length >= 20) {
                    Rect rect = Imgproc.boundingRect(contour);
                    if (rect.area() > 3000) {
                        Imgproc.rectangle(input, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 1);
                        Imgproc.drawContours(input, contours, -1, new Scalar(255, 0, 0));
                    }
                }
            }

            return processed;
        }


    }
}
