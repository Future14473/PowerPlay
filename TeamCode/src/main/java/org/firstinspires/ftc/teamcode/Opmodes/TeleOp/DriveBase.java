package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

//

@TeleOp(name = "DriveBASE", group = "Testing")
public class DriveBase extends LinearOpMode {
    SampleMecanumDrive drive;

    public static double YMult = 1.1;
    public static double mvmtMult = 1.0;
    public static double rotMult = 0.9;
    Slides slides;
    VirtualFourBar v4b;
    Timer timer;


    @Override
    public void runOpMode() throws InterruptedException {


        drive = new SampleMecanumDrive(hardwareMap);


        waitForStart();

        while (opModeIsActive()) {
            Pose2d pos = drive.getPoseEstimate();
            Vector2d input = new Vector2d(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x
            ).rotated(-pos.getHeading());
            drive.setWeightedDrivePower(
                    new Pose2d(
                            input.getX(),
                            input.getY(),
                            -gamepad1.right_stick_x
                    )
            );
            drive.update();

        }
    }


}
