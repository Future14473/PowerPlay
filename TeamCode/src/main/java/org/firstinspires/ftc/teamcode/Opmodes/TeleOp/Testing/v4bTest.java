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
        v4b.setHome();

        waitForStart();

        while(opModeIsActive()) {
            if (gamepad1.x) {
                v4b.setHome();
            } if (gamepad1.y) {
                v4b.outHigh();
            } if (gamepad1.b) {
                v4b.outMid();
            } if (gamepad1.a) {
                v4b.outLow();
            }
        }
    }
}
