package org.firstinspires.ftc.teamcode.ComputerVision;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

@Config
public class YRCB extends OpenCvPipeline {
    Telemetry telemetry;
    Mat base = new Mat();
    Mat processed = new Mat();





    public YRCB(Telemetry t) {
        telemetry = t;

    }

    public static Scalar lowerBounds = new Scalar(0,142,20);
    public static Scalar upperBounds = new Scalar(255, 170, 90);

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, base, Imgproc.COLOR_RGB2YCrCb);
        Core.inRange(base, lowerBounds, upperBounds, processed);

        Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_OPEN, new Mat());
        Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_CLOSE, new Mat());

        Imgproc.GaussianBlur(processed, processed, new Size(5.0, 15.0), 0.00);

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(processed, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(input, contours, -1, new Scalar(0,255, 0));



        return input;


    }
}
