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
        servoTurret.setHome();

        waitForStart();

        while(opModeIsActive()) {
            if (gamepad1.x) {
                servoTurret.setHome();
            } if (gamepad1.b) {
                servoTurret.setOut();
            } if (gamepad1.left_stick_x > 0) {
                servoTurret.incrementUp();
            } if (gamepad1.left_stick_x < 0) {
                servoTurret.incrementDown();
            }

            telemetry.addData("turret position", servoTurret.getCurrPos());
            telemetry.update();
        }
    }
}
