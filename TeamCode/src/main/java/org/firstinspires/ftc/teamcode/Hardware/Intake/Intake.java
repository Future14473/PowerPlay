package org.firstinspires.ftc.teamcode.Hardware.Intake;

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
        v4b.autoIntake(stackNum);
        timer.safeDelay(1000);
        slides.retract();
        timer.safeDelay(1000);
        turret.setHome();
        timer.safeDelay(1000);
        claw.openWide(true);
        timer.safeDelay(1000);
    }

    public void autoIntake(Timer timer){
        claw.shutUp();
        timer.safeDelay(1000);
    }

    public void teleopIntakeReady(Timer timer) {
        v4b.setHome();
        timer.safeDelay(1000);
        slides.retract();
        timer.safeDelay(1000);
        turret.setHome();
        timer.safeDelay(1000);
        claw.openWide(true);
    }

    public void teleopIntake(Timer timer) {
        claw.shutUp();
    }
}
