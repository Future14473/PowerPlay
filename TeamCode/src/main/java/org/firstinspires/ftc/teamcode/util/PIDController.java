package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDController {
    double kp;
    double ki;
    double kd;

    double integralSum;
    private double lastError = 0;

    ElapsedTime timer = new ElapsedTime();

    public PIDController(double kp, double ki, double kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
    }

    public double update(double reference, double state) {
        double error = reference - state;
        integralSum += error * timer.seconds();
        double derivative = (error - lastError) / timer.seconds();
        lastError = error;

        timer.reset();

        double output = (error * kp) + (derivative * kd) + (integralSum * ki);
        return output;

    }
}