package org.firstinspires.ftc.teamcode.Hardware.Subsystems;

import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.CLOSE_POS_CLAW;
import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.COMPLIANT_INTAKE_SPEED;
import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.COMPLIANT_OUT_SPEED;
import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.HOME_POS_COMPLIANT;
import static org.firstinspires.ftc.teamcode.Hardware.Subsystems.Constants.OUT_POS_CLAW;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ClawCompliant {
    Servo compliantLeft;
    Servo compliantRight;
    Servo openWide;

    public ClawCompliant(HardwareMap hardwareMap) {

        //continuous rotation servos
        compliantLeft = hardwareMap.get(Servo.class, "compliantLeft");
        compliantRight = hardwareMap.get(Servo.class, "compliantRight");
        //todo check if this is right or left servo to change
        compliantRight.setDirection(Servo.Direction.REVERSE);

        //open & close servio
        openWide = hardwareMap.get(Servo.class, "openWide");
    }

    // direction TRUE: intaking; direction FALSE: out;
    public void openWide(boolean direction) {
        compliantLeft.setPosition(direction ? COMPLIANT_INTAKE_SPEED : COMPLIANT_OUT_SPEED);
        compliantRight.setPosition(direction ? COMPLIANT_INTAKE_SPEED : COMPLIANT_OUT_SPEED);
        openWide.setPosition(OUT_POS_CLAW);
    }


    public void shutUp() {
        compliantLeft.setPosition(HOME_POS_COMPLIANT);
        compliantRight.setPosition(HOME_POS_COMPLIANT);
        openWide.setPosition(CLOSE_POS_CLAW);
    }
}
