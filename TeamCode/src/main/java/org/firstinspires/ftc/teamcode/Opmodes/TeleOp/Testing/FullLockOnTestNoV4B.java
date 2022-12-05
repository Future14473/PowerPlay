package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.VSLAM.LockOnOuttake;
import org.firstinspires.ftc.teamcode.Hardware.VSLAM.t265RRLocalizer;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.opmode.TwoWheelTrackingLocalizer;

@TeleOp(name="Lockon shit", group="main")
public class FullLockOnTestNoV4B extends LinearOpMode {
    Timer timer;

    Slides slides;
    ServoTurret servoTurret;
    Claw claw;

    SampleMecanumDrive drive;

    t265RRLocalizer slamra;
    LockOnOuttake lockon;
    Pose2d robotPosVSLAM = new Pose2d();
    Pose2d robotPosOdo = new Pose2d();


    double rotMult = 1.0;
    double mvmtMult = 1.0;

    boolean hasCone = false;

    @Override
    public void runOpMode() throws InterruptedException {

        drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drive.setLocalizer(new TwoWheelTrackingLocalizer(hardwareMap, drive));


        slides = new Slides(hardwareMap);
        servoTurret = new ServoTurret(hardwareMap);
        claw = new Claw(hardwareMap);

        // KEEP THIS AS 90!!!!!!!
        Pose2d startPos = new Pose2d(-34.5, -72, Math.toRadians(90));
        lockon = new LockOnOuttake();
        slamra.setPoseEstimate(startPos);

        waitForStart();

        drive();

        while (opModeIsActive()) {
            robotPosVSLAM = slamra.getPoseEstimate();
            robotPosOdo = drive.getPoseEstimate();

            gamepadOneControls();
            gamepadTwoControls();

            telemetryConfig();
            slamra.update();
        }
    }

    public void telemetryConfig() {
        telemetry.addData("VSLAM Position", robotPosVSLAM);
        telemetry.addData("Deadwheel position",robotPosOdo);
        telemetry.addData("Has cone", hasCone);
        telemetry.update();
    }

    public void gamepadOneControls() {
        if (gamepad1.x) {
        }
        if (gamepad1.a) {
            servoTurret.setHome();
        }
    }

    public void gamepadTwoControls() {
        if (gamepad2.a && !hasCone) {
            servoTurret.setHome();
            claw.openWide();
        } else if (gamepad2.a){
            claw.openWide();
        }
        //todo reduce timer if necessary
        if (gamepad2.x) {
            claw.shutUp();
            timer.safeDelay(150);
            slides.extendJunction();

        }
        if (gamepad2.b) {
            slides.extendLow();
        }
        if (gamepad2.y) {
            slides.extendMid();
        }

        //todo set max values
        if (gamepad2.left_stick_x > 0) {
            servoTurret.incrementUp();
        } else if (gamepad2.left_stick_x < 0) {
            servoTurret.incrementDown();
        }

        if (gamepad2.right_bumper) {
            servoTurret.setHome();
        }
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
//                drive.setMotorPowers(leftFront * Math.abs(leftFront), leftRear * Math.abs(leftRear), rightRear * Math.abs(rightRear), rightFront * Math.abs(rightFront));


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
