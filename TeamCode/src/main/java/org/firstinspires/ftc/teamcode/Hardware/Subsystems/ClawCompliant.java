package org.firstinspires.ftc.teamcode.Hardware.Subsystems;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.CLOSE_POS_CLAW;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.COMPLIANT_INTAKE_SPEED;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.COMPLIANT_OUT_SPEED;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.HOME_POS_COMPLIANT;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.OUT_POS_CLAW;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ClawCompliant {
    Servo compliant;
    Servo openWide;

    public ClawCompliant(HardwareMap hardwareMap) {

        compliant = hardwareMap.get(Servo.class, "compliant");

        openWide = hardwareMap.get(Servo.class, "openWide");
    }

    /**
     * @param direction true: intaking; false: out;
     */

    //todo worth or not to move complaint while outtaking
    public void openWide(boolean direction) {
        compliant.setPosition(direction ? COMPLIANT_INTAKE_SPEED : COMPLIANT_OUT_SPEED);
        openWide.setPosition(OUT_POS_CLAW);
    }


    public void shutUp() {
        compliant.setPosition(HOME_POS_COMPLIANT);
        openWide.setPosition(CLOSE_POS_CLAW);
    }

    public void openNoComplaint() {
        openWide.setPosition(OUT_POS_CLAW);
    }
}
