package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class Base {


    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        Pose2d startPos = new Pose2d(-36, -62, Math.toRadians(90));
        Pose2d preloadOut = new Pose2d(36, -20, Math.toRadians(-45));
        Pose2d stackIn = new Pose2d(60, -20, Math.toRadians(0));

        int resetDist = 7;


        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(360), Math.toRadians(360), 11.5)
                .followTrajectorySequence(drive ->
                                drive.trajectorySequenceBuilder(startPos)
                                        .lineTo(new Vector2d(-36,-13))
                                        .turn(Math.toRadians(135))
                                        .lineTo(new Vector2d(-29.5,-5.5))

                                        .lineToLinearHeading(new Pose2d(-61.5, -10, Math.toRadians(180)))

                                        .lineToLinearHeading(new Pose2d(-29.5,-5.5, Math.toRadians(225)))

                                        .lineToLinearHeading(new Pose2d(-61.5, -10, Math.toRadians(180)))

                                        .lineToLinearHeading(new Pose2d(-29.5,-5.5, Math.toRadians(225)))

                                        .lineToLinearHeading(new Pose2d(-61.5, -10, Math.toRadians(180)))

                                        .lineToLinearHeading(new Pose2d(-29.5,-5.5, Math.toRadians(225)))

                                        .forward(12)
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