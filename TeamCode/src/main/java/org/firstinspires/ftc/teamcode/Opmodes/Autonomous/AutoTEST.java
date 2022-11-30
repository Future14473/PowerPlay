package org.firstinspires.ftc.teamcode.Opmodes.Autonomous;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Disabled
@Config
@Autonomous(group = "drive")
public class AutoTEST extends LinearOpMode {
    public static double DISTANCE = 60;
    public static double DISPLACEMENT_DISTANCE = 30;
    public static double DEGREES = 45;
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Pose2d startPos = new Pose2d(-35, -72, Math.toRadians(0));

        TrajectorySequence traj = drive.trajectorySequenceBuilder(startPos)
                .forward(DISTANCE)
                .addDisplacementMarker(DISPLACEMENT_DISTANCE, () -> {
                    drive.turn(Math.toRadians(DEGREES));
                })
                .build();

        waitForStart();
        drive.followTrajectorySequence(traj);
        while (opModeIsActive() && !isStopRequested()) {}


    }
}
