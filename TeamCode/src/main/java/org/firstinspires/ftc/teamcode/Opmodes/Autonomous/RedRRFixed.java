package org.firstinspires.ftc.teamcode.Opmodes.Autonomous;

import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.blueAllianceBlueStation;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.blueAllianceBlueStationPole;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.blueAllianceBlueStationStack;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.cycleReady;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.preloadReady;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.SLIDES_HOME;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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
public class RedRRFixed extends LinearOpMode {

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



        intake.teleopIntakeReady();
        intake.intake();

        telemetry.addLine("Subsystems Initialized\nPlease work you monkey");
        telemetry.update();

        waitForStart();
        drive.setPoseEstimate(blueAllianceBlueStation);


        TrajectorySequence moveToPolePreload = drive.trajectorySequenceBuilder(new Pose2d(0,0,0))
                .lineTo(new Vector2d(-34,8))
                .turn(Math.toRadians(-135))
                .lineToLinearHeading(new Pose2d(-60, 13, Math.toRadians(180)))

                .build();


        drive.followTrajectorySequence(moveToPolePreload);

/*
        TrajectorySequence moveToStack = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(blueAllianceBlueStationStack)
                .build();

        drive.followTrajectorySequence(moveToStack);
        timer.safeDelay(100);
        intake.intake();
        timer.safeDelay(100);
        slides.setCustom(slides.getHeight()+400);


        TrajectorySequence moveToPoleFromStack = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(blueAllianceBlueStationPole)
                .addDisplacementMarker(cycleReady, () -> {
                    outtake.outtakeReadyHigh(timer);
                })
                .build();

        drive.followTrajectorySequence(moveToPoleFromStack);

        //OUTTAKE SEQUENCE
        outtake.outtakeTeleOp(timer);
        intake.intake();
        slides.extendStack(2);
        timer.safeDelay(SLIDES_HOME);
        outtake.outtake();



        drive.followTrajectorySequence(moveToStack);
        timer.safeDelay(100);
        intake.intake();
        timer.safeDelay(100);
        slides.setCustom(slides.getHeight()+400);

        drive.followTrajectorySequence(moveToPoleFromStack);
        //OUTTAKE SEQUENCE
        outtake.outtakeTeleOp(timer);
        intake.intake();
        slides.extendStack(3);
        timer.safeDelay(SLIDES_HOME);
        outtake.outtake();

        drive.followTrajectorySequence(moveToStack);
        timer.safeDelay(100);
        intake.intake();
        timer.safeDelay(100);
        slides.setCustom(slides.getHeight()+400);

        drive.followTrajectorySequence(moveToPoleFromStack);
        //OUTTAKE SEQUENCE
        outtake.outtakeTeleOp(timer);
        intake.intake();
        slides.extendStack(4);
        timer.safeDelay(SLIDES_HOME);
        outtake.outtake();

//        timer.safeDelay(1000);*/


    }
}
