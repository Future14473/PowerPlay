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
    public static int highGoal = 1700;
    public static int midGoal = 1000;
    public static int lowGoal = 1200;
    public static int junction = 250;

    public static int home = 0;
    public static int velocity = 6000;

    public static int stack1 = 400;
    public static int stack2 = 300;
    public static int stack3 = 250;
    public static int stack4 = 300;
    public static int stack5 = 0;

//  claw
    public static double OUT_POS_CLAW = 0.3; // close pos
    public static double CLOSE_POS_CLAW = 0.05; // open pos
    public static double HOME_POS_COMPLIANT = 0.53;
    public static double COMPLIANT_INTAKE_SPEED = 0;
    public static double COMPLIANT_OUT_SPEED = 1;


//   v4b
    public static double HOME_POS_V4B = 0;
    public static double OUT_V4B = 0.6;


//  turret
    public static double HOME_POS_TURRET = 0;
    public static double OUT_POS_TURRET = 0.65;
    public static double MANUAL_INCREMENT_STEP = 0.05;

// odo retraction
    public static double ODO_UP = 0;
    public static double ODO_DOWN = 0.5;


//  intake timings
    // OUTTAKE HIGH
    public static double V4B_OUT = 500;
    public static double TURRET_OUT = 200;


    public static double TURRET_HOME = 300;
    public static double V4B_HOME = 800;
    public static double SLIDES_HOME = 700;


    public static double SLIDES_OUT = 500;


}
