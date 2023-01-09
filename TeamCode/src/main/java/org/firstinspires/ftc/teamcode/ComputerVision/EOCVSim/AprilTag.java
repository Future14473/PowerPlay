package org.firstinspires.ftc.teamcode.ComputerVision.EOCVSim;



import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.apriltag.AprilTagDetectorJNI;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class AprilTag extends OpenCvPipeline {

// 106 for every 50 mm
    //2.12 pixels for every mm
    //cone/pole detection

    Mat base = new Mat();
    Mat matR = new Mat();
    Mat matB = new Mat();
    Mat matY = new Mat();

    public static Scalar lowThreshR = new Scalar(110, 62, 20);
    public static Scalar highThreshR = new Scalar(179, 255, 255);
    public static Scalar lowThreshB = new Scalar(0, 151, 20);
    public static Scalar highThreshB = new Scalar(31, 222, 255);
    public static Scalar lowThreshY = new Scalar(21, 100, 0);
    public static Scalar highThreshY = new Scalar(34, 175, 255);

    double centerXY;
    double centerXR;
    double centerXB;
    double frameCenter;

    private static int MINPOLEFILTER = 300;
    private static int MINCONEFILTER = 0;

    private double maxAreaR;
    private double maxAreaB;
    private  double maxAreaY;
    private Rect maxRectR = new Rect();
    private Rect maxRectG = new Rect();
    private Rect maxRectB = new Rect();
    private Rect maxRectY = new Rect();

    boolean foundPole = false;
    boolean foundConeRed = false;
    boolean foundConeBlue = false;

    private final Object sync = new Object();

    Telemetry telemetry;


    public AprilTag(Telemetry telemetry) {
        this.telemetry = telemetry;
    }
    @Override
    public Mat processFrame(Mat input) {
        maxAreaY = 0;
        Imgproc.cvtColor(input, base, Imgproc.COLOR_RGB2HSV);

        //AFTER apriltag stuff is done

        Core.inRange(base, lowThreshR, highThreshR, matR);
        Core.inRange(base, lowThreshB, highThreshB, matB);
        Core.inRange(base, lowThreshY, highThreshY, matY);

        //threshold after hsv for initial noise removal
        Imgproc.threshold(matR, matR, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.threshold(matB, matB, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.threshold(matY, matY, 0, 255, Imgproc.THRESH_OTSU);

        // remove noise
        Imgproc.morphologyEx(matR, matR, Imgproc.MORPH_OPEN, new Mat());
        Imgproc.morphologyEx(matR, matR, Imgproc.MORPH_CLOSE, new Mat());
        Imgproc.morphologyEx(matB, matB, Imgproc.MORPH_OPEN, new Mat());
        Imgproc.morphologyEx(matB, matB, Imgproc.MORPH_CLOSE, new Mat());
        Imgproc.morphologyEx(matY, matY, Imgproc.MORPH_OPEN, new Mat());
        Imgproc.morphologyEx(matY, matY, Imgproc.MORPH_CLOSE, new Mat());

        // contour detection
        List<MatOfPoint> contoursR = new ArrayList<>();
        Imgproc.findContours(matR, contoursR, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        List<MatOfPoint> contoursB = new ArrayList<>();
        Imgproc.findContours(matB, contoursB, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        List<MatOfPoint> contoursY = new ArrayList<>();
        Imgproc.findContours(matY, contoursY, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);



        // draw bounding boxes and get cords
        synchronized (sync) {
            for (MatOfPoint contour : contoursR) {
//              double area = Imgproc.contourArea(contour);
                Point[] contourArray = contour.toArray();
                if (contourArray.length >= 15) {
                    Rect rect = Imgproc.boundingRect(contour);


                    if (rect.area() > maxAreaR) {
                        maxAreaR = rect.area();
                        maxRectR = rect;
                    }
                }
            }

            for (MatOfPoint contour : contoursB) {
//              double area = Imgproc.contourArea(contour);
                Point[] contourArray = contour.toArray();
                if (contourArray.length >= 15) {
                    Rect rect = Imgproc.boundingRect(contour);
                    if (rect.area() > maxAreaB) {
                        maxAreaB = rect.area();
                        maxRectB = rect;
                    }
                }
            }

            for (MatOfPoint contour : contoursY) {
                Point[] contourArray = contour.toArray();
                if (contourArray.length >= 15) {
                    Rect rect = Imgproc.boundingRect(contour);

                    if (rect.area() > maxAreaY) {
                        maxAreaY = rect.area();
                        maxRectY = rect;
                    }
                }
            }
        }

        //get center X of item to lock on to
        Imgproc.rectangle (input, maxRectY.tl(), maxRectY.br(), new Scalar(255,0,0),2);
        // todo change to height if incorrect center
        if (maxAreaY >= MINPOLEFILTER) {
            foundPole = true;
            centerXY = (maxRectY.tl().x+maxRectY.br().x)/2;
            frameCenter = input.width()/2;

            double dist = 0;
            if (centerXY + 10 > frameCenter) {
                dist = centerXY - frameCenter;
            } else if (centerXY - 10 < frameCenter) {
                dist = centerXY - frameCenter;
            }



            telemetry.addData("distPIXELS", dist);
            telemetry.addData("distMM", dist/2.12);
            telemetry.addData("rectWidth", maxRectY.width);
            telemetry.update();



        } else {
            foundPole = false;
        }

        // todo change to height if incorrect center
        if (maxAreaR >= MINCONEFILTER) {
            foundConeRed = true;
            centerXR = (maxRectR.tl().x+maxRectR.br().x)/2;
            frameCenter = input.width()/2;

        } else {
            foundConeRed = false;
        }
        if (maxAreaB >= MINCONEFILTER) {
            foundConeBlue = true;
            centerXB = (maxRectR.tl().x+maxRectR.br().x)/2;
            frameCenter = input.width()/2;

        } else {
            foundConeBlue = false;
        }
        return input;
    }

    /**
     * @param item 0 pole; 1 red; 2 blue
     */
    public double lockOnCV (int item) {
        double dist = 0;
        synchronized (sync) {


            if (item == 1) {
                if (centerXR + 10 > frameCenter) {
                    dist = centerXR - frameCenter;
                } else if (centerXR - 10 < frameCenter) {
                    dist = centerXR - frameCenter;
                }
            }
            if (item == 2) {
                if (centerXB + 10 > frameCenter) {
                    dist = centerXB - frameCenter;
                } else if (centerXB - 10 < frameCenter) {
                    dist = centerXB - frameCenter;
                }
            }


            return dist;
        }
    }
}