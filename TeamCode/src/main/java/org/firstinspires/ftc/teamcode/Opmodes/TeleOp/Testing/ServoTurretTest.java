package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.HOME_POS_TURRET;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.OUT_POS_TURRET;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;

@TeleOp
public class ServoTurretTest extends LinearOpMode {
    Servo servoTurret;

    @Override
    public void runOpMode() throws InterruptedException {

        servoTurret = hardwareMap.get(Servo.class, "servoTurret");

        waitForStart();

        while(opModeIsActive()) {
            if (gamepad1.x) {
                servoTurret.setPosition(HOME_POS_TURRET);
            }
            if (gamepad1.b) {
                servoTurret.setPosition(OUT_POS_TURRET);
            }
        }
    }
}
