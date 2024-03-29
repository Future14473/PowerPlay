package org.firstinspires.ftc.teamcode.ComputerVision;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

@Config
public class YRCB extends OpenCvPipeline {
    Mat base = new Mat();
    Mat processed = new Mat();

    private final Object sync = new Object();

    public static Scalar lowerBounds = new Scalar(0,46,42);
    public static Scalar upperBounds = new Scalar(255, 255, 144);

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, base, Imgproc.COLOR_RGB2YCrCb);
        Core.inRange(base, lowerBounds, upperBounds, processed);

        Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_OPEN, new Mat());
        Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_CLOSE, new Mat());

        Imgproc.GaussianBlur(processed, processed, new Size(5.0, 15.0), 0.00);
        Imgproc.GaussianBlur(processed, processed, new Size(5.0, 5.0), 0.00);


        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(processed, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        synchronized (sync) {
            for (MatOfPoint contour : contours) {
                Point[] contourArray = contour.toArray();
                if (contourArray.length >= 15 && contourArray.length <= 85) {
                    Rect rect = Imgproc.boundingRect(contour);
                    if (rect.area() > 1000 && rect.area() < 3000) {
                        Imgproc.rectangle(input, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 1);
                    }

                }
            }
        }


        return input;


    }
}