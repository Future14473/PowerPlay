package org.firstinspires.ftc.teamcode.util;

public class MotionProfile {

    double maxAccel;
    double maxVel;

    public MotionProfile(double maxAccel, double maxVel) {
        this.maxAccel = maxAccel;
        this.maxVel = maxVel;
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
