package org.firstinspires.ftc.teamcode.Constants;

import com.acmerobotics.dashboard.config.Config;

import org.opencv.core.Scalar;

@Config
public class CVConstants {
    //pink
    public static Scalar LOWR = new Scalar(110, 62, 20);
    public static Scalar HIGHR = new Scalar(179, 255, 255);
    //green
    public static Scalar LOWG = new Scalar(36, 155, 20);
    public static Scalar HIGHG = new Scalar(79, 241, 255);
    //orange
    public static Scalar LOWB = new Scalar(0, 151, 20);
    public static Scalar HIGHB = new Scalar(31, 222, 255);
    //yellow
    // todo tune this
    public static Scalar LOWY = new Scalar(0, 151, 20);
    public static Scalar HIGHY = new Scalar(31, 222, 255);
}
