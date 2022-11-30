package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;


import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.CLOSE_POS_CLAW;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.HIGH_V4B;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.HOME_POS_V4B;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.OUT_POS_CLAW;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

//
@Config
@TeleOp(name = "ServoTEST", group = "Testing")
public class ServoTEST extends LinearOpMode {

    Servo pointServo;
    Servo pointServo2;
    Servo clawServo;

    double HOME_POS = 0.2;

    @Override
    public void runOpMode() throws InterruptedException {

        // servo
        pointServo = hardwareMap.get(Servo.class, "rightv4b");
        pointServo2 = hardwareMap.get(Servo.class, "leftv4b");

        clawServo = hardwareMap.get(Servo.class, "openWide");


        pointServo.setPosition(HOME_POS);

        pointServo2.setPosition(HOME_POS);


        waitForStart();
        while (opModeIsActive()) {

            if(gamepad1.b) {
                clawServo.setPosition(CLOSE_POS_CLAW);
            } if (gamepad1.left_bumper) {
                clawServo.setPosition(OUT_POS_CLAW);
            }
            if (gamepad1.x) {
                pointServo.setPosition(HOME_POS_V4B);
                pointServo2.setPosition(HOME_POS_V4B);
            } if (gamepad1.y) {
                pointServo.setPosition(HIGH_V4B);
                pointServo2.setPosition(HIGH_V4B);
            }
        }
    }




}
