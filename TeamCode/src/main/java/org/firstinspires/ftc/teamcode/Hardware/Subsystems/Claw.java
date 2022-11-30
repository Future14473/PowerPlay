package org.firstinspires.ftc.teamcode.Hardware.Subsystems;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.CLOSE_POS_CLAW;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.OUT_POS_CLAW;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    Servo openWide;

    public Claw(HardwareMap hardwareMap) {
        openWide = hardwareMap.get(Servo.class, "openWide");
    }

    public double getPos() {
        return openWide.getPosition();
    }

    public void openWide() {
        openWide.setPosition(OUT_POS_CLAW);
    }

    public void shutUp() {
        openWide.setPosition(CLOSE_POS_CLAW);
    }
}
