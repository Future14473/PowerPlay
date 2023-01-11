package org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no;

import com.acmerobotics.dashboard.config.Config;

@Config
public class AutoConstants {

    //distance to drop preload
    public static double preloadDistance = 52;
    //turn angle to drop preload
    public static double preloadAngle = 90;

    public static double moveBackIntoPole = 2;


    public static double moveStraightToStack = 22;

    public static double moveBackFromStack = 21.6;

    public static double turnToPole = -7;

    public static double moveBackIntoPoleCycle = 0.8;


    public static double strafeToStackFromPole = 15;

    public static double moveStraightFromPoleCycle = 5;

    public static double moveStraightToStackCycle = 28.8;

    //strafe weights
    public static double strafeY = 0.01;
    public static double strafeX = 0.6;
    public static double strafeR = -0.002;

    //linear weights
    public static double linearY = 0.65;
    public static double linearX = 0.01;
    public static double linearR = -0.005;

    //telop straight
    public static double teleOpMoveBackToPole = 5;
    public static double teleOpReset = 5;
    public static double teleOpMoveStraightToIntake = 10;
}
