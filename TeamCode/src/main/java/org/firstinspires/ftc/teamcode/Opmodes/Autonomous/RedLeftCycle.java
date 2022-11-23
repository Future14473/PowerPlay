package org.firstinspires.ftc.teamcode.Opmodes.Autonomous;


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
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Disabled
@Autonomous
public class RedLeftCycle extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        double maxVel = 10;
        double maxAngularVel = 10;
        double trackWidth = 11.5;
        double outtakeTime = 1;
        double intakeTime = 3;
        double moveDist = 7;

        Timer timer = new Timer(this);
        Slides slides = new Slides(hardwareMap);
        VirtualFourBar v4b = new VirtualFourBar(hardwareMap);
        ServoTurret servoTurret = new ServoTurret(hardwareMap);


        Pose2d startPos = new Pose2d(-34.5, -72, Math.toRadians(90));
        v4b.setHome();



        TrajectorySequence traj = drive.trajectorySequenceBuilder(startPos)
                //first outtake position
                .lineTo(new Vector2d(-34.5, -8))
                .turn(Math.toRadians(180))

                .forward(3)
                .waitSeconds(outtakeTime)
                .forward(10)
                .addDisplacementMarker(70, () -> {
                    slides.extendHigh();
                    timer.safeDelay(100);
                    v4b.outHigh();
                })

                .build();
        waitForStart();

        drive.followTrajectorySequence(traj);
        while (opModeIsActive() && !isStopRequested()) {}


    }
}
