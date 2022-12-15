package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;


import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

//
@Config
@TeleOp(name = "DriveBASE", group = "Testing")
public class DriveBase extends LinearOpMode {
    SampleMecanumDrive drive;

    public static double YMult = 1.1;
    public static double mvmtMult = 1.0;
    public static double rotMult = 0.9;
    Slides slides;
    VirtualFourBar v4b;
    Timer timer;


    @Override
    public void runOpMode() throws InterruptedException {


        drive = new SampleMecanumDrive(hardwareMap);

        slides = new Slides(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        timer = new Timer(this);

        slides.resetEncoders();
        v4b.intake();

        waitForStart();

        drive();

        while (opModeIsActive()) {
            telemetryConfig(telemetry);
            if (gamepad1.x) {
                slides.retract();
                v4b.intake();
            }
            if (gamepad1.a) {
                slides.extendLow();
            } else if (gamepad1.b) {
                slides.extendMid();
            } else if (gamepad1.y) {
                slides.extendHigh();
                timer.safeDelay(1000);
                v4b.outtake();

            }
        }
    }


    @SuppressLint("DefaultLocale")
    public void telemetryConfig(Telemetry telemetry) {
        telemetry.update();
    }


    public void drive() {
        new Thread(() -> {
            while (opModeIsActive()) {
                drive.setWeightedDrivePower(
                        new Pose2d(
                                -gamepad1.left_stick_y,
                                gamepad1.left_stick_x,
                                -gamepad1.right_stick_x
                        )
                );
            }
        }).start();
    }
}
