package org.firstinspires.ftc.teamcode.trajectorysequence;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

public class TestTrajectory {
    public static TrajectorySequence createSequence(SampleMecanumDrive drive) {
        double maxVel = 10;
        double maxAngularVel = 10;
        double trackWidth = 11.5;
        double outtakeTime = 1;
        double intakeTime = 3;
        double moveDist = 7;



        Pose2d startPos = new Pose2d(-34.5, -72, Math.toRadians(90));

        TrajectorySequence traj = drive.trajectorySequenceBuilder(startPos)
                //first outtake position
                .lineTo(new Vector2d(-34.5, -12))
                .waitSeconds(outtakeTime)
                .turn(Math.toRadians(145))
                //first intake position
                .forward(20).waitSeconds(intakeTime)
                //second outtake position
                .back(5).waitSeconds(outtakeTime)
//                      runs x seconds AFTER previous thing has run
//                      .UNSTABLE_addTemporalMarkerOffset()

                //TODO: prepare for first outtake (extend linear slides and move v4b)
                .addDisplacementMarker(35, () -> {


                })
                /*TODO: perform first outtake (move servoturret and open claw)
                 *  NOTE: waitSeconds ONLY FOR OUTTAKE - reset arm ideally while bot is turning
                 * measure how long it takes to outtake ONLY - apply that to waitSeconds*/.addDisplacementMarker(48, () -> {

                })

                //TODO: prepare for intake (open claw, spin wheels)
                .addDisplacementMarker(60, () -> {

                })
                //TODO extend linear slides and move v4b - outtake after bot stopped
                .addDisplacementMarker(70, () -> {

                }).build();

        return traj;
    }

}
