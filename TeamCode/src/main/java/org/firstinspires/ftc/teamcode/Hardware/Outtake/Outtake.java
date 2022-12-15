package org.firstinspires.ftc.teamcode.Hardware.Outtake;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.SLIDES_OUT;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.TURRET_OUT;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.V4B_HOME;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ClawCompliant;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;

public class Outtake {
    Slides slides;
    Claw claw;
    VirtualFourBar v4b;
    ServoTurret turret;

    public Outtake(Slides slides, Claw claw, VirtualFourBar v4b, ServoTurret turret) {
        this.slides = slides;
        this.claw = claw;
        this.v4b = v4b;
        this.turret = turret;
    }

    public void outtake() {
        claw.openWide();
    }

    //TODO condense to one function (use low/mid/high parameters)
    public void outtakeReadyLow(Timer timer) {
        slides.extendLow();
        timer.safeDelay(SLIDES_OUT);
        turret.setOut();
        timer.safeDelay(TURRET_OUT);
        v4b.outtake();
        timer.safeDelay(V4B_HOME);
        turret.setOut();
    }

    public void outtakeReadyMid(Timer timer) {
        slides.extendMid();
        timer.safeDelay(SLIDES_OUT);
        turret.setOut();
        timer.safeDelay(TURRET_OUT);
        v4b.outtake();
        timer.safeDelay(V4B_HOME);
        turret.setOut();
    }

    public void outtakeReadyHigh(Timer timer) {
        slides.extendHigh();
        timer.safeDelay(SLIDES_OUT);
        turret.setOut();
        timer.safeDelay(TURRET_OUT);
        v4b.outtake();
        timer.safeDelay(V4B_HOME);
    }

    public void outtakeReadyJunction() {
        slides.extendJunction();
    }
}
