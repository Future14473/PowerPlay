package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.technototes.library.hardware.servo.Servo;
import com.technototes.library.hardware.servo.ServoProfiler;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.MockServo;

@Disabled
@TeleOp
public class servoProfileTest extends LinearOpMode {

    Servo servo = new Servo(new MockServo()).startAt(0);
    ServoProfiler servoController;

    @Override
    public void runOpMode() throws InterruptedException {
        servoController = new ServoProfiler(servo).setConstraints(1, 0.6, 1);
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.x) {
                servoController.setTargetPosition(0);
            }
            if (gamepad1.a) {
                servoController.setTargetPosition(1);
            }
        }
    }


}
