package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ClawCompliant;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;

//
@Config
@TeleOp(name = "SlidesTEST", group = "Testing")
public class SlidesTest extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        Slides slides = new Slides(hardwareMap);
        slides.resetEncoders();





        waitForStart();
        while (opModeIsActive()) {
            if(gamepad1.a) {
            } else if (gamepad1.x) {
                slides.retract();
            } else if (gamepad1.y) {
                slides.extendHigh();
            }
        }

    }
}
