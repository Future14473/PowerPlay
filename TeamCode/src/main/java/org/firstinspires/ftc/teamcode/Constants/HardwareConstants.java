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
    public static int highGoal = 3000;
    public static int midGoal = 2000;
    public static int lowGoal = 1200;
    public static int junction = 250;

    public static int home = 0;
    public static int velocity = 5000;

//  claw
    public static double OUT_POS_CLAW = 0.1; // close pos
    public static double CLOSE_POS_CLAW = 0; // open pos
    public static double HOME_POS_COMPLIANT = 0.53;
    public static double COMPLIANT_INTAKE_SPEED = 0;
    public static double COMPLIANT_OUT_SPEED = 1;


//   v4b
    public static double HOME_POS_V4B = 0;
    public static double OUT_V4B = 0.4;

    public static int stack1 = 0;
    public static int stack2 = 0;
    public static int stack3 = 0;
    public static int stack4 = 0;
    public static int stack5 = 0;

    public static double AUTO_1_INTAKE = 0;
    public static double AUTO_2_INTAKE = 0;
    public static double AUTO_3_INTAKE = 0;
    public static double AUTO_4_INTAKE = 0;
    public static double AUTO_5_INTAKE = 0;

//  turret
    public static double HOME_POS_TURRET = 0;
    public static double OUT_POS_TURRET = 0.65;
    public static double MANUAL_INCREMENT_STEP = 0.05;


//  intake timings
    public static double TURRET_HOME = 500;
    public static double V4B_HOME = 800;
    public static double SLIDES_HOME = 500;

    public static double TURRET_OUT = 500;
    public static double SLIDES_OUT = 500;


}
