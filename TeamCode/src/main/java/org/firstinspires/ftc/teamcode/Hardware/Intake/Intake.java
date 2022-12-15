package org.firstinspires.ftc.teamcode.Hardware.Intake;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.SLIDES_HOME;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.TURRET_HOME;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.V4B_HOME;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ClawCompliant;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;

public class Intake {
    Slides slides;
    Claw claw;
    VirtualFourBar v4b;
    ServoTurret turret;

    public Intake(Slides slides, Claw claw, VirtualFourBar v4b, ServoTurret turret) {
        this.slides = slides;
        this.claw = claw;
        this.v4b = v4b;
        this.turret = turret;
    }
    // intake the cones on the stack
    //TODO change timings between each operation
    public void autoIntakeReady(int stackNum, Timer timer) {
        turret.setHome();
        timer.safeDelay(TURRET_HOME);
        v4b.autoIntake(stackNum);
        timer.safeDelay(V4B_HOME);
        slides.retract();
        timer.safeDelay(SLIDES_HOME);
        claw.openWide();
    }

    public void teleopIntake(Timer timer) {
        v4b.intake();
        timer.safeDelay(V4B_HOME);
        slides.retract();
        timer.safeDelay(SLIDES_HOME);
        turret.setHome();
        timer.safeDelay(TURRET_HOME);
        claw.openWide();
    }

    public void teleopIntakeReady() {
        slides.resetEncoders();
        claw.openWide();
        v4b.intake();
        turret.setHome();
    }

    public void intake() {
        claw.shutUp();
    }

}
