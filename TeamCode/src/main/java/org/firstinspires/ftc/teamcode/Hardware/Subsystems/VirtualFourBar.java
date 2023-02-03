package org.firstinspires.ftc.teamcode.Hardware.Subsystems;


import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.HOME_POS_V4B;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.MAX_ROTATION_DEGREES;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.OUT_V4B;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class VirtualFourBar {
    Servo leftv4b;
    Servo rightv4b;

    public VirtualFourBar(@NonNull HardwareMap hardwareMap) {
        leftv4b = hardwareMap.get(Servo.class, "leftv4b");
        rightv4b = hardwareMap.get(Servo.class, "rightv4b");
        leftv4b.setDirection(Servo.Direction.REVERSE);
        rightv4b.setDirection(Servo.Direction.REVERSE);
    }


    //USE FOR DEBUG ONLY
    public void moveAngle(double angle) {
        leftv4b.setPosition(angle / MAX_ROTATION_DEGREES);
        rightv4b.setPosition(angle / MAX_ROTATION_DEGREES);
    }


    public void intake() {
        leftv4b.setPosition(HOME_POS_V4B);
        rightv4b.setPosition(HOME_POS_V4B);
    }

   public void outtake() {
        leftv4b.setPosition(OUT_V4B);
        rightv4b.setPosition(OUT_V4B);
   }


//  debug
    public void setCustom(double value) {
        leftv4b.setPosition(value);
        rightv4b.setPosition(value);
    }

}
