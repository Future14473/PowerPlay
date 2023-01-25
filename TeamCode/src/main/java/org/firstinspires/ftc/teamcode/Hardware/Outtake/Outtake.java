package org.firstinspires.ftc.teamcode.Hardware.Outtake;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.OUTTAKE;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.OUT_POS_TURRET;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.OUT_V4B_2;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.SLIDES_HOME;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.SLIDES_OUT;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.V4B_OUT;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.TURRET_OUT;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.V4B_HOME;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.home;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.slidesDownToOuttake;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
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
        claw.shutUp();
    }

    //TODO condense to one function (use low/mid/high parameters)
    public void outtakeReadyLow(Timer timer) {
        slides.extendLow();
    }

    public void outtakeReadyMid(Timer timer) {
        slides.extendMid();
        timer.safeDelay(SLIDES_OUT);
        v4b.outtake();
        timer.safeDelay(TURRET_OUT);
        turret.setOut();
    }
    public void outtakeReadyMid2(Timer timer) {
        slides.extendMid();
        timer.safeDelay(SLIDES_OUT);
        v4b.setCustom(OUT_V4B_2);
        timer.safeDelay(TURRET_OUT);
        turret.setOut();
    }

    public void outtakeReadyHigh(Timer timer) {
        slides.extendHigh();
        timer.safeDelay(V4B_OUT);
        v4b.outtake();
        timer.safeDelay(TURRET_OUT);
        turret.setOut();

    }

    public void outtakeReadyHigh2(Timer timer) {
        slides.extendHigh();
        timer.safeDelay(V4B_OUT);
        v4b.setCustom(OUT_V4B_2);
        timer.safeDelay(TURRET_OUT);
        turret.setOut();

    }

    public void outtakeAuto(Timer timer) {

    }

    public void outtakeTeleOp(Timer timer) {
        if (slides.getHeight() > 400) {
            v4b.setCustom(OUTTAKE);
            timer.safeDelay(100);
            this.outtake();
            timer.safeDelay(100);
            v4b.intake();
            timer.safeDelay(200);
            turret.setHome();
        }
    }

    public void outtakeTeleOp2(Timer timer) {
        if (slides.getHeight() > 400) {
            v4b.setCustom(OUTTAKE);
            timer.safeDelay(100);
            this.outtake();
            timer.safeDelay(100);
            v4b.intake();
            timer.safeDelay(200);
            turret.setHome();
        }
    }

    public void outtakeReadyJunction() {
        slides.extendJunction();
    }
}
