package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.util.Encoder;

@Autonomous
public class EncoderTest extends LinearOpMode {
    private Encoder parallelEncoder, perpendicularEncoder;
    public void runOpMode() throws InterruptedException {


        waitForStart();

        parallelEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "rightSlide"));
        perpendicularEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "encoder"));


        parallelEncoder.setDirection(Encoder.Direction.REVERSE);
        perpendicularEncoder.setDirection(Encoder.Direction.REVERSE);



        while (!isStopRequested()) {

            telemetry.addData("parallelEncoder: ", parallelEncoder.getCurrentPosition());
            telemetry.addData("perpendicularEncoder", perpendicularEncoder.getCurrentPosition());
            telemetry.update();
        }
    }
}
