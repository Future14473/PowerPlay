package org.firstinspires.ftc.teamcode.Constants.no;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.ComputerVision.SleeveDetection;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

//blue side closest to the garage door

@Disabled
@Autonomous
public class PreloadParkBlueLeft extends LinearOpMode {

    SampleMecanumDrive drive;
    Pose2d startPos;

    Slides slides;
    VirtualFourBar v4b;
    Claw claw;

    private enum Locations {
        ONE, TWO, THREE, NONE
    }

    public OpenCvCamera camera;
    public SleeveDetection cv;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new SampleMecanumDrive(hardwareMap);
        slides = new Slides(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        claw = new Claw(hardwareMap);

        slides.resetEncoders();

        claw.shutUp();
//        v4b.setHome();

        startPos = new Pose2d(-35, 72, Math.toRadians(-90));

//        camera = cameraInit();
//        startStream();

        //ensure cv is started and working
//        Locations color1 = getSleeveColor(cv);
//        telemetry.addData("Sleeve Color", color1);
//        telemetry.update();

        waitForStart();

//        while (opModeIsActive()) {
//            Locations color2 = getSleeveColor(cv);
//            telemetry.addData("Sleeve Color", color2);
//            telemetry.update();
//
//            switch(color2) {
//                case ONE:
//                    drive.followTrajectorySequence(createTrajectory());
//                    drive.followTrajectorySequence(createParkOne(drive));
//                case TWO:
//                    drive.followTrajectorySequence(createParkTwo(drive));
//                case THREE:
//                    drive.followTrajectorySequence(createParkThree(drive));
//            }
//
//        }
        drive.followTrajectorySequence(createTrajectory());


    }

    public TrajectorySequence createTrajectory() {
        TrajectorySequence traj = drive.trajectorySequenceBuilder(startPos)
                .forward(52)
                .turn(Math.toRadians(90))
                .forward(28)
                .addDisplacementMarker(() -> {
                    claw.openWide();
                })
                .back(14)
                .addDisplacementMarker(89, () -> {
                    slides.retract();
                    claw.shutUp();
//                    v4b.setHome();
                })


                //revert to 0 ready for park
                .turn(Math.toRadians(-90))

                // prepare first outtake
                .addDisplacementMarker(35, () -> {
                    slides.extendHigh();
                    v4b.setCustom(0.45);
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
