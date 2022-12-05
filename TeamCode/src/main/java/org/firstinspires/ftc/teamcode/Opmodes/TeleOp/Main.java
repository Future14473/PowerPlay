package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;


import static org.firstinspires.ftc.teamcode.Constants.OpmodeConstants.mvmtMult;
import static org.firstinspires.ftc.teamcode.Constants.OpmodeConstants.rotMult;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;


@TeleOp(name = "Main", group = "Linear Opmode")
public class Main extends LinearOpMode {
    SampleMecanumDrive drive;


    private final FtcDashboard dashboard = FtcDashboard.getInstance();



    Slides slides;
    Claw claw;


    @Override
    public void runOpMode() throws InterruptedException {


        //drivetrain
        drive = new SampleMecanumDrive(hardwareMap);


        slides = new Slides(hardwareMap);
        slides.resetEncoders();


        claw = new Claw(hardwareMap);
        claw.openWide();

        waitForStart();

        drive();

        while (opModeIsActive()) {

            // gamepad 1 controls
            if (gamepad1.left_bumper) {
                claw.openWide();
            }

            if (gamepad1.right_bumper) {
                claw.shutUp();

            }

            // gamepad 2 controls
            if (gamepad1.b) {
                slides.extendLow();
            }

            if (gamepad1.y) {
                slides.extendMid();

            } if (gamepad1.a) {
                slides.extendJunction();
            }

            if (gamepad1.x) {
                slides.retract();
            }
        }
    }

    public void drive() {
        new Thread(() -> {
            while (opModeIsActive()) {
                double y = -gamepad1.left_stick_y;
                double x = -gamepad1.left_stick_x;
                double rx = gamepad1.right_stick_x * rotMult;


                double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                double leftFront = ((y + x + rx) / denominator) * mvmtMult;
                double leftRear = ((y - x + rx) / denominator) * mvmtMult;
                double rightRear = ((y + x - rx) / denominator) * mvmtMult;
                double rightFront = ((y - x - rx) / denominator) * mvmtMult;

                drive.setMotorPowers(leftFront, leftRear, rightRear, rightFront);

            }
        }).start();
    }
}
