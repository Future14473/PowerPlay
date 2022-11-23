package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ClawCompliant;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Config
@Disabled
@TeleOp(name="Main2", group="main")
public class MainNoV4B extends LinearOpMode {
    Timer timer;

    Slides slides;
    ServoTurret servoTurret;
    ClawCompliant claw;

    SampleMecanumDrive drive;


    double rotMult = 1.0;
    double mvmtMult = 1.0;

    boolean hasCone = false;

    @Override
    public void runOpMode() throws InterruptedException {


        drive = new SampleMecanumDrive(hardwareMap);


        slides = new Slides(hardwareMap);
        servoTurret = new ServoTurret(hardwareMap);
        claw = new ClawCompliant(hardwareMap);

        waitForStart();

        drive();

        while (opModeIsActive()) {
            gamepadOneControls();
            gamepadTwoControls();

            telemetry.addData("Has cone", hasCone);
            telemetry.update();
        }
    }

    public void gamepadOneControls() {

    }

    public void gamepadTwoControls() {
        if (gamepad2.a && !hasCone) {
            servoTurret.setHome();
            claw.openWide(true);
        } else if (gamepad2.a){
            claw.openWide(false);
        }
        //todo reduce timer if necessary
        if (gamepad2.x) {
            claw.shutUp();
            //timer.safeDelay(150);
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
                double y = -gamepad1.left_stick_y * Math.abs(gamepad1.left_stick_y);
                double x = gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x);
                double rx = gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x)* rotMult;

                double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                double leftFront = ((y + x + rx) / denominator) * mvmtMult;
                double leftRear = ((y - x + rx) / denominator) * mvmtMult;
                double rightRear = ((y + x - rx) / denominator) * mvmtMult;
                double rightFront = ((y - x - rx) / denominator) * mvmtMult;

                drive.setMotorPowers(leftFront * Math.abs(leftFront), leftRear * Math.abs(leftRear), rightRear * Math.abs(rightRear), rightFront * Math.abs(rightFront));
            }
        }).start();
    }
}
