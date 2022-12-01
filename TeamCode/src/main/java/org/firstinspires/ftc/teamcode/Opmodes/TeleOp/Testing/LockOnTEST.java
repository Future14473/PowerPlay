package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;


import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.VSLAM.LockOnOuttake;
import org.firstinspires.ftc.teamcode.Hardware.VSLAM.t265RRLocalizer;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.opmode.TwoWheelTrackingLocalizer;

@TeleOp(name = "LockOnTEST", group = "Testing")
public class LockOnTEST extends LinearOpMode {


    t265RRLocalizer slamra;

    Servo pointServo;

    Pose2d robotPosVSLAM = new Pose2d();

    public LockOnOuttake lockon = new LockOnOuttake();


    @Override
    public void runOpMode() throws InterruptedException {

        //vslam
        slamra = new t265RRLocalizer(hardwareMap);
        slamra.setPoseEstimate(new Pose2d(0, 0, Math.toRadians(0)));

        pointServo = hardwareMap.get(Servo.class, "pointServo");
        pointServo.setPosition(0.5);

        waitForStart();

        while (opModeIsActive()) {
            robotPosVSLAM = slamra.getPoseEstimate();

            pointServo.setPosition(lockon.lockOn(robotPosVSLAM));
            slamra.update();
            telemetryConfig(telemetry);
        }

        if (isStopRequested()) {
            slamra.stop();
        }
    }


    @SuppressLint("DefaultLocale")
    public void telemetryConfig(Telemetry telemetry) {
        telemetry.addData("Servo Position", pointServo.getPosition());
        telemetry.addData("moveAngle", lockon.lockOn(robotPosVSLAM));
        telemetry.addData("trim", lockon.getTrim());
        telemetry.addData("Robot Position VSLAM", String.format("(%1$f, %2$f, %3$f)", robotPosVSLAM.getX(), robotPosVSLAM.getY(), robotPosVSLAM.getHeading()));
        telemetry.update();
    }

}
