package org.firstinspires.ftc.teamcode.Hardware.Subsystems;

import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.HOME_POS_TURRET;
import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.MANUAL_INCREMENT_STEP;
import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.MAX_ROTATION_DEGREES;
import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.OUT_POS_TURRET;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class ServoTurret {

    Servo servoTurret;

    long start = System.currentTimeMillis();

    public ServoTurret(HardwareMap hardwareMap) {
        servoTurret = hardwareMap.get(Servo.class, "servoTurret");
    }

    public void setHome() {
        servoTurret.setPosition(HOME_POS_TURRET);
    }


    public void setAngle(double angle) {
        servoTurret.setPosition(angle / MAX_ROTATION_DEGREES);
    }

    public void setOut() {
        servoTurret.setPosition(OUT_POS_TURRET);
    }

    public void setFractional(double angle) {
        servoTurret.setPosition(angle);
    }

    public double getCurrPos() {
        return servoTurret.getPosition();
    }

    public void incrementUp() {
        servoTurret.setPosition(servoTurret.getPosition() + (MANUAL_INCREMENT_STEP));
    }

    public void incrementDown() {
        servoTurret.setPosition(servoTurret.getPosition() - (MANUAL_INCREMENT_STEP ));
    }
}