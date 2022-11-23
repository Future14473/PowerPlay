package org.firstinspires.ftc.teamcode.Opmodes.Autonomous;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
public class AutoTEST extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Pose2d startPos = new Pose2d(-35, -72, Math.toRadians(0));

        TrajectorySequence traj = drive.trajectorySequenceBuilder(startPos)
                .lineToLinearHeading(new Pose2d(-35, -12, Math.toRadians(45)))
//                .lineToLinearHeading(new Pose2d(-60, -12, Math.toRadians(180)))
                .build();

        waitForStart();
        drive.followTrajectorySequence(traj);
        while (opModeIsActive() && !isStopRequested()) {}


    }
}
