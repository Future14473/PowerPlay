package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class Base {


    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        Pose2d startPos = new Pose2d(36, 72, Math.toRadians(-90));
        Pose2d preloadOut = new Pose2d(36, 12, Math.toRadians(-135));
        Pose2d stackIn = new Pose2d(60, 12, Math.toRadians(0));

        int resetDist = 7;


        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 11)
                .followTrajectorySequence(drive ->
                                drive.trajectorySequenceBuilder(startPos)
                                        .lineToLinearHeading(preloadOut)
                                        .waitSeconds(3)
                                        .lineToLinearHeading(stackIn)


//                                .back(24)
//                                .waitSeconds(1)
//                                .forward(24)
//                                .waitSeconds(1)
//                                .back(24)
//                                .waitSeconds(1)
//                                .forward(24)
//                                .waitSeconds(1)
//                                .back(24)
//                                .waitSeconds(1)
//                                .forward(24)
//                                .waitSeconds(1)
//                                .back(24)
//                                .waitSeconds(1)
//                                .forward(24)
//                                .waitSeconds(1)
//                                .back(24)


//                               raise slides, prepare v4b outtake (FRONT)
                                .addDisplacementMarker((startPos.getY() - preloadOut.getY()) / 2, () -> {
                                })
//                               open claw lock + lock on to pole (USE SERVO TURRET)
                                .addDisplacementMarker(startPos.getY() - preloadOut.getY() - 0.0000001, () -> {
                                })
//                                reset to intake first cone
                                .addDisplacementMarker(startPos.getY() - preloadOut.getY() + resetDist, () -> {})
//                              intake first cone
                                .addDisplacementMarker((startPos.getY() - preloadOut.getY()) + (stackIn.getX() - preloadOut.getX()) - 0.000001, () -> {})
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}