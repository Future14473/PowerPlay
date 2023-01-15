package org.firstinspires.ftc.teamcode.Constants.no;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.opmode.TwoWheelTrackingLocalizer;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Disabled
@Autonomous
public class RedLeftCycle extends LinearOpMode {

    Pose2d startPos = new Pose2d(36, 72, Math.toRadians(90));

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setLocalizer(new TwoWheelTrackingLocalizer(hardwareMap, drive));
        drive.setPoseEstimate(startPos);



        TrajectorySequence traj = drive.trajectorySequenceBuilder(startPos)
                //move forward 60
                .lineTo(new Vector2d(36, 12))
                .turn(45)
                .turn(-135)
                .forward(30)


                .build();
        waitForStart();

        drive.followTrajectorySequence(traj);
        while (opModeIsActive() && !isStopRequested()) {}

    }
}
