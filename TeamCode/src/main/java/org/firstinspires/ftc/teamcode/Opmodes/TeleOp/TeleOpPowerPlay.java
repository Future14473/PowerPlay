package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;


import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

//
@Config
@TeleOp(name = "TeleOpPowerPlay", group = "Testing")
public class TeleOpPowerPlay extends LinearOpMode {
    SampleMecanumDrive drive;

    public static double YMult = 1.1;
    public static double mvmtMult = 1.0;
    public static double rotMult = 0.7;


    @Override
    public void runOpMode() throws InterruptedException {


        drive = new SampleMecanumDrive(hardwareMap);

        waitForStart();

        drive();

        while (opModeIsActive()) {
            telemetryConfig(telemetry);
        }
    }


    @SuppressLint("DefaultLocale")
    public void telemetryConfig(Telemetry telemetry) {
        telemetry.update();
    }


    public void drive() {
        new Thread(() -> {
            while (opModeIsActive()) {
                double y = -gamepad1.left_stick_y;
                double x = gamepad1.left_stick_x * YMult;
                double rx = gamepad1.right_stick_x * rotMult;

                double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                double leftFront = ((y + x + rx) / denominator) * mvmtMult;
                double leftRear = ((y - x + rx) / denominator) * mvmtMult;
                double rightRear = ((y + x - rx) / denominator) * mvmtMult;
                double rightFront = ((y - x - rx) / denominator) * mvmtMult;

                drive.setMotorPowers(leftFront * Math.abs(leftFront), leftRear * Math.abs(leftRear), rightRear * Math.abs(rightRear), rightFront * Math.abs(rightFront));
            }
        }).start();
    }
}
