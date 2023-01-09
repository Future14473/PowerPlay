package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Drivetrain;

@TeleOp
public class NewDtTest extends LinearOpMode {

    Drivetrain drivetrain;


    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new Drivetrain(hardwareMap);
        waitForStart();

        while(opModeIsActive()) {
            telemetry.addData("x", drivetrain.getPosition().get(0));
            telemetry.addData("y", drivetrain.getPosition().get(1));
            telemetry.addData("heading", drivetrain.getHeading());
            telemetry.update();
        }
    }

    public void dt() {
        new Thread(() -> {
            while (opModeIsActive()) {

            }
        }).start();
    }
}
