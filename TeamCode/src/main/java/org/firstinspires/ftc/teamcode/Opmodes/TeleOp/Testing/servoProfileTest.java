package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.HOME_POS_V4B;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.OUT_V4B;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.technototes.library.hardware.servo.Servo;
import com.technototes.library.hardware.servo.ServoProfiler;


@TeleOp
public class servoProfileTest extends LinearOpMode {

    com.qualcomm.robotcore.hardware.Servo leftv4b;
    com.qualcomm.robotcore.hardware.Servo rightv4b;

    Servo servoLeft;
    Servo servoRight;
    ServoProfiler servoControllerLeft;
    ServoProfiler servoControllerRight;


    @Override
    public void runOpMode() throws InterruptedException {
        leftv4b = hardwareMap.get(com.qualcomm.robotcore.hardware.Servo.class, "leftv4b");
        rightv4b = hardwareMap.get(com.qualcomm.robotcore.hardware.Servo.class, "rightv4b");

        leftv4b.setDirection(com.qualcomm.robotcore.hardware.Servo.Direction.REVERSE);
        rightv4b.setDirection(com.qualcomm.robotcore.hardware.Servo.Direction.REVERSE);
        servoLeft = new Servo(leftv4b).startAt(0);
        servoRight = new Servo(leftv4b).startAt(0);

        servoControllerLeft = new ServoProfiler(servoLeft).setConstraints(10, 10, 10);
        servoControllerRight = new ServoProfiler(servoLeft).setConstraints(10, 10, 10);
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.x) {
                servoControllerLeft.setTargetPosition(HOME_POS_V4B);
                servoControllerRight.setTargetPosition(HOME_POS_V4B);
            }
            if (gamepad1.a) {
                servoControllerLeft.setTargetPosition(OUT_V4B);
                servoControllerRight.setTargetPosition(OUT_V4B);
            }
            servoControllerLeft.update();
            servoControllerRight.update();
        }
    }
}