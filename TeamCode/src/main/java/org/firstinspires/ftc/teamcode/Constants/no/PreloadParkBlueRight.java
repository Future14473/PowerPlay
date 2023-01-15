package org.firstinspires.ftc.teamcode.Constants.no;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;


@Disabled
@Autonomous
public class PreloadParkBlueRight extends LinearOpMode {

    SampleMecanumDrive drive;
    Pose2d startPos;

    Slides slides;
    VirtualFourBar v4b;
    Claw claw;
    ServoTurret servoTurret;

    public static int marker1 = 30;
    //claw open
    public static int marker2 = 49;

    //retract
    public static int marker3 = 65;
    @Override
    public void runOpMode() throws InterruptedException {
        drive = new SampleMecanumDrive(hardwareMap);
        slides = new Slides(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        claw = new Claw(hardwareMap);
        servoTurret = new ServoTurret(hardwareMap);

        claw.shutUp();

        startPos = new Pose2d(35, 72, Math.toRadians(270));
        waitForStart();
        drive.followTrajectorySequence(createTrajectory());


    }

    public TrajectorySequence createTrajectory() {
        TrajectorySequence traj = drive.trajectorySequenceBuilder(startPos)
                .lineTo(new Vector2d(35, 38))
                .turn(Math.toRadians(90))
                .forward(23)
                .addDisplacementMarker(marker2, () -> {
                    claw.openWide();
                })
                .addDisplacementMarker(marker3,() -> {
                    slides.retract();
                })



                //revert to 0 ready for park
                .back(23)
                .turn(Math.toRadians(-90))


                // prepare first outtake
                .addDisplacementMarker(marker1, () -> {
                    slides.extendLow();

                })


                .build();

        return traj;
    }
}
