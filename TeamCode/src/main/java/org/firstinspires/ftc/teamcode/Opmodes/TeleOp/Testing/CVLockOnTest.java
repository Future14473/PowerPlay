package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.HOME_POS_TURRET;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.MAX_ROTATION_DEGREES;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.OUT_POS_TURRET;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.ComputerVision.AprilTag;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Disabled
@TeleOp
public class CVLockOnTest extends LinearOpMode {

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    Servo pointServo;

    public OpenCvCamera camera;
    public AprilTag cv;

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

    @Override
    public void runOpMode() throws InterruptedException {
        pointServo = hardwareMap.get(Servo.class, "servoTurret");

        camera = cameraInit();
        startStream();

        pointServo.setPosition(HOME_POS_TURRET);
        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            computerVision(cv);
        }
    }

    public void computerVision(AprilTag cv) {
        new Thread(() -> {
            while (true) {
                if (cv.lockOnCV(1) == 0) {
                    telemetry.addLine("Pole not found");
                    pointServo.setPosition(HOME_POS_TURRET);
                } else {
                    telemetry.addData("DistancePixels", cv.lockOnCV(1));
                    telemetry.addData("Servo Position", pointServo.getPosition());

                }
                telemetry.update();
            }
        }).start();

    }

    public OpenCvCamera cameraInit() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        return OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "back"), cameraMonitorViewId);
    }

    public void startStream() {
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                // GPU-accelerated render!
                camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                requestOpModeStop();
            }
        });
        dashboard.startCameraStream(camera, 0);
        cv = new AprilTag(tagsize, fx, fy, cx, cy);
        camera.setPipeline(cv);
    }
}
