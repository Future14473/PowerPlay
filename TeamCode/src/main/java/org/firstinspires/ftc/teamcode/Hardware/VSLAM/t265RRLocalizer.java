package org.firstinspires.ftc.teamcode.Hardware.VSLAM;


import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Transform2d;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.ChassisSpeeds;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.spartronics4915.lib.T265Camera;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a Road Runner localizer that uses the Intel T265 Realsense
 */
@Config
public class t265RRLocalizer implements Localizer {

    private Pose2d poseOffset;
    private static Pose2d mPoseEstimate = new Pose2d();
    private Pose2d rawPose;
    private T265Camera.CameraUpdate up;

    public static T265Camera slamra;

    // if the camera is offcenter
    public static double slamraOffsetX = 0;
    public static double slamraOffsetY = 0;
    public static double angleModifier = 0;
    final int robotRadius = 9;

    //if the camera is in the center
    public static boolean makeCameraCenter = false;

    private static T265Camera.PoseConfidence poseConfidence;


    public t265RRLocalizer(HardwareMap hardwareMap) {
        poseOffset = new Pose2d();
        mPoseEstimate = new Pose2d();
        rawPose = new Pose2d();

        if (slamra == null) {
            slamra = new T265Camera(new Transform2d(new Translation2d(0, 0), new Rotation2d(0)), 0, hardwareMap.appContext);
            setPoseEstimate(new Pose2d(0, 0, 0));
        }
        slamra.start();
    }

    @NotNull
    @Override
    public Pose2d getPoseEstimate() {

        if (up != null) {

            Translation2d oldPose = up.pose.getTranslation();
            Rotation2d oldRot = up.pose.getRotation();

            //The T265's unit of measurement is meters.  dividing it by .0254 converts meters to inches.
            rawPose = new Pose2d(oldPose.getX() / .0254, oldPose.getY() / .0254, oldRot.getDegrees()); //raw pos
            mPoseEstimate = rawPose.plus(poseOffset); //offsets the pose to be what the pose estimate is;

        }

        if (makeCameraCenter) return mPoseEstimate;
        else {
            return adjustPosbyCameraPos();
        }
    }


    @Override
    public void setPoseEstimate(@NotNull Pose2d pose2d) {
        pose2d = new Pose2d(pose2d.getX(), pose2d.getY(), pose2d.getHeading());

        poseOffset = pose2d.minus(rawPose);
        poseOffset = new Pose2d(poseOffset.getX(), poseOffset.getY(), Math.toRadians(0));
    }

    public static T265Camera.PoseConfidence getConfidence() {
        return poseConfidence;
    }

    public void stop() {
        slamra.stop();
    }

    /**
     * @return the heading of the robot (in radians)
     */
    public static double getHeading() {
        return norma(mPoseEstimate.getHeading() - angleModifier);
    }


    @Override
    public void update() {
        up = slamra.getLastReceivedCameraUpdate();
        poseConfidence = up.confidence;
    }


    @Nullable
    @Override
    public Pose2d getPoseVelocity() {
        //variable up is updated in update()
        ChassisSpeeds velocity = up.velocity;
        return new Pose2d(velocity.vxMetersPerSecond / .0254, velocity.vyMetersPerSecond / .0254, velocity.omegaRadiansPerSecond);
    }

    public TelemetryPacket fieldPosUpdate() {
        final int robotRadius = 9; // inches

        TelemetryPacket packet = new TelemetryPacket();
        Canvas field = packet.fieldOverlay();

        T265Camera.CameraUpdate up = slamra.getLastReceivedCameraUpdate();
        if (up == null) return null;

        // We divide by 0.0254 to convert meters to inches
        Translation2d translation = new Translation2d(up.pose.getTranslation().getX() / 0.0254, up.pose.getTranslation().getY() / 0.0254);
        Rotation2d rotation = up.pose.getRotation();

        field.strokeCircle(translation.getX(), translation.getY(), robotRadius);
        double arrowX = rotation.getCos() * robotRadius, arrowY = rotation.getSin() * robotRadius;
        double x1 = translation.getX() + arrowX / 2, y1 = translation.getY() + arrowY / 2;
        double x2 = translation.getX() + arrowX, y2 = translation.getY() + arrowY;
        field.strokeLine(x1, y1, x2, y2);

        return packet;
    }


    /**
     * @param angle angle in radians
     * @return normiazled angle between ranges 0 to 2Pi
     */
    private double norm(double angle) {
        while (angle > Math.toRadians(360)) angle -= Math.toRadians(360);
        while (angle <= 0) angle += Math.toRadians(360);
        return angle;
    }

    private static double norma(double angle) {
        while (angle > Math.toRadians(360)) angle -= Math.toRadians(360);
        while (angle <= 0) angle += Math.toRadians(360);
        return angle;
    }

    private Pose2d adjustPosbyCameraPos() {
        double dist = Math.hypot(slamraOffsetX, slamraOffsetY); //distance camera is from center
        double angle = Math.atan2(slamraOffsetY, slamraOffsetX);
        double cameraAngle = mPoseEstimate.getHeading() - angle;
        double detlaX = dist * cos(cameraAngle);
        double detlaY = dist * sin(cameraAngle);
        return mPoseEstimate.minus(new Pose2d(detlaX, detlaY));
    }

    // FTC Dashboard doesnt let you send telemetry and packets at the same time :(
    @Deprecated
    public void fieldPosUpdate(FtcDashboard dashboard) {
        final int robotRadius = 9; // inches

        TelemetryPacket packet = new TelemetryPacket();
        Canvas field = packet.fieldOverlay();


        field.strokeCircle(mPoseEstimate.getX(), mPoseEstimate.getY(), robotRadius);
        double arrowX = Math.cos(mPoseEstimate.getHeading()) * robotRadius, arrowY = Math.sin(mPoseEstimate.getHeading()) * robotRadius;
        double x1 = mPoseEstimate.getX() + arrowX / 2, y1 = mPoseEstimate.getY() + arrowY / 2;
        double x2 = mPoseEstimate.getX() + arrowX, y2 = mPoseEstimate.getY() + arrowY;
        field.strokeLine(x1, y1, x2, y2);

        dashboard.sendTelemetryPacket(packet);
    }


}