package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Disabled
@TeleOp(name = "AutoSpline", group = "Testing")
public class DriveSpline extends LinearOpMode {
    SampleMecanumDrive drive;

    enum driveMode {
        DRIVER,
        AUTO
    }

    driveMode currentMode = driveMode.DRIVER;

    @Override
    public void runOpMode() throws InterruptedException {


        drive = new SampleMecanumDrive(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {


            switch (currentMode) {
                case DRIVER:
                    telemetry.addLine("Manual Control");
                    drive.setWeightedDrivePower(
                            new Pose2d(
                                    -gamepad1.left_stick_y,
                                    -gamepad1.left_stick_x,
                                    -gamepad1.right_stick_x
                            )
                    );

                    if (gamepad1.a) {
                        currentMode = driveMode.AUTO;
                    }
                    break;

                case AUTO:
                    telemetry.addLine("Auto Control");
                    TrajectorySequence traj = drive.trajectorySequenceBuilder(new Pose2d())
                            .forward(25)
                            .build();
                    drive.followTrajectorySequence(traj);

                    if (gamepad1.x) {
                        currentMode = driveMode.DRIVER;
                    }
                    break;
            }

            telemetry.update();
        }
    }
}
