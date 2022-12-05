package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ClawCompliant;


@TeleOp(name="Claw Test", group="Testing")
public class ClawTest extends LinearOpMode {
    ClawCompliant claw;

    @Override
    public void runOpMode() throws InterruptedException {
        claw = new ClawCompliant(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                claw.openWide(true);
            }

            if (gamepad1.x) {
                claw.shutUp();
            }
            if  (gamepad1.b) {
                claw.openWide(false);
            }
        }

    }

}
