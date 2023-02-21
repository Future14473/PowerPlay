package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
public class RangeSensorTest extends LinearOpMode {
    ModernRoboticsI2cRangeSensor rangeSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");

        waitForStart();

        while(opModeIsActive()) {
            telemetry.addData("cm", rangeSensor.getDistance(DistanceUnit.CM));
            telemetry.update();
        }
    }
}
