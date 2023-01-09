package org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no;


import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.strafeY;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.strafeR;
import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.strafeX;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Drivetrain;

/** Tests the trajectory of RED 1+5 auto */

@Autonomous(name="SCREW ROAD RUNNER", group = "drive")
public class AutoTEST extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Drivetrain drivetrain = new Drivetrain(hardwareMap);

        waitForStart();




        // strafe to stack
        double initPos2 = drivetrain.getPosition().get(1);
        drivetrain.setPower(strafeY, strafeX,-strafeR);
        while(opModeIsActive()) {
            double currPos = drivetrain.getPosition().get(1);
            if (Math.abs(currPos - initPos2) >= 30) {
                drivetrain.setPower(0,0,0);
                break;
            }

        }

    }
}
