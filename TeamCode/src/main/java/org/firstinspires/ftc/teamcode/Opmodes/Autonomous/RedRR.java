package org.firstinspires.ftc.teamcode.Opmodes.Autonomous;

import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.moveBackFromStack;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.moveBackIntoPole;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.moveStraightToStack;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.preloadAngle;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.preloadDistance;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.redStart;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.strafeToStackFromPole;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware.Intake.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Outtake.Outtake;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.TwoWheelTrackingLocalizer;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
public class RedRR extends LinearOpMode {

    Slides slides;
    Claw claw;
    VirtualFourBar v4b;
    ServoTurret servoTurret;

    Intake intake;
    Outtake outtake;

    SampleMecanumDrive drive;
    TwoWheelTrackingLocalizer localizer;

    double targetAngle;


    @Override
    public void runOpMode() {
        PhotonCore.enable();

        Timer timer = new Timer(this);

        drive = new SampleMecanumDrive(hardwareMap);
        localizer = new TwoWheelTrackingLocalizer(hardwareMap, drive);

        slides = new Slides(hardwareMap);
        claw = new Claw(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        servoTurret = new ServoTurret(hardwareMap);

        intake = new Intake(slides, claw, v4b, servoTurret);
        outtake = new Outtake(slides, claw, v4b, servoTurret);

        intake.teleopIntakeReady();

        targetAngle = localizer.getHeading()+90;

        telemetry.addLine("Subsystems Initialized\nPlease work you monkey");
        telemetry.update();

        waitForStart();

        intake.intake();

        //move straight to preload (RR PID)
        TrajectorySequence moveStraightToPreload = drive.trajectorySequenceBuilder(new Pose2d())
                .forward(preloadDistance)
                .addDisplacementMarker(preloadDistance / 2, () -> {
                    outtake.outtakeReadyHigh(timer);
                })
                .build();

        drive.followTrajectorySequence(moveStraightToPreload);


        //turn and move back to drop
        setMotorPower(0,0,0.6);
        double initAngle = localizer.getHeading();
        while (opModeIsActive()) {
            double currAngle = localizer.getHeading();

            if(getRelativeAngle(initAngle, currAngle) >= preloadAngle) {
                setMotorPower(0,0,0);
                timer.safeDelay(50);
                setMotorPower(-0.5,0,0);
                double initPos = localizer.getWheelPositions().get(0);
                while (opModeIsActive()) {
                    double currPos = drive.getWheelPositions().get(0);
                    if (Math.abs(currPos - initPos) > moveBackIntoPole) {
                        setMotorPower(0,0,0);
                        correctAngularError();
                        break;
                    }
                }
                break;
            }
        }

        outtake.outtakeAuto(timer);

        setMotorPower(0.5, 0,0);
        timer.safeDelay(100);
        setMotorPower(0,0,0);

        //strafe to stack
        TrajectorySequence strafeToPole = drive.trajectorySequenceBuilder(new Pose2d())
                .strafeLeft(strafeToStackFromPole)
                .addDisplacementMarker(strafeToStackFromPole/2, () -> {
                    intake.autoIntakeReady(1, timer);
                })
                .build();

        drive.followTrajectorySequence(strafeToPole);

        //move forward to stack
        TrajectorySequence moveForwardToStack = drive.trajectorySequenceBuilder(new Pose2d())
                .forward(moveStraightToStack)
                .build();

        drive.followTrajectorySequence(moveForwardToStack);

        //move back from stack
        TrajectorySequence moveBackFromStackToPole = drive.trajectorySequenceBuilder(new Pose2d())
                .back(moveBackFromStack)
                .build();


        drive.followTrajectorySequence(moveBackFromStackToPole);


    }

    public void setMotorPower(double y, double x, double rx) {

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double leftFront = ((y + x + rx) / denominator);
        double leftRear = ((y - x + rx) / denominator);
        double rightRear = ((y + x - rx) / denominator);
        double rightFront = ((y - x - rx) / denominator);

        drive.setMotorPowers(leftFront, leftRear, rightRear, rightFront);
    }

    public void correctAngularError() {
        double initHeading = localizer.getHeading();

        if (initHeading > targetAngle) {
            setMotorPower(0,0,-0.5);
            while (opModeIsActive()) {
                if (localizer.getHeading() < targetAngle) {
                    setMotorPower(0,0,0);
                    break;
                }
            }
        } else {
            setMotorPower(0,0,0.5);
            while (opModeIsActive()) {
                if (localizer.getHeading() > targetAngle) {
                    setMotorPower(0,0,0);
                    break;
                }
            }
        }
    }

    public double getRelativeAngle(double init, double target) {
        if (Math.abs(init - target) <= 180) {
            return Math.abs(init - target);
        } else {
            return 360 - Math.abs(init - target);
        }
    }

}
