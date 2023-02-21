package org.firstinspires.ftc.teamcode.Hardware.Subsystems;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.INTAKE_WHEEL_SPEED;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IntakeWheels {

    DcMotorEx leftDong;
    DcMotorEx rightDong;

    public IntakeWheels(HardwareMap hardwareMap) {
        leftDong = hardwareMap.get(DcMotorEx.class, "leftDong");
        rightDong = hardwareMap.get(DcMotorEx.class, "rightDong");

        leftDong.setDirection(DcMotorEx.Direction.REVERSE);

        leftDong.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDong.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }



    public void intake() {
        leftDong.setPower(INTAKE_WHEEL_SPEED);
        rightDong.setPower(INTAKE_WHEEL_SPEED);
    }

    public void outtake() {
        leftDong.setPower(-INTAKE_WHEEL_SPEED);
        rightDong.setPower(-INTAKE_WHEEL_SPEED);
    }

    public void idle() {
        leftDong.setPower(0);
        rightDong.setPower(0);
    }
}
