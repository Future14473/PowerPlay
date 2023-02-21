package org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no;

import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.blueAllianceBlueStation;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.preloadReady;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.redAllianceRedStation;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.redAllianceRedStationPole;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.redAllianceRedStationStack;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.SLIDES_HOME;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.ComputerVision.AprilTag;
import org.firstinspires.ftc.teamcode.Hardware.Intake.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Outtake.Outtake;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.IntakeWheels;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Main;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;

@Autonomous
public class RedRR extends LinearOpMode {

    OpenCvCamera camera;
    AprilTag aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    int ONE = 1;
    int TWO = 2;
    int THREE = 3;

    AprilTagDetection tagOfInterest = null;

    SampleMecanumDrive drive;

    Slides slides;
    Claw claw;
    VirtualFourBar v4b;
    IntakeWheels intakeWheels;

    Timer timer;


    @Override
    public void runOpMode() {

        timer = new Timer(this);

        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(new Pose2d(-36, 63, Math.toRadians(-90)));

        //subsystems
        slides = new Slides(hardwareMap);
        claw = new Claw(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        intakeWheels = new IntakeWheels(hardwareMap);

        v4b.intake();
        slides.resetEncoders();
        claw.shutUp();
        intakeWheels.idle();

        telemetry.addLine("Subsystems Initialized\nWORK ALREADY");
        telemetry.update();

        waitForStart();


        slides.setCustom(1000);
        timer.safeDelay(150);
        v4b.intakeIdle();

        TrajectorySequence moveToPolePreload = drive.trajectorySequenceBuilder(blueAllianceBlueStation)
                .lineToLinearHeading(new Pose2d(-36,14, Math.toRadians(-45)))
                .lineTo(new Vector2d(-24,5))
                .addDisplacementMarker(preloadReady, () -> {
                    slides.extendHigh();
                })
                .build();
        drive.followTrajectorySequence(moveToPolePreload);

        v4b.setCustom(0.2);
        timer.safeDelay(250);
        claw.setCustom(0.52);
        timer.safeDelay(200);
        v4b.setCustom(0.75);
        slides.setCustom(170);

        TrajectorySequence moveToStack = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(new Pose2d(-56.6, 10, Math.toRadians(0)))
                .build();

        drive.followTrajectorySequence(moveToStack);
        claw.setCustom(0.52);
        v4b.setCustom(1.0);
        timer.safeDelay(250);
        claw.shutUp();
        timer.safeDelay(200);
        slides.setCustom(600);
        v4b.intakeIdle();


        TrajectorySequence moveToMidPole = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(new Pose2d(-24,21, Math.toRadians(45)))
                .addDisplacementMarker(10, () -> {
                    slides.extendMid();
                })
                .build();

        drive.followTrajectorySequence(moveToMidPole);

        v4b.setCustom(0.15);
        timer.safeDelay(500);
        claw.openWide();
        timer.safeDelay(250);
        v4b.setCustom(0.7);
        slides.setCustom(170);

        TrajectorySequence moveToStack2 = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(new Pose2d(-56.6, 10, Math.toRadians(0)))
                .build();


        drive.followTrajectorySequence(moveToStack2);
        v4b.setCustom(1.00);
        timer.safeDelay(250);
        claw.shutUp();
        timer.safeDelay(200);
        slides.setCustom(600);
        v4b.intakeIdle();

        TrajectorySequence moveToMidPole2 = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(new Pose2d(-27,21, Math.toRadians(45)))
                .addDisplacementMarker(10, () -> {
                    slides.extendMid();
                })
                .build();
        drive.followTrajectorySequence(moveToMidPole2);

        v4b.setCustom(0.15);
        timer.safeDelay(500);
        claw.openWide();
        timer.safeDelay(250);
        v4b.setCustom(0.62);
        slides.retract();

        TrajectorySequence moveToStack3 = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(new Pose2d(-56.6, 10, Math.toRadians(0)))
                .build();
        drive.followTrajectorySequence(moveToStack3);
        v4b.setCustom(0.9);
        timer.safeDelay(150);
        claw.shutUp();
        timer.safeDelay(200);
        slides.setCustom(600);
        v4b.intakeIdle();

        TrajectorySequence moveToLowPole = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(new Pose2d(-51, 17, Math.toRadians(45)))
                .build();

        drive.followTrajectorySequence(moveToLowPole);

        v4b.setCustom(0.15);
        timer.safeDelay(500);
        claw.openWide();
        timer.safeDelay(250);
        v4b.setCustom(0.7);
        slides.retract();

    }
}
