package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.home;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.linearR;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.linearX;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.linearY;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.teleOpMoveBackToPole;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.teleOpMoveStraightToIntake;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.teleOpReset;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Intake.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Outtake.Outtake;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.OdoRetraction;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;

@TeleOp
public class Tomfoolery extends LinearOpMode {
    Slides slides;
    Claw claw;
    VirtualFourBar v4b;
    ServoTurret servoTurret;
    Timer timer;

    OdoRetraction retraction;

    Intake intake;
    Outtake outtake;

    Drivetrain drive;

    public enum Mode {
        DRIVER,
        AUTO
    }

    Mode mode = Mode.DRIVER;


    double targetHeading;

    @Override
    public void  runOpMode() throws InterruptedException {

        timer = new Timer(this);

        //drivetrain
        drive = new Drivetrain(hardwareMap);

        //subsystems
        slides = new Slides(hardwareMap);
        claw = new Claw(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        servoTurret = new ServoTurret(hardwareMap);
        retraction = new OdoRetraction(hardwareMap);

        intake = new Intake(slides, claw, v4b, servoTurret);
        outtake = new Outtake(slides, claw, v4b, servoTurret);
        retraction.retract();

        intake.teleopIntakeReady();

        while (!isStarted() && !isStopRequested()) {
            telemetry.addData("Heading", drive.getHeading());
            telemetry.update();
        }

        while (opModeIsActive()) {
            switch (mode) {
                case DRIVER:

                    drive.setPower(gamepad1.left_stick_y*0.7, gamepad1.left_stick_x*0.7, gamepad1.right_stick_x*0.5);
                    //bumpers open and close claw only
                    if (gamepad1.right_bumper) {
                        outtake.outtake();
                    }

                    if (gamepad1.left_bumper) {
                        intake.intake();
                    }

                    if (gamepad1.x) {
                        intake.teleopIntake(timer);
                    }

                    if (gamepad1.dpad_down) {
                        outtake.outtakeReadyJunction();
                    }
                    if (gamepad1.a) {
                        outtake.outtakeReadyLow(timer);
                    }

                    if (gamepad1.b) {
                        outtake.outtakeReadyMid(timer);
                    }

                    if (gamepad1.y) {
                        outtake.outtakeReadyHigh(timer);
                    }

                    if (gamepad1.dpad_left) {
                        servoTurret.setCustom(servoTurret.getCurrPos() - 0.05);
                    }
                    if (gamepad1.dpad_right) {
                        servoTurret.setCustom(servoTurret.getCurrPos() + 0.05);
                    }

                    if (gamepad1.dpad_up) {
                        mode = Mode.AUTO;
                        //check if ready to intake
                        if (slides.getHeight() == home) {
                            outtake.outtake();
                        } else {
                            intake.teleopIntake(timer);

                        }
                        targetHeading = drive.getHeading();
                    }
                case AUTO:
                    while(opModeIsActive() && !gamepad1.dpad_left) {

                        if (gamepad1.dpad_left) {
                            break;
                        }

                        intake.intake();
                        if (gamepad1.dpad_left) {
                            break;
                        }
                        timer.safeDelay(50);
                        if (gamepad1.dpad_left) {
                            break;
                        }
                        slides.extendJunction();
                        if (gamepad1.dpad_left) {
                            break;
                        }


                        //drive back to pole
                        double initPos = drive.getPosition().get(0);
                        drive.setPower(-linearY, -linearX, -linearR);
                        while(opModeIsActive() && !gamepad1.dpad_left) {
                            double currPos = drive.getPosition().get(0);
                            if (Math.abs(currPos - initPos) >= teleOpMoveBackToPole) {
                                drive.setPower(0,0,0);
                                double initPos2 = drive.getHeading();

                                if (initPos2 > targetHeading) {
                                    drive.setPower(0,0,-0.5);
                                    while (opModeIsActive()) {
                                        if (drive.getHeading() < targetHeading) {
                                            drive.setPower(0,0,0);
                                            break;
                                        }
                                    }
                                } else {
                                    drive.setPower(0,0,0.5);
                                    while (opModeIsActive()) {
                                        if (drive.getHeading() > targetHeading) {
                                            drive.setPower(0,0,0);
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }

                        outtake.outtakeAuto(timer);

                        //move forward to pick next cone
                        double initPos3 = drive.getPosition().get(0);
                        drive.setPower(linearY, linearX, linearR);
                        while (opModeIsActive() && !gamepad1.dpad_left) {
                            double currPos = drive.getPosition().get(0);
                            if (Math.abs(currPos - initPos3) > teleOpReset) {
                                intake.teleopIntake(timer);
                            }

                            if (Math.abs(currPos - initPos3) > teleOpMoveStraightToIntake) {
                                drive.setPower(0,0,0);
                                double initPos2 = drive.getHeading();

                                if (initPos2 > targetHeading) {
                                    drive.setPower(0,0,-0.5);
                                    while (opModeIsActive()) {
                                        if (drive.getHeading() < targetHeading) {
                                            drive.setPower(0,0,0);
                                            break;
                                        }
                                    }
                                } else {
                                    drive.setPower(0,0,0.5);
                                    while (opModeIsActive()) {
                                        if (drive.getHeading() > targetHeading) {
                                            drive.setPower(0,0,0);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    mode = Mode.DRIVER;
            }

            telemetry.addData("Drive mode", mode);
            telemetry.update();

        }
    }
}
