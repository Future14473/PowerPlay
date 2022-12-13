package org.firstinspires.ftc.teamcode.Hardware.Subsystems;

import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.highGoal;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.home;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.junction;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.lowGoal;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.midGoal;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.stack1;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.stack2;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.stack3;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.stack4;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.stack5;
import static org.firstinspires.ftc.teamcode.Constants.HardwareConstants.velocity;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class Slides {
    DcMotorEx leftSlide;
    DcMotorEx rightSlide;

    public static int incrementAmt = 50;


    public Slides(HardwareMap hardwareMap) {
        leftSlide = hardwareMap.get(DcMotorEx.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotorEx.class, "rightSlide");
        leftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setTargetPosition(0);
        rightSlide.setTargetPosition(0);
        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void resetEncoders() {
        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setTargetPosition(0);
        rightSlide.setTargetPosition(0);
        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void incrementDown() {
        leftSlide.setTargetPosition(leftSlide.getCurrentPosition() - incrementAmt);
        rightSlide.setTargetPosition(leftSlide.getCurrentPosition() - incrementAmt);
        leftSlide.setVelocity(velocity);
        rightSlide.setVelocity(velocity);
    }

    public void EMERGENCY() {
        leftSlide.setVelocity(2000);
        rightSlide.setVelocity(2000);
    }

    public void incrementUp() {
        leftSlide.setTargetPosition(leftSlide.getCurrentPosition() + incrementAmt);
        rightSlide.setTargetPosition(leftSlide.getCurrentPosition() + incrementAmt);
        leftSlide.setVelocity(velocity);
        rightSlide.setVelocity(velocity);
    }

    public int getHeight() {
        return leftSlide.getCurrentPosition();
    }

    public boolean isBusy() {
        return Math.abs(leftSlide.getCurrentPosition() - leftSlide.getTargetPosition()) < 30;
    }

    public void extendHigh() {
        leftSlide.setTargetPosition(highGoal);
        rightSlide.setTargetPosition(highGoal);
        leftSlide.setVelocity(velocity);
        rightSlide.setVelocity(velocity);
    }

    public void extendMid() {
        leftSlide.setTargetPosition(midGoal);
        rightSlide.setTargetPosition(midGoal);
        leftSlide.setVelocity(velocity);
        rightSlide.setVelocity(velocity);
    }


    public void extendLow() {
        leftSlide.setTargetPosition(lowGoal);
        rightSlide.setTargetPosition(lowGoal);
        leftSlide.setVelocity(velocity);
        rightSlide.setVelocity(velocity);
    }

    public void extendJunction() {
        leftSlide.setTargetPosition(junction);
        rightSlide.setTargetPosition(junction);
        leftSlide.setVelocity(velocity);
        rightSlide.setVelocity(velocity);
    }

    public void extendStack(int num) {
        switch (num) {
            case 1:
                leftSlide.setTargetPosition(stack1);
                rightSlide.setTargetPosition(stack1);
                leftSlide.setVelocity(velocity);
                rightSlide.setVelocity(velocity);
            case 2:
                leftSlide.setTargetPosition(stack2);
                rightSlide.setTargetPosition(stack2);
                leftSlide.setVelocity(velocity);
                rightSlide.setVelocity(velocity);
            case 3:
                leftSlide.setTargetPosition(stack3);
                rightSlide.setTargetPosition(stack3);
                leftSlide.setVelocity(velocity);
                rightSlide.setVelocity(velocity);
            case 4:
                leftSlide.setTargetPosition(stack4);
                rightSlide.setTargetPosition(stack4);
                leftSlide.setVelocity(velocity);
                rightSlide.setVelocity(velocity);
            case 5:
                leftSlide.setTargetPosition(stack5);
                rightSlide.setTargetPosition(stack5);
                leftSlide.setVelocity(velocity);
                rightSlide.setVelocity(velocity);
        }
    }

    public void retract() {
        leftSlide.setTargetPosition(home-50);
        rightSlide.setTargetPosition(home);
        leftSlide.setVelocity(velocity);
        rightSlide.setVelocity(velocity);
    }
}


