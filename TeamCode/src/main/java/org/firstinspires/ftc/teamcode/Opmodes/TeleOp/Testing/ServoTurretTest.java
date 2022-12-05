package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;

@TeleOp
public class ServoTurretTest extends LinearOpMode {
    ServoTurret servoTurret;

    @Override
    public void runOpMode() throws InterruptedException {

        servoTurret = new ServoTurret(hardwareMap);

        waitForStart();

        while(opModeIsActive()) {
            if (gamepad1.x) {
                servoTurret.setHome();
            }

            if (gamepad1.b) {
                servoTurret.setOut();
            }

            if (gamepad1.dpad_right) {
                servoTurret.incrementUp();
            }

            if (gamepad1.dpad_left) {
                servoTurret.incrementDown();
            }

            telemetry.addData("turret position", servoTurret.getCurrPos());
            telemetry.update();
        }
    }
}
