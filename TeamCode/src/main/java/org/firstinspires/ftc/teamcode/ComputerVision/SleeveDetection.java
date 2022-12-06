package org.firstinspires.ftc.teamcode.ComputerVision;


import static org.firstinspires.ftc.teamcode.Constants.CVConstants.HIGHB;
import static org.firstinspires.ftc.teamcode.Constants.CVConstants.HIGHG;
import static org.firstinspires.ftc.teamcode.Constants.CVConstants.HIGHR;
import static org.firstinspires.ftc.teamcode.Constants.CVConstants.HIGHY;
import static org.firstinspires.ftc.teamcode.Constants.CVConstants.LOWB;
import static org.firstinspires.ftc.teamcode.Constants.CVConstants.LOWG;
import static org.firstinspires.ftc.teamcode.Constants.CVConstants.LOWR;
import static org.firstinspires.ftc.teamcode.Constants.CVConstants.LOWY;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;


public class SleeveDetection extends OpenCvPipeline {
    Telemetry telemetry;
    Mat base = new Mat();
    Mat matR = new Mat();
    Mat matB = new Mat();
    Mat matG = new Mat();
    Mat matY = new Mat();


    public SleeveDetection(Telemetry t) {
        telemetry = t;
    }

    Scalar lowThreshR = LOWR;
    Scalar highThreshR = HIGHR;
    Scalar lowThreshG = LOWG;
    Scalar highThreshG = HIGHG;
    Scalar lowThreshB = LOWB;
    Scalar highThreshB = HIGHB;
    Scalar lowThreshY = LOWY;
    Scalar highThreshY = HIGHY;


    Rect maxRect;
    double x, y, w, h;
    double centerX, centerY;
    double frameCenter;


    public static int MINAREA = 500;
    private static int MINFILTER = 500;
    private static int MINPOLEFILTER = 300;

    private double maxAreaR;
    private double maxAreaG;
    private double maxAreaB;
    private  double maxAreaY;
    private Rect maxRectR = new Rect();
    private Rect maxRectG = new Rect();
    private Rect maxRectB = new Rect();
    private Rect maxRectY = new Rect();

    boolean foundPole = false;

    public enum SleeveColor {
        PINK, GREEN, ORANGE, NONE
    }

    private SleeveColor sleeveColor;


    private final Object sync = new Object();


    @Override
    public Mat processFrame(Mat input) {
        maxAreaR = 0;
        maxAreaG = 0;
        maxAreaB = 0;
        maxAreaY = 0;
        // IMAGE CLEANUP

        // Convert img to HSV
        Imgproc.cvtColor(input, base, Imgproc.COLOR_RGB2HSV);

        // outputs objects that are in rage of the hsv values given
        Core.inRange(base, lowThreshR, highThreshR, matR);
        Core.inRange(base, lowThreshG, highThreshG, matG);
        Core.inRange(base, lowThreshB, highThreshB, matB);
        Core.inRange(base, lowThreshY, highThreshY, matY);


        //threshold after hsv for initial noise removal
        Imgproc.threshold(matR, matR, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.threshold(matG, matG, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.threshold(matB, matB, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.threshold(matY, matY, 0, 255, Imgproc.THRESH_OTSU);

        // remove noise
        Imgproc.morphologyEx(matR, matR, Imgproc.MORPH_OPEN, new Mat());
        Imgproc.morphologyEx(matR, matR, Imgproc.MORPH_CLOSE, new Mat());
        Imgproc.morphologyEx(matG, matG, Imgproc.MORPH_OPEN, new Mat());
        Imgproc.morphologyEx(matG, matG, Imgproc.MORPH_CLOSE, new Mat());
        Imgproc.morphologyEx(matB, matB, Imgproc.MORPH_OPEN, new Mat());
        Imgproc.morphologyEx(matB, matB, Imgproc.MORPH_CLOSE, new Mat());
        Imgproc.morphologyEx(matY, matY, Imgproc.MORPH_OPEN, new Mat());
        Imgproc.morphologyEx(matY, matY, Imgproc.MORPH_CLOSE, new Mat());

        // blurring - reduces clarity of detections though - use only if high noise
//        Imgroc.GaussianBlur(mat, mat, new Size(5.0, 15.0), 0.00);

        // final thresholding to sharpen the blurs
        Imgproc.threshold(matR, matR, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.threshold(matG, matG, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.threshold(matB, matB, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.threshold(matY, matY, 0, 255, Imgproc.THRESH_OTSU);

        // contour detection
        List<MatOfPoint> contoursR = new ArrayList<>();
        Imgproc.findContours(matR, contoursR, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//        Imgproc.drawContours(matR, contoursR, -1, new Scalar(255, 0, 0));
        List<MatOfPoint> contoursG = new ArrayList<>();
        Imgproc.findContours(matG, contoursG, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//        Imgproc.drawContours(matG, contoursG, -1, new Scalar(255, 0, 0));
        List<MatOfPoint> contoursB = new ArrayList<>();
        Imgproc.findContours(matB, contoursB, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//        Imgproc.drawContours(matB, contoursB, -1, new Scalar(255, 0, 0));
        List<MatOfPoint> contoursY = new ArrayList<>();
        Imgproc.findContours(matY, contoursY, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        // draw bounding boxes and get cords
        synchronized (sync) {
            for (MatOfPoint contour : contoursR) {
//              double area = Imgproc.contourArea(contour);
                Point[] contourArray = contour.toArray();
                if (contourArray.length >= 15) {
                    Rect rect = Imgproc.boundingRect(contour);

                    //Imgproc.rectangle(input, new Point(x, y), new Point(x + w, y + h), new Scalar(0, 255, 0), 2);

                    if (rect.area() > maxAreaR) {
                        maxAreaR = rect.area();
                        maxRectR = rect;
                    }
                }
            }
            for (MatOfPoint contour : contoursG) {
//              double area = Imgproc.contourArea(contour);
                Point[] contourArray = contour.toArray();
                if (contourArray.length >= 15) {
                    Rect rect = Imgproc.boundingRect(contour);

                    //Imgproc.rectangle(input, new Point(x, y), new Point(x + w, y + h), new Scalar(0, 255, 0), 2);

                    if (rect.area() > maxAreaG) {
                        maxAreaG = rect.area();
                        maxRectG = rect;
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

        // todo change to height if incorrect center
        if (maxAreaY >= MINPOLEFILTER) {
            foundPole = true;
            centerX = (maxRectY.tl().x+maxRectY.br().x)/2;
            frameCenter = input.width()/2;

        } else {
            foundPole = false;
        }

        // for park
        DoubleStream areas = DoubleStream.of(maxAreaR, maxAreaG, maxAreaB);
        OptionalDouble avgArea = areas.average();
        if (avgArea.getAsDouble() > MINFILTER) {


            // Red sleeve
            if (maxAreaR >= maxAreaG && maxAreaR >= maxAreaB) {
                maxRect = maxRectR;
                sleeveColor = SleeveColor.PINK;
            }
            // Green sleeve
            else if (maxAreaG >= maxAreaR && maxAreaG >= maxAreaB) {
                maxRect = maxRectG;
                sleeveColor = SleeveColor.GREEN;
            }
            // Blue sleeve
            else if (maxAreaB > maxAreaR && maxAreaB >= maxAreaG) {
                maxRect = maxRectB;
                sleeveColor = SleeveColor.ORANGE;
            }
        } else {
            sleeveColor = SleeveColor.NONE;
        }

        //further processing for localization (not necessary here)
        if (maxRect.area() > MINAREA) {
            Imgproc.rectangle(input, maxRect, new Scalar(0, 255, 0), 1);
        }

        telemetry.addData("Sleeve Color", sleeveColor);
        telemetry.update();

        return input;

    }

    public double lockOnCV () {
        double dist = 0;
        synchronized (sync) {
            if (foundPole) {
                if (centerX + 10 > frameCenter) {
                    dist = centerX - frameCenter;
                } else if (centerX - 10 < frameCenter) {
                    dist = centerX - frameCenter;
                }
            }
            return dist;
        }
    }

//    public double getCenterX() {
//        synchronized (sync) {
//            return centerX;
//        }
//    }

//    public double getCenterY() {
//        synchronized (sync) {
//            return centerY;
//        }
//    }

    public SleeveColor getSleeveColor() {
        return sleeveColor;
    }

}

