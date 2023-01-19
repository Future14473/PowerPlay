package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ClawCompliant;

@Disabled
@TeleOp(name="Claw Test", group="Testing")
public class ClawTest extends LinearOpMode {
     Claw claw;

    @Override
    public void runOpMode() throws InterruptedException {
        claw = new Claw(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                claw.openWide();
            }

            if (gamepad1.x) {
                claw.shutUp();
            }
            if  (gamepad1.b) {
                claw.openWide();
            }
        }

    }

}
