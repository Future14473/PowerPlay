package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;


import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

//
@Config
@Disabled
@TeleOp(name = "PowerPlayFirstLeague", group = "Linear Opmode")
public class MainwV4Bar extends LinearOpMode {
    SampleMecanumDrive drive;


    private final FtcDashboard dashboard = FtcDashboard.getInstance();


    ServoTurret servoTurret;
    Slides slides;
    VirtualFourBar v4b;
    Claw claw;


    Timer timer = new Timer(this);

    @Override
    public void runOpMode() throws InterruptedException {

        drive = new SampleMecanumDrive(hardwareMap);


        // subsystems init
        servoTurret = new ServoTurret(hardwareMap);
        servoTurret.setHome();
        slides = new Slides(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        claw = new Claw(hardwareMap);

        boolean intakeReady = false;

        waitForStart();

        drive();

        while (opModeIsActive()) {

            //intaking
            if (gamepad2.x && !intakeReady) {
                slides.resetEncoders();
                v4b.intake();
                servoTurret.setHome();
                claw.openWide();

                intakeReady = true;

            } else if (gamepad2.x && intakeReady) {
                claw.shutUp();
            }

            //outtaking
            if (gamepad2.a) {
                synchronousmovement(1);
                v4b.outHigh();
                claw.openWide();

            }
            if (gamepad2.b) {
                synchronousmovement(2);
                v4b.outMid();
                claw.openWide();
            }
            if (gamepad2.y) {
                synchronousmovement(3);
                v4b.outHigh();
                claw.openWide();
            }

            if (gamepad2.right_trigger > 0) {
                claw.openWide();
            }

            //servo turret ajustments
            if (gamepad2.right_stick_x > 0) {
                servoTurret.incrementUp();
            } else if (gamepad2.right_stick_x < 0) {
                servoTurret.incrementDown();
            }

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
                double y = gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x);
                double x = -gamepad1.left_stick_y * Math.abs(gamepad1.left_stick_y);
                double rx = -gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x);

                double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                double leftFront = ((y + x + rx) / denominator);
                double leftRear = ((y - x + rx) / denominator);
                double rightRear = ((y + x - rx) / denominator);
                double rightFront = ((y - x - rx) / denominator);

                drive.setMotorPowers(leftFront, leftRear, rightRear, rightFront);
            }
        }).start();
    }
    public void synchronousmovement(int a){
        new Thread(() -> {
            if (a == 1){
            slides.extendHigh();}
            if (a == 2){
                slides.extendMid();}
            if (a == 3){
                slides.extendLow();}
        }).start();
    }
}
