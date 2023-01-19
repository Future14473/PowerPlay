package org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Disabled
@Autonomous
public class PreloadParkRedRight extends LinearOpMode {

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

        startPos = new Pose2d(35, -72, Math.toRadians(90));
        waitForStart();
        drive.followTrajectorySequence(createTrajectory());


    }

    public TrajectorySequence createTrajectory() {
        TrajectorySequence traj = drive.trajectorySequenceBuilder(startPos)
                .lineTo(new Vector2d(35, -12))
                .turn(Math.toRadians(-95))
                .addDisplacementMarker(() -> {
                    claw.openWide();
                })
                .addDisplacementMarker(() -> {
                    slides.retract();
                    claw.shutUp();
//                    v4b.setHome();
                })
                .waitSeconds(3)

                //revert to 0 ready for park
                .turn(Math.toRadians(95))
                .back(30)

                // prepare first outtake
                .addDisplacementMarker(35, () -> {
                    slides.extendHigh();
                    v4b.setCustom(0.45);
                })

                .build();

        return traj;
    }
}
