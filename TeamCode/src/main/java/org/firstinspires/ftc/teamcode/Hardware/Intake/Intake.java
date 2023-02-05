package org.firstinspires.ftc.teamcode.Hardware.Intake;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.SLIDES_HOME;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.home;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;

public class Intake {
    Slides slides;
    Claw claw;
    VirtualFourBar v4b;

    public Intake(Slides slides, Claw claw, VirtualFourBar v4b) {
        this.slides = slides;
        this.claw = claw;
        this.v4b = v4b;
    }
    // intake the cones on the stack
    //TODO change timings between each operation
    public void autoIntakeReady(int stackNum, Timer timer) {
        claw.openWide();
        v4b.intake();
        timer.safeDelay(200);

        timer.safeDelay(100);
        slides.extendStack(stackNum);
        timer.safeDelay(SLIDES_HOME);
        timer.safeDelay(100);
        claw.shutUp();


    }

    public void teleopIntake(Timer timer) {
        if (slides.getHeight() != home) {
            claw.openWide();
            v4b.intake();
            timer.safeDelay(200);

            timer.safeDelay(100);
            slides.retract();
            timer.safeDelay(SLIDES_HOME);
            claw.shutUp();
        }
    }

    public void teleopIntakeReady() {
        slides.resetEncoders();
        v4b.intake();
        claw.shutUp();
    }

    public void intake() {
        claw.openWide();
    }

}
