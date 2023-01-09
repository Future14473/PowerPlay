package org.firstinspires.ftc.teamcode.Hardware.Subsystems;

import static org.firstinspires.ftc.teamcode.Opmodes.Autonomous.no.AutoConstants.preloadDistance;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.opmode.util.AxisDirection;
import org.firstinspires.ftc.teamcode.drive.opmode.util.BNO055IMUUtil;
import org.firstinspires.ftc.teamcode.drive.opmode.util.Encoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Drivetrain {
    public static double TICKS_PER_REV = 8192;
    public static double WHEEL_RADIUS = 0.688975; // in
    public static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    public static double PARALLEL_X = 0; // X is the up and down direction
    public static double PARALLEL_Y = 5.25; // Y is the strafe direction

    public static double PERPENDICULAR_X = -5.25;
    public static double PERPENDICULAR_Y = 0;

    public static double X_MULTIPLIER = 1.054945; // Multiplier in the X direction
    public static double Y_MULTIPLIER = 1.0644827; // Multiplier in the Y direction

    SampleMecanumDrive drive;

    double speedMult = 1;
    double linearMult = 1;
    double rotationalMult = 1;
    double strafeMult = 1;

    private Encoder parallelEncoder, perpendicularEncoder;
    private BNO055IMU imu;


    public Drivetrain(HardwareMap hardwareMap) {

        drive = new SampleMecanumDrive(hardwareMap);

        parallelEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "rightFront"));
        perpendicularEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "rightRear"));

        parallelEncoder.setDirection(Encoder.Direction.REVERSE);

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(parameters);

        BNO055IMUUtil.remapZAxis(imu, AxisDirection.NEG_Y);
    }

    public double getHeading() {
        return imu.getAngularOrientation().firstAngle;
    }

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    public List<Double> getPosition() {
        return Arrays.asList(
            encoderTicksToInches(parallelEncoder.getCurrentPosition()) * X_MULTIPLIER,
            encoderTicksToInches(perpendicularEncoder.getCurrentPosition()) * Y_MULTIPLIER
        );
    }

    public void setPower(double y, double x, double rx) {
        x = x * strafeMult;
        y = y * linearMult;
        rx = rx * rotationalMult;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double leftFront = ((y + x + rx) / denominator) * speedMult;
        double leftRear = ((y - x + rx) / denominator) * speedMult;
        double rightRear = ((y + x - rx) / denominator) * speedMult;
        double rightFront = ((y - x - rx) / denominator) * speedMult;

        drive.setMotorPowers(leftFront * Math.abs(leftFront), leftRear * Math.abs(leftRear), rightRear * Math.abs(rightRear), rightFront * Math.abs(rightFront));
    }


}
