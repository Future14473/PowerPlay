package org.firstinspires.ftc.teamcode.Opmodes.Autonomous;

import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.blueAllianceBlueStation;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.blueAllianceBlueStationPole;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.blueAllianceBlueStationStack;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.cycleReady;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.preloadReady;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.resetFromPole;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware.Intake.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Outtake.Outtake;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
public class RedRR extends LinearOpMode {

    SampleMecanumDrive drive;

    Slides slides;
    Claw claw;
    VirtualFourBar v4b;
    ServoTurret servoTurret;

    Intake intake;
    Outtake outtake;

    Timer timer;

    @Override
    public void runOpMode() {

        timer = new Timer(this);

        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(blueAllianceBlueStation);

        slides = new Slides(hardwareMap);
        claw = new Claw(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        servoTurret = new ServoTurret(hardwareMap);

        intake = new Intake(slides, claw, v4b, servoTurret);
        outtake = new Outtake(slides, claw, v4b, servoTurret);

        intake.teleopIntakeReady();
        intake.intake();

        telemetry.addLine("Subsystems Initialized\nPlease work you monkey");
        telemetry.update();

        waitForStart();



        slides.setCustom(400);
        timer.safeDelay(150);



        TrajectorySequence moveToPolePreload = drive.trajectorySequenceBuilder(blueAllianceBlueStation)
                .lineToLinearHeading(blueAllianceBlueStationPole)
                .addDisplacementMarker(preloadReady, () -> {
                    outtake.outtakeReadyHigh(timer);
                })
                .build();

        drive.followTrajectorySequence(moveToPolePreload);
        timer.safeDelay(1000);
        outtake.outtakeAuto(timer);

        TrajectorySequence moveToStack = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(blueAllianceBlueStationStack)
                .addDisplacementMarker(resetFromPole, () -> {
                    intake.autoIntakeReady(1, timer);
                })
                .build();

        drive.followTrajectorySequence(moveToStack);
        timer.safeDelay(1000);
        intake.intake();
        slides.setCustom(slides.getHeight()+250);

        TrajectorySequence moveToPoleFromStack = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(blueAllianceBlueStationPole)
                .addDisplacementMarker(cycleReady, () -> {
                    outtake.outtakeReadyHigh(timer);
                })
                .build();

        drive.followTrajectorySequence(moveToPoleFromStack);
        timer.safeDelay(1000);
        outtake.outtakeAuto(timer);

        TrajectorySequence reset = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(blueAllianceBlueStation)
                .addDisplacementMarker(5, () -> {
                    intake.teleopIntake(timer);
                })
                .build();

        drive.followTrajectorySequence(reset);
    }
}
