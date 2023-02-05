package org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no;

import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.blueAllianceBlueStation;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.preloadReady;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.redAllianceRedStation;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.redAllianceRedStationPole;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.redAllianceRedStationStack;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.SLIDES_HOME;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.ComputerVision.AprilTag;
import org.firstinspires.ftc.teamcode.Hardware.Intake.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Outtake.Outtake;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Autonomous
public class RedRR extends LinearOpMode {

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

    int ONE = 1;
    int TWO = 2;
    int THREE = 3;

    AprilTagDetection tagOfInterest = null;

    SampleMecanumDrive drive;

    Slides slides;
    Claw claw;
    VirtualFourBar v4b;
    ServoTurret servoTurret;

    Intake intake;
    Outtake outtake;

    Timer timer;

    @Override
    public void runOpMode() {

        timer = new Timer(this);

        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(redAllianceRedStation);

        slides = new Slides(hardwareMap);
        claw = new Claw(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        servoTurret = new ServoTurret(hardwareMap);


        intake.teleopIntakeReady();
        intake.intake();

        telemetry.addLine("Subsystems Initialized\nPlease work you monkey");
        telemetry.update();

        slides.resetEncoders();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Main"), cameraMonitorViewId);
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
                    if(tag.id == ONE || tag.id == TWO || tag.id == THREE)
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
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }



        slides.setCustom(250);
        timer.safeDelay(50);



        TrajectorySequence moveToPolePreload = drive.trajectorySequenceBuilder(redAllianceRedStation)
                .lineTo(new Vector2d(-36,-13))
                .turn(Math.toRadians(135))
                .lineTo(new Vector2d(-35,-3.5))
                .addDisplacementMarker(preloadReady, () -> {
                    outtake.outtakeReadyHigh(timer);
                })
                .build();



        drive.followTrajectorySequence(moveToPolePreload);

        //OUTTAKE SEQUENCE
        outtake.outtakeTeleOp(timer);
        intake.intake();
        slides.retract();
        timer.safeDelay(SLIDES_HOME);
        outtake.outtake();
        slides.extendStack(1);


        TrajectorySequence moveToStack = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(new Pose2d(-61.5, -14, Math.toRadians(180)))
                .build();

        drive.followTrajectorySequence(moveToStack);

        timer.safeDelay(10);
        intake.intake();
        timer.safeDelay(50);
        slides.setCustom(slides.getHeight()+ 400);


        TrajectorySequence moveToPoleFromStack = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(new Pose2d(-35,-5.5, Math.toRadians(225)))
                .addDisplacementMarker(3, () -> {
                    outtake.outtakeReadyHigh(timer);
                })
                .build();

        drive.followTrajectorySequence(moveToPoleFromStack);

        //OUTTAKE SEQUENCE
        outtake.outtakeTeleOp(timer);
        intake.intake();
        slides.retract();
        timer.safeDelay(SLIDES_HOME);
        outtake.outtake();
        slides.extendStack(2);


        drive.followTrajectorySequence(moveToStack);

        timer.safeDelay(100);
        intake.intake();
        timer.safeDelay(100);
        slides.setCustom(slides.getHeight()+400);

        drive.followTrajectorySequence(moveToPoleFromStack);


        //OUTTAKE SEQUENCE
        outtake.outtakeTeleOp(timer);
        intake.intake();
        slides.retract();
        timer.safeDelay(SLIDES_HOME);



        if (tagOfInterest.id == ONE) {
            TrajectorySequence parkOne = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                    .forward(12)
                    .build();
            drive.followTrajectorySequence(parkOne);
            intake.teleopIntake(timer);

        } else if (tagOfInterest == null || tagOfInterest.id == TWO) {
            intake.teleopIntake(timer);

        } else if (tagOfInterest.id == THREE) {
            TrajectorySequence parkThree = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                    .back(12)
                    .build();
            drive.followTrajectorySequence(parkThree);

        }


    }

    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }
}
