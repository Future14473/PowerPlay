package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;


import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.highGoal;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.lowGoal;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.midGoal;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.IntakeWheels;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDriveCancelable;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

// ඞ
@TeleOp(name = "Main", group = "ඞ")
public class Main extends LinearOpMode {
    SampleMecanumDriveCancelable drive;

    Timer timer = new Timer(this);

    private final FtcDashboard dashboard = FtcDashboard.getInstance();


    Slides slides;
    Claw claw;
    VirtualFourBar v4b;
    IntakeWheels intakeWheels;

    Boolean retracting = false;
    Boolean override = false;
    Boolean autoOuttakeReady = false;
    Boolean autoIntakeReady = false;

    double driveSpeedModifier;

    public enum RobotState {
        IDLE,
        INTAKING,
        INTAKE_IDLE,
        OUTTAKING_HIGH,
        OUTTAKING_MID,
        OUTTAKING_LOW,
        OUTTAKE_IDLE,
        AUTO,
        AUTO_INTAKE
    }

    RobotState robotState = RobotState.IDLE;

    @Override
    public void runOpMode() throws InterruptedException {

        //drivetrain
        drive = new SampleMecanumDriveCancelable(hardwareMap);

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
                "                          ⣠⣤⣤⣤⣤⣤⣶⣦⣤⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀ \n" +
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

            switch (robotState) {


                case IDLE:
                    intakeWheels.idle();
                    driveSpeedModifier = 1;

                    if (gamepad2.x) {
                        robotState = RobotState.INTAKING;
                    }
                    break;

                case INTAKING:
                    retracting = false;
                    if (v4b.getPostion() > 0.02) claw.shutUp();
                    else if (v4b.getPostion() <= 0.02) claw.openWide();
                    intakeWheels.intake();
                    if (gamepad1.left_bumper) {
                        claw.shutUp();
                        intakeWheels.idle();
                        timer.safeDelay(100);
                        robotState = RobotState.INTAKE_IDLE;
                    }

                    if (gamepad1.dpad_left) {
                        claw.shutUp();
                        TrajectorySequence moveBackToPole = drive.trajectorySequenceBuilder(new Pose2d())
                                .back(15)
                                .addDisplacementMarker(5, () -> {
                                    slides.extendHigh();
                                    v4b.outtake();
                                })
                                .build();
                        timer.safeDelay(50);
                        drive.followTrajectorySequenceAsync(moveBackToPole);
                        robotState = RobotState.AUTO;
                    }

                    if (autoIntakeReady) {
                        TrajectorySequence moveStraightToPole = drive.trajectorySequenceBuilder(new Pose2d())
                                .forward(15)
                                .addDisplacementMarker(5, () -> {
                                    v4b.intake();
                                    slides.retract();
                                })
                                .build();
                        drive.followTrajectorySequenceAsync(moveStraightToPole);
                        robotState = RobotState.AUTO;
                    }
                    break;
                case INTAKE_IDLE:
                    claw.shutUp();
                    intakeWheels.outtake();
                    if (v4b.getPostion() < 0.3) {
                        slides.setCustom(500);
                    }

                    if (slides.getHeight() >= 400) {
                        v4b.intakeIdle();
                    }

                    if (v4b.getPostion() >= 0.4) {
                        slides.retract();
                    }

                    if (gamepad2.y && v4b.getPostion() >= 0.4) {
                        intakeWheels.idle();
                        robotState = RobotState.OUTTAKING_HIGH;
                    }
                    if (gamepad2.b && v4b.getPostion() >= 0.4) {
                        intakeWheels.idle();
                        robotState = RobotState.OUTTAKING_MID;
                    }
                    if (gamepad2.a && v4b.getPostion() >= 0.4) {
                        intakeWheels.idle();
                        robotState = RobotState.OUTTAKE_IDLE;
                    }
                    break;

                case OUTTAKING_HIGH:
                    driveSpeedModifier = 0.6;
                    slides.extendHigh();
                    if (slides.getHeight() >= highGoal - 600) {
                        v4b.outtake();
                    }

                    if (gamepad2.right_trigger>0 && slides.getHeight() >= highGoal - 400) {
                        claw.openWide();
                        timer.safeDelay(50);
                        robotState = RobotState.OUTTAKE_IDLE;
                    }
                    break;
                case OUTTAKING_MID:
                    driveSpeedModifier = 0.6;
                    slides.extendMid();
                    if (slides.getHeight() >= midGoal - 600) {
                        v4b.outtake();
                    }

                    if (gamepad2.right_trigger>0 & slides.getHeight() >= midGoal - 100) {
                        claw.openWide();
                        timer.safeDelay(50);
                        robotState = RobotState.OUTTAKE_IDLE;
                    }
                    break;

                case OUTTAKING_LOW:
                    driveSpeedModifier = 0.6;
                    slides.extendLow();
                    if (slides.getHeight() >= lowGoal - 600) {
                        v4b.outtake();
                    }

                    if (gamepad2.right_trigger>00 && slides.getHeight() >= lowGoal - 100) {
                        claw.openWide();
                        timer.safeDelay(50);
                        robotState = RobotState.OUTTAKE_IDLE;
                    }
                    break;
                case OUTTAKE_IDLE:
                    driveSpeedModifier = 0.6;
                    if (gamepad2.x) {
                        driveSpeedModifier = 1;
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
                case AUTO:
                    if (!drive.isBusy() && !autoIntakeReady) {
                        autoOuttakeReady = true;
                    }

                    if (!drive.isBusy() && autoIntakeReady) {
                        claw.shutUp();
                        autoIntakeReady = false;
                        robotState = RobotState.INTAKING;
                    }

                    if (autoOuttakeReady) {
                        v4b.setCustom(0.75);
                    }

                    if (autoOuttakeReady && (v4b.getPostion() >= 0.7)) {
                        claw.openWide();
                        autoIntakeReady = true;
                        robotState = RobotState.INTAKING;

                    }

                    if (gamepad2.x) {
                        drive.breakFollowing();
                        override = true;
                        robotState = RobotState.INTAKING;
                    }
                    break;

            }

            if (gamepad2.dpad_down) {
                driveSpeedModifier = 1;
                override = true;
                claw.shutUp();
                robotState = RobotState.INTAKING;
            }
            if (override) {
                v4b.intake();
            }

            if (override && (v4b.getPostion() <= 0.2)) {
                slides.retract();
            }
            if (override && slides.getHeight() <= 50 && (v4b.getPostion() <= 0.2)) {
                override = false;
                claw.openWide();
            }

            if (gamepad2.left_bumper) {
                claw.openWide();
            }
            if (gamepad2.right_bumper) {
                claw.shutUp();
            }
            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y * driveSpeedModifier,
                            -gamepad1.left_stick_x * driveSpeedModifier,
                            -gamepad1.right_stick_x * driveSpeedModifier
                    ));
            telemetry.update();
        }
    }
}
