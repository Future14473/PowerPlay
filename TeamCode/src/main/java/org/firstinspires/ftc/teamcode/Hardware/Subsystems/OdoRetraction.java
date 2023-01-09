package org.firstinspires.ftc.teamcode.Hardware.Subsystems;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.ODO_DOWN;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.ODO_UP;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class OdoRetraction {
    Servo retraction;

    public OdoRetraction(HardwareMap hardwareMap) {
        retraction = hardwareMap.get(Servo.class, "retraction");
    }

    public void retract() {
        retraction.setPosition(ODO_UP);
    }

    public void down() {
        retraction.setPosition(ODO_DOWN);
    }

}
