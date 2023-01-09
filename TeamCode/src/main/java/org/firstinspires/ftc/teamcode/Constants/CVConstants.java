package org.firstinspires.ftc.teamcode.Constants;

import com.acmerobotics.dashboard.config.Config;

import org.opencv.core.Scalar;

@Config
public class CVConstants {
    //CONE
    public static Scalar LOWR = new Scalar(110, 62, 20);
    public static Scalar HIGHR = new Scalar(179, 255, 255);

    //CONE
    public static Scalar LOWB = new Scalar(0, 151, 20);
    public static Scalar HIGHB = new Scalar(31, 222, 255);

    //POLE
    public static Scalar LOWY = new Scalar(11, 180, 134);
    public static Scalar HIGHY = new Scalar(26, 255, 255);

    public static int poleWidth = 35;
}
