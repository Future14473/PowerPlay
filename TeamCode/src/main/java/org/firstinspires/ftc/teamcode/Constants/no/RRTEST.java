package org.firstinspires.ftc.teamcode.Constants.no;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.opmode.TwoWheelTrackingLocalizer;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
public class RRTEST extends LinearOpMode {

    Pose2d startPos = new Pose2d(36, 72, Math.toRadians(-90));

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setLocalizer(new TwoWheelTrackingLocalizer(hardwareMap, drive));
        drive.setPoseEstimate(startPos);


        TrajectorySequence traj = drive.trajectorySequenceBuilder(startPos)
                //move forward 60
                .lineTo(new Vector2d(33, 27))
                .turn(Math.toRadians(-145))
                .lineTo(new Vector2d(33,27))


                .build();
        waitForStart();

        drive.followTrajectorySequence(traj);
        while (opModeIsActive() && !isStopRequested()) {}

    }
}
