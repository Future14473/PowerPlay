package org.firstinspires.ftc.teamcode.Constants.no;


import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Drivetrain;

@TeleOp
public class turnTest extends LinearOpMode {
    Drivetrain drive;



    @Override
    public void runOpMode() throws InterruptedException {
        drive = new Drivetrain(hardwareMap);
        PhotonCore.enable();

        waitForStart();

        if (gamepad1.x) {
            drive.setPower(0,0,0.5);
            double initPos = drive.getHeading();
            while (opModeIsActive()) {
                if (drive.getRelativeAngle(initPos, drive.getHeading()) >= 90) {
                    drive.setPower(0,0,0);
                }
            }
        }

        if (gamepad1.a) {
            drive.setPower(0,0,0.7);
            double initPos = drive.getHeading();
            while (opModeIsActive()) {
                if (drive.getRelativeAngle(initPos, drive.getHeading()) >= 90) {
                    drive.setPower(0,0,0);
                }
            }
        }

        if (gamepad1.b) {
            drive.setPower(0,0,-0.5);
            double initPos = drive.getHeading();
            while (opModeIsActive()) {
                if (drive.getRelativeAngle(initPos, drive.getHeading()) >= 90) {
                    drive.setPower(0,0,0);
                }
            }
        }
        if (gamepad1.y) {
            drive.setPower(0,0,-0.7);
            double initPos = drive.getHeading();
            while (opModeIsActive()) {
                if (drive.getRelativeAngle(initPos, drive.getHeading()) >= 90) {
                    drive.setPower(0,0,0);
                }
            }
        }
    }

}
