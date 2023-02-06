package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;


import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.OUTTAKE;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.SLIDES_HOME;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.highGoal;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.lowGoal;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.midGoal;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Intake.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Outtake.Outtake;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.IntakeWheels;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.OdoRetraction;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

// ඞ
@TeleOp(name = "Main", group = "ඞ")
public class Main extends LinearOpMode {
    SampleMecanumDrive drive;

    Timer timer = new Timer(this);

    private final FtcDashboard dashboard = FtcDashboard.getInstance();


    Slides slides;
    Claw claw;
    VirtualFourBar v4b;
    IntakeWheels intakeWheels;

    Boolean retracting = false;
    Boolean override = false;

    public enum RobotState {
        IDLE,
        INTAKING,
        INTAKE_IDLE,
        OUTTAKING_HIGH,
        OUTTAKING_MID,
        OUTTAKING_LOW,
        OUTTAKE_IDLE
    }

    RobotState robotState = RobotState.IDLE;

    @Override
    public void runOpMode() throws InterruptedException {

        //drivetrain
        drive = new SampleMecanumDrive(hardwareMap);

        //subsystems
        slides = new Slides(hardwareMap);
        claw = new Claw(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        intakeWheels = new IntakeWheels(hardwareMap);


        v4b.intake();
        slides.resetEncoders();
        claw.openWide();
        intakeWheels.idle();

        telemetry.addLine("Subsystems initialized .\n" +
                "              ⣠⣤⣤⣤⣤⣤⣶⣦⣤⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀ \n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⡿⠛⠉⠙⠛⠛⠛⠛⠻⢿⣿⣷⣤⡀⠀⠀⠀⠀⠀ \n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⣼⣿⠋⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠈⢻⣿⣿⡄⠀⠀⠀⠀ \n" +
                "⠀⠀⠀⠀⠀⠀⠀⣸⣿⡏⠀⠀⠀⣠⣶⣾⣿⣿⣿⠿⠿⠿⢿⣿⣿⣿⣄⠀⠀⠀ \n" +
                "⠀⠀⠀⠀⠀⠀⠀⣿⣿⠁⠀⠀⢰⣿⣿⣯⠁⠀⠀⠀⠀⠀⠀⠀⠈⠙⢿⣷⡄⠀ \n" +
                "⠀⠀⣀⣤⣴⣶⣶⣿⡟⠀⠀⠀⢸⣿⣿⣿⣆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣷⠀ \n" +
                "⠀⢰⣿⡟⠋⠉⣹⣿⡇⠀⠀⠀⠘⣿⣿⣿⣿⣷⣦⣤⣤⣤⣶⣶⣶⣶⣿⣿⣿⠀ \n" +
                "⠀⢸⣿⡇⠀⠀⣿⣿⡇⠀⠀⠀⠀⠹⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠃⠀ \n" +
                "⠀⣸⣿⡇⠀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠉⠻⠿⣿⣿⣿⣿⡿⠿⠿⠛⢻⣿⡇⠀⠀ \n" +
                "⠀⣿⣿⠁⠀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣧⠀⠀ \n" +
                "⠀⣿⣿⠀⠀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⠀⠀ \n" +
                "⠀⣿⣿⠀⠀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⠀⠀ \n" +
                "⠀⢿⣿⡆⠀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⡇⠀⠀ \n" +
                "⠀⠸⣿⣧⡀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⠃⠀⠀ \n" +
                "⠀⠀⠛⢿⣿⣿⣿⣿⣇⠀⠀⠀⠀⠀⣰⣿⣿⣷⣶⣶⣶⣶⠶⠀⢠⣿⣿⠀⠀⠀ \n" +
                "⠀⠀⠀⠀⠀⠀⠀⣿⣿⠀⠀⠀⠀⠀⣿⣿⡇⠀⣽⣿⡏⠁⠀⠀⢸⣿⡇⠀⠀⠀ \n" +
                "⠀⠀⠀⠀⠀⠀⠀⣿⣿⠀⠀⠀⠀⠀⣿⣿⡇⠀⢹⣿⡆⠀⠀⠀⣸⣿⠇⠀⠀⠀ \n" +
                "⠀⠀⠀⠀⠀⠀⠀⢿⣿⣦⣄⣀⣠⣴⣿⣿⠁⠀⠈⠻⣿⣿⣿⣿⡿⠏⠀⠀⠀⠀ \n" +
                "⠀⠀⠀⠀⠀⠀⠀⠈⠛⠻⠿⠿⠿⠿⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
        telemetry.update();

        waitForStart();


        while (opModeIsActive()) {

            telemetry.addData("robot state", robotState);

            switch(robotState) {


                case IDLE:
                    intakeWheels.idle();

                    if (gamepad1.x) {
                        robotState = RobotState.INTAKING;
                    }
                    break;

                case INTAKING:
                    retracting = false;
                    claw.openWide();
//                    intakeWheels.intake();
                    if (gamepad1.left_trigger > 0) {
                        claw.shutUp();
                        timer.safeDelay(100);
                        robotState = RobotState.INTAKE_IDLE;
                    }
                    break;
                case INTAKE_IDLE:

                    intakeWheels.outtake();
                    if (v4b.getPostion() < 0.3) {
                        slides.setCustom(200);
                    }

                    if (slides.getHeight() >= 130) {
                        v4b.intakeIdle();
                        intakeWheels.idle();
                    }

                    if (v4b.getPostion() >= 0.4) {
                        slides.retract();
                    }

                    if (gamepad1.y && v4b.getPostion() >= 0.4) {
                        robotState = RobotState.OUTTAKING_HIGH;
                    }
                    break;

                case OUTTAKING_HIGH:
                    slides.extendHigh();
                    if (slides.getHeight() >= highGoal - 600) {
                        v4b.outtake();
                    }

                    if (gamepad1.right_trigger > 0 && slides.getHeight() >= highGoal - 100) {
                        claw.openWide();
                        timer.safeDelay(50);
                        robotState = RobotState.OUTTAKE_IDLE;
                    }
                    break;
                case OUTTAKING_MID:
                    slides.extendMid();
                    if (slides.getHeight() >= midGoal - 600) {
                        v4b.outtake();
                    }

                    if (gamepad1.right_trigger > 0 && slides.getHeight() >= midGoal - 100) {
                        claw.openWide();
                        timer.safeDelay(50);
                        robotState = RobotState.OUTTAKE_IDLE;
                    }
                    break;

                case OUTTAKING_LOW:
                    slides.extendLow();
                    if (slides.getHeight() >= lowGoal - 600) {
                        v4b.outtake();
                    }

                    if (gamepad1.right_trigger > 0 && slides.getHeight() >= lowGoal - 100) {
                        claw.openWide();
                        timer.safeDelay(50);
                        robotState = RobotState.OUTTAKE_IDLE;
                    }
                    break;
                case OUTTAKE_IDLE:

                    if (gamepad1.x) {
                        retracting = true;
                    }

                    if (retracting) {
                        claw.shutUp();
                        v4b.intake();
                    }

                    if (retracting && (v4b.getPostion() <= 0.2)) {
                        slides.retract();
                    }

                    if (slides.getHeight() <= 50) {
                        robotState = RobotState.INTAKING;
                    }
                    break;



            }

            if (gamepad1.dpad_down) {
                override = true;
                robotState = RobotState.INTAKING;
                v4b.intake();
            }

            if (override && (v4b.getPostion() <= 0.2)) {
                slides.retract();
            }
            if (override && slides.getHeight() <= 50) {
                override = false;
                claw.openWide();
            }
            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x,
                            -gamepad1.right_stick_x
                    ));
            telemetry.update();
        }
    }
}
