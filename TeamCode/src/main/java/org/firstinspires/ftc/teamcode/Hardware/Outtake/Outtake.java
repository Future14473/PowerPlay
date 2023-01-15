package org.firstinspires.ftc.teamcode.Hardware.Outtake;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.OUT_POS_TURRET;
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

    public void outtakeReadyHigh(Timer timer) {
        slides.extendHigh();
        timer.safeDelay(V4B_OUT);
        v4b.outtake();
        timer.safeDelay(TURRET_OUT);
        turret.setOut();

    }

    public void outtakeAuto(Timer timer) {
        slides.setCustom(1300);
        timer.safeDelay(130);
        this.outtake();
        slides.setCustom(1800);
    }

    public void outtakeTeleOp(Timer timer) {
        if (slides.getHeight() > 400) {
            int initPos = slides.getHeight();
            slides.setCustom(initPos - slidesDownToOuttake);
            timer.safeDelay(130);
            this.outtake();
            slides.setCustom(initPos);
        }
    }

    public void outtakeReadyJunction() {
        slides.extendJunction();
    }
}
