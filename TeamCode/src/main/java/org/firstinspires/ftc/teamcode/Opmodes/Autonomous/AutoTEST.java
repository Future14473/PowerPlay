package org.firstinspires.ftc.teamcode.Opmodes.Autonomous;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Config
@Autonomous(name="lineTolinearheading TEST", group = "drive")
public class AutoTEST extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Pose2d startPos = new Pose2d(-35, -72, Math.toRadians(0));

        TrajectorySequence traj = drive.trajectorySequenceBuilder(startPos)
                //move forward 60
                .lineToLinearHeading(new Pose2d(36, 12,  Math.toRadians(-135)))
                .lineToLinearHeading(new Pose2d(62, 12, Math.toRadians(0)))

                .build();

        waitForStart();
        drive.followTrajectorySequence(traj);


        while (opModeIsActive() && !isStopRequested()) {}


    }
}
