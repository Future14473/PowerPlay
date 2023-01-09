package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;


import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Intake.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Outtake.Outtake;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Disabled
@TeleOp(name = "Main2", group = "Linear Opmode")
public class Main2 extends LinearOpMode {
    SampleMecanumDrive drive;

    Timer timer = new Timer(this);

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    Slides slides;
    Claw claw;
    VirtualFourBar v4b;
    ServoTurret servoTurret;

    Intake intake;
    Outtake outtake;
    
    double mvmtMultChildren = 0.8;
    double rotMultChildren = 0.8;

    @Override
    public void runOpMode() throws InterruptedException {

        //drivetrain
        drive = new SampleMecanumDrive(hardwareMap);

        slides = new Slides(hardwareMap);
        claw = new Claw(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        servoTurret = new ServoTurret(hardwareMap);

        intake = new Intake(slides, claw, v4b, servoTurret);
        outtake = new Outtake(slides, claw, v4b, servoTurret);

        intake.teleopIntakeReady();

        waitForStart();

        drive();

        while (opModeIsActive()) {
            if (gamepad1.right_bumper) {
                outtake.outtake();
            }

            if (gamepad1.left_bumper) {
                intake.intake();
            }

            if (gamepad1.x) {
                intake.teleopIntake(timer);
            }

            if (gamepad1.y) {
                outtake.outtakeReadyHigh(timer);
            }
        }
    }

    public void drive() {
        new Thread(() -> {
            while (opModeIsActive()) {
                while (opModeIsActive()) {
                    double y = -gamepad1.left_stick_y;
                    double x = -gamepad1.left_stick_x;
                    double rx = -gamepad1.right_stick_x * rotMultChildren;

                    double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                    double leftFront = ((y + x + rx) / denominator) *mvmtMultChildren;
                    double leftRear = ((y - x + rx) / denominator) *mvmtMultChildren;
                    double rightRear = ((y + x - rx) / denominator) *mvmtMultChildren;
                    double rightFront = ((y - x - rx) / denominator) *mvmtMultChildren;

                    drive.setMotorPowers(leftFront * Math.abs(leftFront), leftRear * Math.abs(leftRear), rightRear * Math.abs(rightRear), rightFront * Math.abs(rightFront));
                }
            }
        }).start();
    }
}
