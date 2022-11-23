package org.firstinspires.ftc.teamcode.Opmodes.Autonomous;

import static org.firstinspires.ftc.teamcode.trajectorysequence.AutoPark.createParkOne;
import static org.firstinspires.ftc.teamcode.trajectorysequence.AutoPark.createParkThree;
import static org.firstinspires.ftc.teamcode.trajectorysequence.AutoPark.createParkTwo;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.ComputerVision.SleeveDetection;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous
public class CVParkTEST extends LinearOpMode {


    public OpenCvCamera camera;
    public SleeveDetection cv;

    SampleMecanumDrive drive;

    private enum Locations {
        ONE, TWO, THREE, NONE
    }

    Pose2d startPos;
    public static int marker1 = 30;
    //claw open
    public static int marker2 = 49;

    //retract
    public static int marker3 = 67;

    Slides slides;
    Claw claw;


    @Override
    public void runOpMode() throws InterruptedException {
        slides = new Slides(hardwareMap);
        claw = new Claw(hardwareMap);

        claw.shutUp();
        // debug

        //drivetrain
        drive = new SampleMecanumDrive(hardwareMap);

        camera = cameraInit();
        startStream();


        //ensure cv is started and working
        Locations color1 = getSleeveColor(cv);
        telemetry.addData("Sleeve Color", color1);
        telemetry.update();
        startPos = new Pose2d(35, 72, Math.toRadians(270));

        waitForStart();


        Locations color2 = getSleeveColor(cv);
        telemetry.addData("Sleeve Color", color2);
        telemetry.update();

        switch(color2) {
            case ONE:
                drive.followTrajectorySequence(createTrajectory());
                drive.followTrajectory(createParkOne());
                break;
            case TWO:
                drive.followTrajectorySequence(createTrajectory());
            case THREE:
                drive.followTrajectorySequence(createTrajectory());
                drive.followTrajectorySequence(createParkThree(drive));
        }

    }

    public Trajectory createParkOne() {


        Trajectory traj = drive.trajectoryBuilder(new Pose2d())
                .strafeRight(20)
                .build();
        return traj;
    }
    public TrajectorySequence createTrajectory() {
        TrajectorySequence traj = drive.trajectorySequenceBuilder(startPos)
                .lineTo(new Vector2d(35, 36))
                .turn(Math.toRadians(90))
                .forward(20)
                .addDisplacementMarker(marker2, () -> {
                    claw.openWide();
                })
                .addDisplacementMarker(marker3,() -> {
                    slides.retract();
                })

                //revert to 0 ready for park
                .back(14)
                .turn(Math.toRadians(-80))


                // prepare first outtake
                .addDisplacementMarker(marker1, () -> {
                    slides.extendLow();

                })


                .build();

        return traj;
    }
    public Locations getSleeveColor(SleeveDetection cv) {

        if (cv.getSleeveColor() == null) {
            telemetry.addData("Sleeve Color", "Null");
        } else {
            switch (cv.getSleeveColor()) {
                case PINK:
                    return Locations.ONE;

                case GREEN:
                    return Locations.TWO;

                case ORANGE:
                    return Locations.THREE;

                case NONE:
                    return Locations.NONE;
            }
        }
        return Locations.NONE;
    }


    public OpenCvCamera cameraInit() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        return OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
    }

    public void startStream() {
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                // GPU-accelerated render!
                camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                requestOpModeStop();
            }
        });
        cv = new SleeveDetection(telemetry);
        camera.setPipeline(cv);
    }
}
