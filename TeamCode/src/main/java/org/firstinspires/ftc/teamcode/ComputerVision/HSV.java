package org.firstinspires.ftc.teamcode.ComputerVision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class HSV extends OpenCvPipeline {

    @Override
    public Mat processFrame(Mat input) {
        Mat mat = new Mat();

        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
        if (mat.empty()) return input;

        Scalar lowHSV = new Scalar(20, 70, 80); //lenient
        Scalar highHSV = new Scalar(32, 255, 255); //lenient

        Mat thresh = new Mat();

        Core.inRange(mat, lowHSV, highHSV, thresh);

        Mat masked = new Mat();

        Core.bitwise_and(mat, mat, masked, thresh);

        Scalar average = Core.mean(masked, thresh);

        Mat scaledMask = new Mat();

        masked.convertTo(scaledMask, -1, 150 / average.val[1], 0);

        Scalar strictLowHSV = new Scalar(0, 150, 100);
        Scalar strictHighHSV = new Scalar(255, 255, 255);

        Mat scaledThresh = new Mat();

        Core.inRange(scaledMask, strictLowHSV, strictHighHSV, scaledThresh);

        Mat finalMask = new Mat();

        Core.bitwise_and(mat, mat, finalMask, scaledThresh);

        Mat edges = new Mat();

        Imgproc.Canny(finalMask, edges, 100, 200);


        scaledThresh.release();
        scaledMask.release();
        mat.release();
        masked.release();
        edges.release();
        thresh.release();
        finalMask.release();

        return finalMask;

    }
}
