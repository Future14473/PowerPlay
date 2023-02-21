package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;


import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Disabled
@TeleOp(group = "Testing")
public class ColorSensorTest extends LinearOpMode {

    RevColorSensorV3 color;

    @Override
    public void runOpMode() throws InterruptedException {

        color = hardwareMap.get(RevColorSensorV3.class, "color");


        waitForStart();
        while (opModeIsActive()) {

          telemetry.addData("red", color.red());
          telemetry.addData("blue", color.blue());
          telemetry.addData("green", color.green());

          telemetry.addData("distance", color.getDistance(DistanceUnit.CM));

          telemetry.update();
        }
    }




}
