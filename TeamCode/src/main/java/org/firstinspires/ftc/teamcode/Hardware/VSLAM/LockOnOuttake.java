package org.firstinspires.ftc.teamcode.Hardware.VSLAM;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.MAX_ROTATION_DEGREES;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.OUT_POS_TURRET;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;

public class LockOnOuttake {
    public static double MIN_DIST = 9.0;
    public static double xOffset = 0.0;
    public static double yOffset = 0.0;
    double distancePub;
    double trim = 0;


    private enum Direction {
        TOP_RIGHT,
        BOTTOM_RIGHT,
        TOP_LEFT,
        BOTTOM_LEFT
    }

    private Direction direction;

    public LockOnOuttake() {
    }


    //FIELD COORDINATES

    /*
     *               | +x
     *               |
     *       +y      |
     *  BLUE --------|--------  RED
     *               |
     *               |
     *               |
     *
     *             FRONT
     * */

    //pole coordinates
    public static Pose2d[] poles = {
            new Pose2d(24, 24, Math.toRadians(0)),
            new Pose2d(24, 0, Math.toRadians(0)),
            new Pose2d(24, -24, Math.toRadians(0)),
            new Pose2d(0, -24, Math.toRadians(0)),
            new Pose2d(-24, -24, Math.toRadians(0)),
            new Pose2d(-24, 0, Math.toRadians(0)),
            new Pose2d(-24, 24, Math.toRadians(0)),
            new Pose2d(0, 24, Math.toRadians(0)),
            new Pose2d(48, 24, Math.toRadians(0)),
            new Pose2d(48, -24, Math.toRadians(0)),
            new Pose2d(24, -48, Math.toRadians(0)),
            new Pose2d(-24, -48, Math.toRadians(0)),
            new Pose2d(-48, -24, Math.toRadians(0)),
            new Pose2d(-48, 24, Math.toRadians(0)),
            new Pose2d(-24, 48, Math.toRadians(0)),
            new Pose2d(24, 48, Math.toRadians(0))
    };


    @Deprecated
    public double lockOnOld(Pose2d rPos) {
        double angle = 0;


        // getting the angle from the closest pole
        for (Pose2d p : poles) {
            double distance = getMagnitude(rPos, p);
            if (distance <= MIN_DIST) {
                distancePub = distance;
                angle = getAngle(rPos, p);
                return angle;
            } else {
                distancePub = 0;
            }
        }


        return angle;

    }

    /**
     * Locks on to the nearest pole to the robot
     *
     * @param rPos Pose2d object representing position of robot
     */

    @NonNull
    public double lockOn(Pose2d rPos) {
        double angle = 0;
        double moveAngle = 0;

        // getting the angle from the closest pole
        for (Pose2d p : poles) {
            double distance = getMagnitude(rPos, p);
            if (distance <= MIN_DIST) {
                distancePub = distance;
                angle = getAngle(rPos, p);
                break;
            } else {
                distancePub = 0;
            }
        }

        // calculate angle from robot to pole and convert to fractional degrees
        if (angle == 0) {
            return OUT_POS_TURRET;
        } else {

            //todo test on robot after testing angle
//            if (rPos.getHeading() > 0) {
//                trim = OUT_POS_TURRET + (rPos.getHeading() / MAX_ROTATION_DEGREES);
////            } else if (rPos.getHeading() > 85) {
////                trim = (OUT_POS_TURRET + ((90 - rPos.getHeading()) / MAX_ROTATION_DEGREES));
//            } else if (rPos.getHeading() < 0 ) {
//                trim = OUT_POS_TURRET - (Math.abs(rPos.getHeading()) / MAX_ROTATION_DEGREES);
////            } else if (rPos.getHeading() < -85) {
////                trim = OUT_POS_TURRET - ((90 + rPos.getHeading()) / MAX_ROTATION_DEGREES);
//            }

            trim = OUT_POS_TURRET;

            if (angle > 0) {
                moveAngle = trim - Math.abs((angle / MAX_ROTATION_DEGREES));
            } else if (angle < 0) {
                moveAngle = trim + ((Math.abs(angle)) / MAX_ROTATION_DEGREES);
            }

            return moveAngle;
        }
    }


    private double getMagnitude(Pose2d robotPosition, Pose2d poleLoc) {
        double rX = robotPosition.getX() + xOffset;
        double rY = robotPosition.getY() + yOffset;

        double pX = poleLoc.getX();
        double pY = poleLoc.getY();

        return Math.sqrt(Math.pow(rX - pX, 2) + Math.pow(rY - pY, 2));

    }


    // THE ISSUE IS HERE WITH ARCTAN
    private double getAngle(Pose2d robotPosition, Pose2d poleLoc) {
        double rX = robotPosition.getX() + xOffset;
        double rY = robotPosition.getY() + yOffset;

        double pX = poleLoc.getX();
        double pY = poleLoc.getY();

        double angle = Math.toDegrees(Math.atan((rY - pY) / (rX - pX)));

        if (Math.abs(angle) > 90) {
            if (angle < 0) {
                return -(angle + 90);
            } else if (angle > 0) {
                return -(angle - 90);
            }
        } else return angle;

        return 0;
    }

    public double getDistance() {
        return distancePub;
    }

    public double getTrim() {
        return trim;
    }


}
