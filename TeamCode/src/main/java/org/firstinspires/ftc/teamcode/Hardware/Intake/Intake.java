package org.firstinspires.ftc.teamcode.Hardware.Intake;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.SLIDES_HOME;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.TURRET_HOME;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.V4B_HOME;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ClawCompliant;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.ServoTurret;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Subsystems.VirtualFourBar;
import org.firstinspires.ftc.teamcode.Hardware.util.Timer;

public class Intake {
    Slides slides;
    ClawCompliant claw;
    VirtualFourBar v4b;
    ServoTurret turret;

    public Intake(HardwareMap hardwareMap) {
        slides = new Slides(hardwareMap);
        claw = new ClawCompliant(hardwareMap);
        v4b = new VirtualFourBar(hardwareMap);
        turret = new ServoTurret(hardwareMap);
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
        claw.openWide(true);
    }

    public void teleopIntakeReady(Timer timer) {
        turret.setHome();
        timer.safeDelay(TURRET_HOME);
        v4b.setHome();
        timer.safeDelay(V4B_HOME);
        slides.retract();
        timer.safeDelay(SLIDES_HOME);
        claw.openWide(true);
    }

    public void intake(Timer timer) {
        claw.shutUp();
    }
}
