package org.firstinspires.ftc.teamcode.Constants;

import com.acmerobotics.dashboard.config.Config;

@Config
public class HardwareConstants {

//  drivetrain
    public static double rotMult = 0.7;
    public static double mvmtMult = 0.9;

//  all servos
    public static double MAX_ROTATION_DEGREES = 330;

//  slides
    public static int highGoal = 2000;
    public static int midGoal = 2000;
    public static int lowGoal = 1200;
    public static int junction = 250;
    public static int stack1 = 0;
    public static int stack2 = 0;
    public static int stack3 = 0;
    public static int stack4 = 0;
    public static int stack5 = 0;

    public static int home = 0;
    public static int velocity = 7000;

//   v4b
    public static double HOME_POS_V4B = 0;
    public static double OUT_V4B = 0.7;

    public static double AUTO_1_INTAKE = 0;
    public static double AUTO_2_INTAKE = 0;
    public static double AUTO_3_INTAKE = 0;
    public static double AUTO_4_INTAKE = 0;
    public static double AUTO_5_INTAKE = 0;

//   turret
    public static double HOME_POS_TURRET = 0;
    public static double OUT_POS_TURRET = 0.5;
    public static double MANUAL_INCREMENT_STEP = 0.05;


//   claw
    public static double OUT_POS_CLAW = 0.05;
    public static double CLOSE_POS_CLAW = 0.3;
    public static double HOME_POS_COMPLIANT = 0.53;
    public static double COMPLIANT_INTAKE_SPEED = 1;
    public static double COMPLIANT_OUT_SPEED = 1;

//  intake timings
    public static long TURRET_HOME = 2000;
    public static long V4B_HOME = 2000;
    public static long SLIDES_HOME = 2000;

    public static long TURRET_OUT = 2000;
    public static long SLIDES_OUT = 2000;


}
