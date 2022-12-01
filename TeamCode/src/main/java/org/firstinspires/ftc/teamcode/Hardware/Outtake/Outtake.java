package org.firstinspires.ftc.teamcode.Hardware.Outtake;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.SLIDES_OUT;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.V4B_HOME;

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
    }

    //TODO condense to one function (use low/mid/high parameters)
    public void outtakeReadyLow(Timer timer) {
        slides.extendLow();
        timer.safeDelay(SLIDES_OUT);
        v4b.outLow();
        timer.safeDelay(V4B_HOME);
        turret.setOut();
    }

    public void outtakeReadyMid(Timer timer) {
        slides.extendMid();
        timer.safeDelay(SLIDES_OUT);
        v4b.outMid();
        timer.safeDelay(V4B_HOME);
        turret.setOut();
    }

    public void outtakeReadyHigh(Timer timer) {
        slides.extendHigh();
        timer.safeDelay(SLIDES_OUT);
        v4b.outHigh();
        timer.safeDelay(V4B_HOME);
        turret.setOut();
    }
}
