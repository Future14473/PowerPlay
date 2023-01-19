package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class Base {


    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        Pose2d startPos = new Pose2d(-36, 62, Math.toRadians(-90));
        Pose2d preloadOut = new Pose2d(36, 12, Math.toRadians(-135));
        Pose2d stackIn = new Pose2d(60, 12, Math.toRadians(0));

        int resetDist = 7;


        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(360), Math.toRadians(360), 11.5)
                .followTrajectorySequence(drive ->
                                drive.trajectorySequenceBuilder(startPos)
                                        .lineToLinearHeading(new Pose2d(-30, 8, Math.toRadians(135)))
                                        .lineToLinearHeading(new Pose2d(-60, 13, Math.toRadians(180)))
                                        //^^this times 6

                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}