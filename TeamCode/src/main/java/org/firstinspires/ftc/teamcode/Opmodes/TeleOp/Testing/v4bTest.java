package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;

@TeleOp(name="V4B Test", group = "Testing")
public class v4bTest extends LinearOpMode {

    VirtualFourBar v4b;

    @Override
    public void runOpMode() throws InterruptedException {
        v4b = new VirtualFourBar(hardwareMap);
        v4b.intake();

        waitForStart();

        while(opModeIsActive()) {
            if (gamepad1.x) {
                v4b.intake();
            } if (gamepad1.y) {
                v4b.outtake();
            }
        }
    }
}
