package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MotionProfile {
    Telemetry telemetry;

    double maxAccel;
    double maxVel;
    double target;
    double linearAccelerationMultiplier;


    public MotionProfile(Telemetry t, double maxAccel, double maxVel, double linearAccelerationMultiplier) {
        this.maxAccel = maxAccel;
        this.maxVel = maxVel;
        target = 0;
        this.linearAccelerationMultiplier = linearAccelerationMultiplier;

        telemetry = t;
    }

    public double calculateEntiredt(double distance) {
        double acceleration_dt = maxVel / maxAccel;

        double halfway_distance = distance / 2;

        double acceleration_distance = 0.5 * maxAccel * Math.pow(acceleration_dt, 2);
        if (acceleration_distance > halfway_distance) {
            acceleration_dt = Math.sqrt(halfway_distance / (0.5 * maxAccel));
        }

        acceleration_distance = 0.5 * maxAccel * Math.pow(acceleration_dt, 2);
        maxVel = maxAccel * acceleration_dt;

        double deacceleration_dt = acceleration_dt;

        double cruise_distance = distance - 2 * acceleration_distance;
        double cruise_dt = cruise_distance / maxVel;

        double entire_dt = acceleration_dt + cruise_dt + deacceleration_dt;

        return entire_dt;
    }

    // time-based motion profile
    // trapezoidal motion profile - symmetry
    public double generateMotionProfile2(double distance, double curr_pos, double entire_dt, double current_dt) {
        // time it takes to accelerate (v = v0 + at)
        double accelTime = maxVel / maxAccel;

        double accelDistance = 0.5 * maxAccel + Math.pow(accelTime, 2);
        double cruise_distance = distance - 2 * accelDistance;
        double cruise_dt = cruise_distance / maxVel;
        double deacceleration_time = accelTime + cruise_dt;
        telemetry.addData("accelDist",accelDistance);
        //accelerate to max velocity
        if (curr_pos < accelDistance) {
            return curr_pos + (linearAccelerationMultiplier * curr_pos);
        }

        // maintain max velocity



        //decelerate


        return 0;
    }

    public double generateMotionProfile(double distance, double entire_dt, double current_dt) {
        double acceleration_dt = maxVel / maxAccel;

        double halfway_distance = distance / 2;

        double acceleration_distance = 0.5 * maxAccel * Math.pow(acceleration_dt, 2);
        if (acceleration_distance > halfway_distance) {
            acceleration_dt = Math.sqrt(halfway_distance / (0.5 * maxAccel));
        }

        acceleration_distance = 0.5 * maxAccel * Math.pow(acceleration_dt, 2);
        maxVel = maxAccel * acceleration_dt;


        double cruise_distance = distance - 2 * acceleration_distance;
        double cruise_dt = cruise_distance / maxVel;
        double deacceleration_time = acceleration_dt + cruise_dt;

        telemetry.addData("acceleration_dt", acceleration_dt);
        telemetry.addData("deacceleration time", deacceleration_time);


        if (current_dt > entire_dt) {
            return distance;
        }

        if (current_dt < acceleration_dt) {
            return 0.5 * maxAccel * Math.pow(current_dt, 2);
        } else if (current_dt < deacceleration_time) {
            acceleration_distance = 0.5 * maxAccel * Math.pow(acceleration_dt, 2);
            double cruise_current_dt = current_dt - acceleration_dt;
            return acceleration_distance + maxVel * cruise_current_dt;
        } else {
            acceleration_distance = 0.5 * maxAccel * Math.pow(acceleration_dt, 2);
            cruise_distance = maxVel * cruise_dt;
            deacceleration_time = current_dt - deacceleration_time;

            return acceleration_distance + cruise_distance + maxVel * deacceleration_time - 0.5 * maxAccel * Math.pow(deacceleration_time, 2);

        }
    }
}
