package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Base {
    //extend slides
    public static int marker1 = 37;
    //claw open
    public static int marker2 = 49;
    //retract
    public static int marker3 = 67;

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);


        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(36, 72, Math.toRadians(-90)))
                                //move forward 60
                                .lineToLinearHeading(new Pose2d(36, 12,  Math.toRadians(-135)))
                                .lineToLinearHeading(new Pose2d(62, 12, Math.toRadians(0)))
                                .back(24)
                                .waitSeconds(1)
                                .forward(24)
                                .waitSeconds(1)
                                .back(24)
                                .waitSeconds(1)
                                .forward(24)
                                .waitSeconds(1)
                                .back(24)
                                .waitSeconds(1)
                                .forward(24)
                                .waitSeconds(1)
                                .back(24)
                                .waitSeconds(1)
                                .forward(24)
                                .waitSeconds(1)
                                .back(24)
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}