package org.firstinspires.ftc.teamcode.trajectorysequence;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

public class AutoPark {
    public static Trajectory createParkOne(SampleMecanumDrive drive) {


        Trajectory traj = drive.trajectoryBuilder(new Pose2d())
                .strafeRight(60)
                .build();
        return traj;
    }

    public static TrajectorySequence createParkTwo(SampleMecanumDrive drive) {


        TrajectorySequence traj = drive.trajectorySequenceBuilder(new Pose2d())
            .forward(25)
            .build();
        return traj;
    }

    public static TrajectorySequence createParkThree(SampleMecanumDrive drive) {


        TrajectorySequence traj = drive.trajectorySequenceBuilder(new Pose2d())
                .strafeLeft(60)
                .build();

        return traj;
    }

}
