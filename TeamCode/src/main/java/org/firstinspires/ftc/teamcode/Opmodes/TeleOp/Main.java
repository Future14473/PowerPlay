package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Intake.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Outtake.Outtake;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ClawCompliant;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;


@TeleOp(name = "Main", group = "Linear Opmode")
public class Main extends LinearOpMode {
    SampleMecanumDrive drive;

    Timer timer = new Timer(this);

    private final FtcDashboard dashboard = FtcDashboard.getInstance();


    Slides slides;
    ClawCompliant claw;
    VirtualFourBar v4b;
    ServoTurret servoTurret;

    Intake intake;
    Outtake outtake;

    @Override
    public void runOpMode() throws InterruptedException {

        //drivetrain
        drive = new SampleMecanumDrive(hardwareMap);

        //subsystems
        slides = new Slides(hardwareMap);
        claw = new ClawCompliant(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        servoTurret = new ServoTurret(hardwareMap);

        intake = new Intake(slides, claw, v4b, servoTurret);
        outtake = new Outtake(slides, claw, v4b, servoTurret);

        intake.teleopIntakeReady();

        waitForStart();

        drive();

        while (opModeIsActive()) {

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
