package org.firstinspires.ftc.teamcode.Opmodes.Autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

public class ParkSequences {

    public static TrajectorySequence createParkLeft(SampleMecanumDrive drive) {


        TrajectorySequence traj = drive.trajectorySequenceBuilder(new Pose2d())
                .strafeRight(60)
                .build();

        return traj;
    }

    public static TrajectorySequence createParkMiddle(SampleMecanumDrive drive) {


        TrajectorySequence traj = drive.trajectorySequenceBuilder(new Pose2d())
                .forward(25)
                .build();

        return traj;
    }

    public static TrajectorySequence createParkRight(SampleMecanumDrive drive) {


        TrajectorySequence traj = drive.trajectorySequenceBuilder(new Pose2d())
                .strafeLeft(60)
                .build();

        return traj;
    }

}
