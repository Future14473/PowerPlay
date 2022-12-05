package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Zer0TEST", group = "Testing")
public class zeroinng extends LinearOpMode {

    Servo servo;

    @Override
    public void runOpMode() throws InterruptedException {

         servo = hardwareMap.get(Servo.class, "servoTurret");

        waitForStart();
        while (opModeIsActive()) {
            servo.setPosition(0);
        }
    }
}
