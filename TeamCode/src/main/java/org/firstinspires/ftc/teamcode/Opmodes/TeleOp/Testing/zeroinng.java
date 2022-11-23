package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

//
@Config
@TeleOp(name = "Zer0TEST", group = "Testing")
public class zeroinng extends LinearOpMode {

    Servo leftv4bar;
    Servo rightv4bar;


    @Override
    public void runOpMode() throws InterruptedException {

        // servo

         leftv4bar = hardwareMap.get(Servo.class, "leftv4b");
         rightv4bar = hardwareMap.get(Servo.class, "rightv4b");



        waitForStart();
        while (opModeIsActive()) {

            if(gamepad1.a) {
                leftv4bar.setPosition(0);
                rightv4bar.setPosition(0);
                telemetry.addData("Position B - ", 0);
                telemetry.update();
            }
        }

    }




}
