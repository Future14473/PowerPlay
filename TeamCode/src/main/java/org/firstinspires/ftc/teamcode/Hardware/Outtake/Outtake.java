package org.firstinspires.ftc.teamcode.Hardware.Outtake;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ClawCompliant;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;

public class Outtake {
    Slides slides;
    ClawCompliant claw;
    VirtualFourBar v4b;
    ServoTurret turret;

    public Outtake(HardwareMap hardwareMap) {
        slides = new Slides(hardwareMap);
        claw = new ClawCompliant(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        turret = new ServoTurret(hardwareMap);
    }

    public void outtake(Timer timer) {
        claw.openWide(false);
        timer.safeDelay(1000);
    }

    //TODO condense to one function (use low/mid/high parameters)
    public void outtakeReadyLow(Timer timer) {
        slides.extendLow();
        timer.safeDelay(1000);
        v4b.outHigh();
        timer.safeDelay(1000);
        turret.setOut();
        timer.safeDelay(1000);
    }

    public void outtakeReadyMid(Timer timer) {
        slides.extendLow();
        timer.safeDelay(1000);
        v4b.outHigh();
        timer.safeDelay(1000);
        turret.setOut();
        timer.safeDelay(1000);
    }
    public void outtakeReadyHigh(Timer timer) {
        slides.extendLow();
        timer.safeDelay(1000);
        v4b.outHigh();
        timer.safeDelay(1000);
        turret.setOut();
        timer.safeDelay(1000);
    }


}
