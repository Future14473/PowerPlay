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
@Autonomous(name = "RedLeftBlueRight",group = "!!!!!!!!!!!!!" )
public class PreloadParkRedLeftBlueRight extends LinearOpMode {

    SampleMecanumDrive drive;
    Pose2d startPos;

    Slides slides;
    VirtualFourBar v4b;
    Claw claw;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new SampleMecanumDrive(hardwareMap);
        slides = new Slides(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        claw = new Claw(hardwareMap);

        claw.shutUp();

        startPos = new Pose2d(-35, -72, Math.toRadians(90));
        slides.resetEncoders();
        waitForStart();
        slides.resetEncoders();
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
//                .turn(Math.toRadians(-100))
//                .forward(8)
//                .addDisplacementMarker(() -> {
//                    claw.openWide();
//                })
//                .back(6)
//
//                .addDisplacementMarker(() -> {
//                    slides.retract();
//                    claw.shutUp();
//                    v4b.setHome();
//                })
//                .waitSeconds(3)
//
//                //revert to 0 ready for park
//                .turn(Math.toRadians(100))
//                .addDisplacementMarker(() -> {
//                    slides.incrementDown();
//                    slides.EMERGENCY();
//                    slides.retract();
//                })
//
//
//                // prepare first outtake
//                .addDisplacementMarker(35, () -> {
//                    slides.extendHigh();
//                    v4b.setCustom(0.45);
//                })

                .build();

        return traj;
    }
}
