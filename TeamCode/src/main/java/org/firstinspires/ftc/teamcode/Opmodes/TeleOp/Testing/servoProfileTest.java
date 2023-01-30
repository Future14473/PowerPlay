package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.technototes.library.hardware.servo.Servo;
import com.technototes.library.hardware.servo.ServoProfiler;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.MockServo;


@TeleOp
public class servoProfileTest extends LinearOpMode {

    com.qualcomm.robotcore.hardware.Servo servo1;

    Servo servo;
    ServoProfiler servoController;


    @Override
    public void runOpMode() throws InterruptedException {
        servo1 = hardwareMap.get(com.qualcomm.robotcore.hardware.Servo.class, "s");
        servo = new Servo(servo1).startAt(0);
        servoController = new ServoProfiler(servo).setConstraints(2, 3, 2);
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.x) {
                servoController.setTargetPosition(0);
            }
            if (gamepad1.a) {
                servoController.setTargetPosition(1);
            }
            servoController.update();
        }
    }
}