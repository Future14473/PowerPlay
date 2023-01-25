package org.firstinspires.ftc.teamcode.Constants;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;

@Config
public class AutoConstants {

    public static Pose2d blueAllianceBlueStation = new Pose2d(-36, 62, Math.toRadians(-90));

    public static Pose2d blueAllianceBlueStationPole = new Pose2d(-29.5, 14, Math.toRadians(135));

    public static Pose2d blueAllianceBlueStationStack = new Pose2d(-60, 16, Math.toRadians(180));

    public static Pose2d redAllianceRedStation = new Pose2d(-36, -62, Math.toRadians(90));

    public static Pose2d redAllianceRedStationPole = new Pose2d(-29.5, -14, Math.toRadians(135));

    public static Pose2d redAllianceRedStationStack = new Pose2d(-60, -16, Math.toRadians(180));    public static double preloadReady = 15;

    public static double cycleReady = 3;

    public static double resetFromPole = 5;

    //distance to drop preload
    public static double preloadDistance = 50;

    //prepare for preload outtake
    public static double prepareForPreloadOuttake = 30;

    //turn angle to drop preload
    public static double preloadAngle = 90;

    public static double moveBackIntoPole = 1.8;


    public static double moveStraightToStack = 22;

    public static double moveBackFromStack = 25;

    public static double turnToPole = 45;

    public static double moveBackIntoPoleCycle = 0.8;


    public static double strafeToStackFromPole = 10;

    public static double moveStraightFromPoleCycle = 5;

    public static double moveStraightToStackCycle = 22;

    //strafe weights
    public static double strafeY = 0.01;
    public static double strafeX = 0.6;
    public static double strafeR = -0.002;

    //linear weights
    public static double linearY = 0.71;
    public static double linearX = 0.01;
    public static double linearR = -0.005;

    //telop straight
    public static double teleOpMoveBackToPole = 5;
    public static double teleOpReset = 5;
    public static double teleOpMoveStraightToIntake = 10;
}
