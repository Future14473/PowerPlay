package org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Disabled
// red side closest to the garage door
@Autonomous(name = "BlueLeftRedRight",group = "!!!!!!!!!!!!!" )
public class PreloadParkBlueLeftRedRight extends LinearOpMode {

    SampleMecanumDrive drive;
    Pose2d startPos;

    Slides slides;
    VirtualFourBar v4b;
    Claw claw;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new SampleMecanumDrive(hardwareMap);
        slides = new Slides(hardwareMap);
        slides.resetEncoders();
        claw = new Claw(hardwareMap);

        claw.shutUp();

        startPos = new Pose2d(-35, -72, Math.toRadians(90));
        slides.resetEncoders();
        waitForStart();
        drive.followTrajectorySequence(createTrajectory());

        claw.shutUp();
        slides.retract();


    }

    public TrajectorySequence createTrajectory() {
        TrajectorySequence traj = drive.trajectorySequenceBuilder(startPos)
                .addDisplacementMarker(() -> {
                    claw.shutUp();
                })
                .forward(36)
//                .turn(Math.toRadians(105))
//                .forward(8)
//                .addDisplacementMarker(() -> {
//                    claw.openWide();
//                })
//                .back(8)
//
//                .addDisplacementMarker(() -> {
//                    slides.retract();
//                    claw.shutUp();
//                })
//                .waitSeconds(1)
//
//                //revert to 0 ready for park
//                .turn(Math.toRadians(-105))
//                .addDisplacementMarker(() -> {
//                    slides.incrementDown();
//                    slides.EMERGENCY();
//                })
//
//
//                // prepare first outtake
//                .addDisplacementMarker(35, () -> {
//                    slides.extendHigh();
//                })



                .build();

        return traj;
    }
}
