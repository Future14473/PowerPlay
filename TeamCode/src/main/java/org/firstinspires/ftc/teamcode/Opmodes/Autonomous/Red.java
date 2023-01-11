package org.firstinspires.ftc.teamcode.Opmodes.Autonomous;


import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.linearR;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.linearX;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.linearY;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.moveBackFromStack;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.moveBackIntoPole;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.moveBackIntoPoleCycle;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.moveStraightFromPoleCycle;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.moveStraightToStack;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.moveStraightToStackCycle;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.preloadAngle;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.preloadDistance;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.strafeR;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.strafeToStackFromPole;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.strafeX;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.strafeY;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.turnToPole;
import android.annotation.SuppressLint;

import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.ComputerVision.AprilTag;
import org.firstinspires.ftc.teamcode.Hardware.Intake.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Outtake.Outtake;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Autonomous
public class Red extends LinearOpMode
{
    OpenCvCamera camera;
    AprilTag aprilTagDetectionPipeline;

    Slides slides;
    Claw claw;
    VirtualFourBar v4b;
    ServoTurret servoTurret;

    Intake intake;
    Outtake outtake;



    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    int LEFT = 1;
    int MIDDLE = 2;
    int RIGHT = 3;

    AprilTagDetection tagOfInterest = null;


    Drivetrain drive;
    @Override
    public void runOpMode()
    {
        PhotonCore.enable();
        drive = new Drivetrain(hardwareMap);
        Timer timer = new Timer(this);
        slides = new Slides(hardwareMap);
        claw = new Claw(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        servoTurret = new ServoTurret(hardwareMap);

        intake = new Intake(slides, claw, v4b, servoTurret);
        outtake = new Outtake(slides, claw, v4b, servoTurret);

        intake.teleopIntakeReady();


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "back"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTag(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        telemetry.setMsTransmissionInterval(50);

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested())
        {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)
                {
                    if(tag.id == LEFT || tag.id == MIDDLE || tag.id == RIGHT)
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                    }
                }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }
            telemetry.addData("Heading", drive.getHeading());
            telemetry.update();
            sleep(20);
        }
        double targetAngle = drive.getHeading()+90;

        telemetry.addData("Target heading", targetAngle);

        intake.intake();
        timer.safeDelay(100);
        slides.extendJunction();

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("no tag snapshot found. L");
            telemetry.update();
        }



        double initPos = drive.getPosition().get(0);

        //drive to preload
        drive.setPower(linearY,linearX,linearR);
        while (opModeIsActive()) {
            double currPos = drive.getPosition().get(0);
            if (Math.abs(currPos - initPos) >= preloadDistance) {
                drive.setPower(0,0,0);
                break;
            }
        }

        //turn to drop preload
        drive.setPower(0,0,0.7);
        double initAngle = drive.getHeading();
        while (opModeIsActive()) {
            double currAngle = drive.getHeading();

            telemetry.addData("heading", currAngle);
            telemetry.update();

            if(drive.getRelativeAngle(initAngle, currAngle) >= preloadAngle) {
                drive.setPower(0,0,0);
                timer.safeDelay(50);
                drive.setPower(-0.5,0,0);
                double initPOS = drive.getPosition().get(0);
                while (opModeIsActive()) {
                    double currPos = drive.getPosition().get(0);
                    if (Math.abs(currPos - initPOS) > moveBackIntoPole) {
                        drive.setPower(0,0,0);
                        double initPos6 = drive.getHeading();

                        if (initPos6 > targetAngle) {
                            drive.setPower(0,0,-0.5);
                            while (opModeIsActive()) {
                                if (drive.getHeading() < targetAngle) {
                                    drive.setPower(0,0,0);
                                    break;
                                }
                            }
                        } else {
                            drive.setPower(0,0,0.5);
                            while (opModeIsActive()) {
                                if (drive.getHeading() > targetAngle) {
                                    drive.setPower(0,0,0);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }

        outtake.outtakeAuto(timer);

        // START CYCLE 1
        // strafe to stack


        drive.setPower(strafeY, strafeX, strafeR);
        double initPos43 = drive.getPosition().get(1);
        while (opModeIsActive()) {
            double currPos = drive.getPosition().get(1);
            if(Math.abs(currPos - initPos43) >= strafeToStackFromPole) {
                drive.setPower(0,0,0);
                double initPos6 = drive.getHeading();

                if (initPos6 > targetAngle) {
                    drive.setPower(0,0,-0.5);
                    while (opModeIsActive()) {
                        if (drive.getHeading() < targetAngle) {
                            drive.setPower(0,0,0);
                            break;
                        }
                    }
                } else {
                    drive.setPower(0,0,0.5);
                    while (opModeIsActive()) {
                        if (drive.getHeading() > targetAngle) {
                            drive.setPower(0,0,0);
                            break;
                        }
                    }
                }
                break;
            }
        }

        intake.autoIntakeReady(1,timer);

        // drive forward to stack to pick up cone
        double initPos13 = drive.getPosition().get(0);
        drive.setPower(linearY-0.1,linearX,linearR);
        while(opModeIsActive()) {
            telemetry.addData("heading", drive.getHeading());
            telemetry.update();
            double currPos = drive.getPosition().get(0);
            if(Math.abs(currPos - initPos13) >= moveStraightToStack) {
                drive.setPower(0,0,0);
                double initPos6 = drive.getHeading();

                if (initPos6 > targetAngle) {
                    drive.setPower(0,0,-0.5);
                    while (opModeIsActive()) {
                        if (drive.getHeading() < targetAngle) {
                            drive.setPower(0,0,0);
                            break;
                        }
                    }
                } else {
                    drive.setPower(0,0,0.5);
                    while (opModeIsActive()) {
                        if (drive.getHeading() > targetAngle) {
                            drive.setPower(0,0,0);
                            break;
                        }
                    }
                }
                break;
            }
        }

        intake.intake();
        timer.safeDelay(500);
        slides.setCustom(800);
        timer.safeDelay(200);

        // drive back from stack
        double initPos4 = drive.getPosition().get(0);
        drive.setPower(-linearY,-linearX,-linearR);
        while(opModeIsActive()) {
            double currPos = drive.getPosition().get(0);
            if(Math.abs(currPos - initPos4) >= moveBackFromStack) {
                drive.setPower(0,0,0);
                double initPos6 = drive.getHeading();

                if (initPos6 > targetAngle) {
                    drive.setPower(0,0,-0.5);
                    while (opModeIsActive()) {
                        if (drive.getHeading() < targetAngle) {
                            drive.setPower(0,0,0);
                            break;
                        }
                    }
                } else {
                    drive.setPower(0,0,0.5);
                    while (opModeIsActive()) {
                        if (drive.getHeading() > targetAngle) {
                            drive.setPower(0,0,0);
                            break;
                        }
                    }
                }
                break;
            }
        }

        //turn to outtake
        drive.setPower(0,0,0.7);

        while (opModeIsActive()) {
            double currAngle = drive.getHeading();

            telemetry.addData("heading", currAngle);
            telemetry.update();

            if(currAngle >= turnToPole -  1 && currAngle <= turnToPole + 1) {
                drive.setPower(-0.5,0,0);
                double initPOS = drive.getPosition().get(0);
                while (opModeIsActive()) {
                    double currPos = drive.getPosition().get(0);
                    drive.setPower(-0.5,0,0);
                    if (Math.abs(currPos - initPOS) > moveBackIntoPoleCycle) {
                        drive.setPower(0,0,0);
                        break;
                    }
                }
                break;
            }
        }

        outtake.outtakeAuto(timer);

        //drive forward

        double initPos21 = drive.getPosition().get(0);
        drive.setPower(linearY,linearX,linearR);

        while(opModeIsActive()) {
            double currPos = drive.getPosition().get(0);
            if(Math.abs(currPos - initPos21) >= moveStraightFromPoleCycle) {
                drive.setPower(0,0,0);
                double initPos6 = drive.getHeading();

                if (initPos6 > targetAngle) {
                    drive.setPower(0,0,-0.5);
                    while (opModeIsActive()) {
                        if (drive.getHeading() < targetAngle) {
                            drive.setPower(0,0,0);
                            break;
                        }
                    }
                } else {
                    drive.setPower(0,0,0.5);
                    while (opModeIsActive()) {
                        if (drive.getHeading() > targetAngle) {
                            drive.setPower(0,0,0);
                            break;
                        }
                    }
                }
                break;
            }
        }
        intake.autoIntakeReady(2, timer);


        // drive forward to stack to pick up cone
        double initPos69 = drive.getPosition().get(0);
        drive.setPower(linearY-0.1,linearX,linearR);
        while(opModeIsActive()) {
            telemetry.addData("heading", drive.getHeading());
            telemetry.update();
            double currPos = drive.getPosition().get(0);
            if(Math.abs(currPos - initPos69) >= moveStraightToStackCycle) {
                drive.setPower(0,0,0);
                double initPos6 = drive.getHeading();

                if (initPos6 > targetAngle) {
                    drive.setPower(0,0,-0.5);
                    while (opModeIsActive()) {
                        if (drive.getHeading() < targetAngle) {
                            drive.setPower(0,0,0);
                            break;
                        }
                    }
                } else {
                    drive.setPower(0,0,0.5);
                    while (opModeIsActive()) {
                        if (drive.getHeading() > targetAngle) {
                            drive.setPower(0,0,0);
                            break;
                        }
                    }
                }
                break;
            }
        }

        intake.intake();
        timer.safeDelay(500);
        slides.setCustom(800);
        timer.safeDelay(200);

        // drive back from stack
        double initPos420 = drive.getPosition().get(0);
        drive.setPower(-linearY,-linearX,-linearR);
        while(opModeIsActive()) {
            double currPos = drive.getPosition().get(0);
            if(Math.abs(currPos - initPos420) >= moveBackFromStack) {
                drive.setPower(0,0,0);
                double initPos6 = drive.getHeading();

                if (initPos6 > targetAngle) {
                    drive.setPower(0,0,-0.5);
                    while (opModeIsActive()) {
                        if (drive.getHeading() < targetAngle) {
                            drive.setPower(0,0,0);
                            break;
                        }
                    }
                } else {
                    drive.setPower(0,0,0.5);
                    while (opModeIsActive()) {
                        if (drive.getHeading() > targetAngle) {
                            drive.setPower(0,0,0);
                            break;
                        }
                    }
                }
                break;
            }
        }

        //turn to outtake
        drive.setPower(0,0,0.7);

        while (opModeIsActive()) {
            double currAngle = drive.getHeading();

            telemetry.addData("heading", currAngle);
            telemetry.update();

            if(currAngle >= turnToPole -  1 && currAngle <= turnToPole + 1) {
                drive.setPower(-0.5,0,0);
                double initPOS = drive.getPosition().get(0);
                while (opModeIsActive()) {
                    double currPos = drive.getPosition().get(0);
                    drive.setPower(-0.5,0,0);
                    if (Math.abs(currPos - initPOS) > moveBackIntoPoleCycle) {
                        drive.setPower(0,0,0);
                        break;
                    }
                }
                break;
            }
        }

        outtake.outtakeAuto(timer);



        //drive forward to end (TEMPORARY)
        drive.setPower(0.7,0,0);
        timer.safeDelay(500);
        drive.setPower(0,0,0);
        intake.teleopIntake(timer);







//        if (tagOfInterest.id == LEFT) {
//
//
//        } else if (tagOfInterest == null || tagOfInterest.id == MIDDLE) {
//
//
//        } else if (tagOfInterest.id == RIGHT) {
//
//        }

    }

    @SuppressLint("DefaultLocale")
    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addData("Detected tag name=", tagOfInterest.id);

    }
}