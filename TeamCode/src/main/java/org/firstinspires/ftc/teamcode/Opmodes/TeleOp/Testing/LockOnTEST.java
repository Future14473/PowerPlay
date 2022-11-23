package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;


import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.VSLAM.LockOnOuttake;
import org.firstinspires.ftc.teamcode.Hardware.VSLAM.t265RRLocalizer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.opmode.TwoWheelTrackingLocalizer;

//
@Config
@TeleOp(name = "LockOnTEST", group = "Testing")
public class LockOnTEST extends LinearOpMode {
    SampleMecanumDrive drive;

    t265RRLocalizer slamra;

    public static double rotMult = 0.6, mvmtMult = 0.6;

    ServoTurret pointServo;

    Pose2d robotPosVSLAM = new Pose2d();
    Pose2d robotPosODO = new Pose2d();

    public LockOnOuttake lockon = new LockOnOuttake();

    public boolean outtaking = false;

    @Override
    public void runOpMode() throws InterruptedException {

        //drivetrain
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drive.setLocalizer(new TwoWheelTrackingLocalizer(hardwareMap, drive));


        //vslam
        slamra = new t265RRLocalizer(hardwareMap);
        slamra.setPoseEstimate(new Pose2d(0, 0, Math.toRadians(0)));

        pointServo = new ServoTurret(hardwareMap);
        pointServo.setHome();

        waitForStart();

        drive();

        while (opModeIsActive()) {
            robotPosVSLAM = slamra.getPoseEstimate();

            pointServo.setFractional(lockon.lockOn(robotPosODO));
            telemetryConfig(telemetry);
            drive.update();
            slamra.update();

            if (gamepad1.a) {
                slamra.setPoseEstimate(new Pose2d(0, 0, 0));
                drive.setPoseEstimate(new Pose2d(0, 0, 0));
            }
        }

        if (isStopRequested()) {
            slamra.stop();
        }
    }


    @SuppressLint("DefaultLocale")
    public void telemetryConfig(Telemetry telemetry) {
        telemetry.addData("LockOn angle", lockon.lockOn(robotPosODO));
        telemetry.addData("Servo Position", pointServo.getCurrPos());
        telemetry.addData("Robot Position VSLAM", String.format("(%1$f, %2$f, %3$f)", robotPosVSLAM.getX(), robotPosVSLAM.getY(), robotPosVSLAM.getHeading()));
        telemetry.addData("Robot Position ODO", String.format("(%1$f, %2$f, %3$f)", robotPosODO.getX(), robotPosODO.getY(), robotPosODO.getHeading()));
        telemetry.update();
    }


    public void drive() {
        new Thread(() -> {
            while (opModeIsActive()) {
//                double y = -gamepad1.left_stick_y;
//                double x = gamepad1.left_stick_x;
//                double rx = gamepad1.right_stick_x * rotMult;
//
//                double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
//                double leftFront = ((y + x + rx) / denominator) * mvmtMult;
//                double leftRear = ((y - x + rx) / denominator) * mvmtMult;
//                double rightRear = ((y + x - rx) / denominator) * mvmtMult;
//                double rightFront = ((y - x - rx) / denominator) * mvmtMult;
//
//                drive.setMotorPowers(leftFront, leftRear, rightRear, rightFront);

                // METHOD 2 todo test with controller - like rn
                drive.setWeightedDrivePower(
                        new Pose2d(
                                -gamepad1.left_stick_y,
                                -gamepad1.left_stick_x,
                                -gamepad1.right_stick_x
                        )
                );
            }
        }).start();
    }
}
