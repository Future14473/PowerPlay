package org.firstinspires.ftc.teamcode.Hardware.Subsystems;

import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.AUTO_1_INTAKE;
import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.AUTO_2_INTAKE;
import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.AUTO_3_INTAKE;
import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.HIGH_V4B;
import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.HOME_POS_V4B;
import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.LOW_V4B;
import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.MAX_ROTATION_DEGREES;
import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.MID_V4B;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class VirtualFourBar {
    Servo leftv4b;
    Servo rightv4b;

    public VirtualFourBar(@NonNull HardwareMap hardwareMap) {
        leftv4b = hardwareMap.get(Servo.class, "leftv4b");
        rightv4b = hardwareMap.get(Servo.class, "rightv4b");

    }


    //USE FOR DEBUG ONLY
    public void moveAngle(double angle) {
        leftv4b.setPosition(angle / MAX_ROTATION_DEGREES);
        rightv4b.setPosition(angle / MAX_ROTATION_DEGREES);
    }

    /**
     * @param stackNum the current cone in the stack to intake
     * AUTO ONLY
     */
    public void autoIntake(int stackNum) {
        switch (stackNum) {
            case 1:
                leftv4b.setPosition(AUTO_1_INTAKE);
                rightv4b.setPosition(AUTO_1_INTAKE);
            case 2:
                leftv4b.setPosition(AUTO_2_INTAKE);
                rightv4b.setPosition(AUTO_2_INTAKE);
            case 3:
                leftv4b.setPosition(AUTO_3_INTAKE);
                rightv4b.setPosition(AUTO_3_INTAKE);
        }
    }

    public void intake() {
        leftv4b.setPosition(HOME_POS_V4B);
        rightv4b.setPosition(HOME_POS_V4B);
    }

    public void outHigh() {
        leftv4b.setPosition(HIGH_V4B);
        rightv4b.setPosition(HIGH_V4B);
    }

    public void outMid() {
        leftv4b.setPosition(MID_V4B);
        rightv4b.setPosition(MID_V4B);
    }

    public void outLow() {
        leftv4b.setPosition(LOW_V4B);
        rightv4b.setPosition(LOW_V4B);
    }

    public void setHome() {
        leftv4b.setPosition(HOME_POS_V4B);
        rightv4b.setPosition(HOME_POS_V4B);
    }

    public void setCustom(double value) {
        leftv4b.setPosition(value);
        rightv4b.setPosition(value);
    }

}
