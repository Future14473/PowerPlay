package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import org.firstinspires.ftc.teamcode.ComputerVision.HSV;
import org.firstinspires.ftc.teamcode.ComputerVision.YRCB;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Config
@TeleOp(name = "CVTest", group = "Testing")
public class CVTest extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();


    public OpenCvCamera camera;
    public HSV cv;


    @Override
    public void runOpMode() throws InterruptedException {
        // debug

        camera = cameraInit();
        startStream();



        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            idle();
        }

    }

    public OpenCvCamera cameraInit() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        return OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
    }

    public void startStream() {
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                requestOpModeStop();
            }
        });
        dashboard.startCameraStream(camera, 30);
        cv = new HSV();
        camera.setPipeline(cv);
    }
}
