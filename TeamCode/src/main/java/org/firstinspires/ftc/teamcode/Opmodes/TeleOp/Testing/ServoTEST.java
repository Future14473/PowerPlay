package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;


import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.HOME_POS_V4B;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.OUT_V4B;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

//

@TeleOp(name = "ServoTEST", group = "Testing")
public class ServoTEST extends LinearOpMode {

    Servo right;
    Servo left;


    //todo this is to check which one to reverse
    @Override
    public void runOpMode() throws InterruptedException {

        right = hardwareMap.get(Servo.class, "rightV4b");
        left = hardwareMap.get(Servo.class, "leftV4b");


        waitForStart();
        while (opModeIsActive()) {

            if(gamepad1.b) {
                right.setPosition(HOME_POS_V4B);
            } if (gamepad1.a) {
                left.setPosition(HOME_POS_V4B);
            }
            if (gamepad1.x) {
                right.setPosition(OUT_V4B);
                left.setPosition(OUT_V4B);
            }
        }
    }




}
