package org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.ComputerVision.AprilTag;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Disabled
@Autonomous
public class BlueRightTimeBased extends LinearOpMode
{
    OpenCvCamera camera;
    AprilTag aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;

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

    @Override
    public void runOpMode()
    {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
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
                }

            }

            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        /* Actually do something useful */
        if (tagOfInterest.id == LEFT) {
            telemetry.addLine("LEFT");
        } else if (tagOfInterest == null || tagOfInterest.id == MIDDLE) {
            telemetry.addLine("MIDDLE");
        } else if (tagOfInterest.id == RIGHT) {
            telemetry.addLine("RIGHT");
        }
        telemetry.update();
        Pose2d startPos = new Pose2d(35, 72, Math.toRadians(270));
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        waitForStart();

        TrajectorySequence toHigh = drive.trajectorySequenceBuilder(startPos)
                .lineToLinearHeading(new Pose2d(35, 12, Math.toRadians(270)))
                .build();

        drive.followTrajectorySequence(toHigh);

        Slides slides = new Slides(hardwareMap);
        slides.extendHigh();
        ServoTurret servoTurret = new ServoTurret(hardwareMap);
        servoTurret.setOut();
        VirtualFourBar virtualFourBar = new VirtualFourBar(hardwareMap);
        virtualFourBar.outtake();
        Claw claw = new Claw(hardwareMap);
        claw.openWide();

        TrajectorySequence backitUp = drive.trajectorySequenceBuilder(new Pose2d(35, 12, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(35, 36, Math.toRadians(270)))
                .build();

        drive.followTrajectorySequence(backitUp);

        TrajectorySequence left = drive.trajectorySequenceBuilder(new Pose2d(35, 36, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(11, 36, Math.toRadians(270)))
                .build();

        TrajectorySequence middle = drive.trajectorySequenceBuilder(new Pose2d(35, 36, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(35, 36, Math.toRadians(270)))
                .build();

        TrajectorySequence right = drive.trajectorySequenceBuilder(new Pose2d(35, 36, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(59, 36, Math.toRadians(270)))
                .build();

        if (tagOfInterest.id == LEFT) {
            drive.followTrajectorySequence(left);
        } else if (tagOfInterest == null || tagOfInterest.id == MIDDLE) {
            drive.followTrajectorySequence(middle);
        } else if (tagOfInterest.id == RIGHT) {
            drive.followTrajectorySequence(right);
        }




     // GAY MEN IN MY ARSE ARE SO SLAY TTTYYYY I TUNE D ROAD RUNNER COCK

    }


}