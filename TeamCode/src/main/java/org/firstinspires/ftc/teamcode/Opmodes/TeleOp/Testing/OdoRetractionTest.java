package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.OdoRetraction;

@TeleOp
public class OdoRetractionTest extends LinearOpMode {
    OdoRetraction odometry;

    @Override
    public void runOpMode() throws InterruptedException {
        odometry = new OdoRetraction(hardwareMap);

        if (gamepad1.x) {
            odometry.retract();
        }
        if (gamepad1.a) {
            odometry.down();
        }
    }
}
