package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Hardware.VSLAM.t265RRLocalizer;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Config
@Disabled
@TeleOp(name = "AutoSpline", group = "Testing")
public class DriveSpline extends LinearOpMode {
    SampleMecanumDrive drive;

    t265RRLocalizer slamra;

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static double mvmtMult = 1.1;



    Pose2d robotPos = new Pose2d();

    enum driveMode {
        DRIVER,
        AUTO
    }

    driveMode currentMode = driveMode.DRIVER;

    @Override
    public void runOpMode() throws InterruptedException {


        drive = new SampleMecanumDrive(hardwareMap);


        Timer timer = new Timer(this);

        waitForStart();


        //vslam
        slamra = new t265RRLocalizer(hardwareMap);
        slamra.setPoseEstimate(new Pose2d(0, 0, Math.toRadians(90)));
        drive.setPoseEstimate(slamra.getPoseEstimate());


        while (opModeIsActive()) {

            robotPos = slamra.getPoseEstimate();

            telemetryConfig(telemetry);
            slamra.update();

            switch (currentMode) {
                case DRIVER:
                    gamepad1Controls();
                    break;
                case AUTO:
                    if (gamepad1.x) {

                        currentMode = driveMode.DRIVER;
                    }

                    if (!drive.isBusy()) {
                        currentMode = driveMode.DRIVER;
                    }
                    break;
            }

        }
    }




    public void gamepad1Controls() {

        double y = gamepad1.left_stick_x;
        double x = -gamepad1.left_stick_y * 1.1;
        double rx = -gamepad1.right_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double leftFront = ((y + x + rx) / denominator) * mvmtMult;
        double leftRear = ((y - x + rx) / denominator) * mvmtMult;
        double rightRear = ((y + x - rx) / denominator) * mvmtMult;
        double rightFront = ((y - x - rx) / denominator) * mvmtMult;

        drive.setMotorPowers(leftFront, leftRear, -rightRear, -rightFront);

        if (gamepad1.a) {
            currentMode = driveMode.AUTO;

        }


    }

    public void telemetryConfig(Telemetry telemetry) {

        telemetry.addData("Robot Position", robotPos);

        telemetry.update();
    }
}
