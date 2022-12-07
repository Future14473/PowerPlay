package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;


import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Hardware.Intake.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Outtake.Outtake;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ClawCompliant;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

//
@Config
@TeleOp(name = "TeleOpPowerPlay", group = "Testing")
public class TeleOpPowerPlay extends LinearOpMode {
    SampleMecanumDrive drive;

    public static double YMult = 1.1;
    public static double mvmtMult = 1.0;
    public static double rotMult = 0.7;

    public enum ClawStatus {
        CLAW_OPEN,
        CLAW_CLOSED
    }

    Slides slides;
    ClawCompliant claw;
    VirtualFourBar v4b;
    ServoTurret servoTurret;
    Timer timer = new Timer(this);

    Intake intake;
    Outtake outtake;

    @Override
    public void runOpMode() throws InterruptedException {


        drive = new SampleMecanumDrive(hardwareMap);

        waitForStart();

        drive();

        while (opModeIsActive()) {
            //drivetrain
            drive = new SampleMecanumDrive(hardwareMap);

            //subsystems
            slides = new Slides(hardwareMap);
            claw = new ClawCompliant(hardwareMap);
            v4b = new VirtualFourBar(hardwareMap);
            servoTurret = new ServoTurret(hardwareMap);

            intake = new Intake(slides, claw, v4b, servoTurret);
            outtake = new Outtake(slides, claw, v4b, servoTurret);

            ClawStatus status = ClawStatus.CLAW_OPEN;

            intake.teleopIntakeReady();

            waitForStart();

            drive();

            while (opModeIsActive()) {

                switch (status) {
                    case CLAW_OPEN:
                        if (gamepad1.right_bumper) {
                            intake.intake();
                            status = ClawStatus.CLAW_CLOSED;
                        }
                        break;
                    case CLAW_CLOSED:
                        if (gamepad1.left_bumper) {
                            outtake.outtake();
                            status = ClawStatus.CLAW_CLOSED;
                        }
                        break;


                }


                //bumpers open and close claw only
                if (gamepad1.right_bumper) {
                    outtake.outtake();
                }

                if (gamepad1.left_bumper) {
                    intake.intake();
                }

                if (gamepad1.x) {
                    intake.teleopIntake(timer);
                }

                if (gamepad1.dpad_down) {
                    outtake.outtakeReadyJunction();
                }
                if (gamepad1.a) {
                    outtake.outtakeReadyLow(timer);
                }

                if (gamepad1.b) {
                    outtake.outtakeReadyMid(timer);
                }

                if (gamepad1.y) {
                    outtake.outtakeReadyHigh(timer);
                }
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
