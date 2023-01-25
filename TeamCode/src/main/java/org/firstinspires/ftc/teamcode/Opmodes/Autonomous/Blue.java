package org.firstinspires.ftc.teamcode.Opmodes.Autonomous;


import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.ComputerVision.AprilTag;
import org.firstinspires.ftc.teamcode.Hardware.Intake.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Outtake.Outtake;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;

import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Autonomous
public class Blue extends LinearOpMode
{
    OpenCvCamera camera;
    AprilTag aprilTagDetectionPipeline;

    Slides slides;
    Claw claw;
    VirtualFourBar v4b;
    ServoTurret servoTurret;

    Intake intake;
    Outtake outtake;


    @Override
    public void runOpMode()
    {
        Timer timer = new Timer(this);
        slides = new Slides(hardwareMap);
        claw = new Claw(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        servoTurret = new ServoTurret(hardwareMap);

        intake = new Intake(slides, claw, v4b, servoTurret);
        outtake = new Outtake(slides, claw, v4b, servoTurret);

        intake.teleopIntakeReady();

        intake.intake();


//        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
//        drive.setLocalizer(new TwoWheelTrackingLocalizer(hardwareMap, drive));
//
//
//        Pose2d startPos = new Pose2d(-35, 72, Math.toRadians(-179.9999999));
//
//        TrajectorySequence dropPreload = drive.trajectorySequenceBuilder(startPos)
//                .strafeTo(new Vector2d(-35, 0))
//
//                //prepare to outtake preload
//                .addDisplacementMarker(20, () -> {
//                    outtake.outtakeReadyHigh(timer);
//                })
//
//                .addDisplacementMarker(71.9999999999, () -> {
//                    outtake.outtake();
//                })
//                .build();
//
//        TrajectorySequence preloadToStack = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
//                .lineTo(new Vector2d(-60, 14))
//                .build();
//
//        drive.followTrajectorySequence(dropPreload);
//        new Thread(() -> {intake.autoIntakeReady(5, timer);}).start();
//        drive.followTrajectorySequence(preloadToStack);

//
//        if (tagOfInterest.id == LEFT) {
//            claw.openWide();
//            drive.followTrajectorySequence(ParkSequences.createParkLeft(drive));
//        } else if (tagOfInterest == null || tagOfInterest.id == MIDDLE) {
//            claw.openWide();
//            drive.followTrajectorySequence(ParkSequences.createParkMiddle(drive));
//
//        } else if (tagOfInterest.id == RIGHT) {
//            claw.openWide();
//            drive.followTrajectorySequence(ParkSequences.createParkRight(drive));
//        }

    }

}