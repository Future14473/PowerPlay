package org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.ComputerVision.AprilTag;
import org.firstinspires.ftc.teamcode.Hardware.Intake.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Outtake.Outtake;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.opmode.TwoWheelTrackingLocalizer;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;


import java.util.ArrayList;
@Disabled
@Autonomous
public class BlueLeft extends LinearOpMode
{
    Slides slides;
    Claw claw;
    ServoTurret servoTurret;
    VirtualFourBar v4b;
    Intake intake;
    Outtake outtake;
    Timer timer;

    @Override
    public void runOpMode() throws InterruptedException {
        slides = new Slides(hardwareMap);
        claw = new Claw(hardwareMap);
        servoTurret = new ServoTurret(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        intake = new Intake(slides, claw, v4b, servoTurret);
        outtake = new Outtake(slides, claw, v4b, servoTurret);
        timer = new Timer(this);

        intake.teleopIntakeReady();

        intake.intake();

        waitForStart();

        while(isStarted()){

            Pose2d startPos = new Pose2d(-35, 72, Math.toRadians(0));
            SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
            drive.setLocalizer(new TwoWheelTrackingLocalizer(hardwareMap, drive));

            TrajectorySequence toHigh = drive.trajectorySequenceBuilder(startPos)
                    .strafeRight(98)

                    .addDisplacementMarker(40, () -> {
                        outtake.outtakeReadyHigh(timer);
                    })
                    .addDisplacementMarker(97.99999999, () -> {
                        outtake.outtake();
                    })
                    .build();



            drive.followTrajectorySequence(toHigh);

//            Slides slides = new Slides(hardwareMap);
//            slides.extendHigh();
//            ServoTurret servoTurret = new ServoTurret(hardwareMap);
//            servoTurret.setOut();
//            VirtualFourBar virtualFourBar = new VirtualFourBar(hardwareMap);
//            virtualFourBar.outtake();
//            Claw claw = new Claw(hardwareMap);
//            claw.openWide();

            TrajectorySequence backitUp = drive.trajectorySequenceBuilder(new Pose2d(35, 12, Math.toRadians(270)))
                    .lineToLinearHeading(new Pose2d(35, 36, Math.toRadians(270)))
                    .build();


            TrajectorySequence left = drive.trajectorySequenceBuilder(new Pose2d(35, 36, Math.toRadians(270)))
                    .lineToLinearHeading(new Pose2d(11, 36, Math.toRadians(270)))
                    .build();

            TrajectorySequence middle = drive.trajectorySequenceBuilder(new Pose2d(35, 36, Math.toRadians(270)))
                    .lineToLinearHeading(new Pose2d(35, 36, Math.toRadians(270)))
                    .build();

            TrajectorySequence right = drive.trajectorySequenceBuilder(new Pose2d(35, 36, Math.toRadians(270)))
                    .lineToLinearHeading(new Pose2d(59, 36, Math.toRadians(270)))
                    .build();

        }

    }
}