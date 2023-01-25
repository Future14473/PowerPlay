package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.util.PIDController;

@Config
@TeleOp
public class PIDControllerSlides extends LinearOpMode {
    DcMotorEx slideMotor;

    //todo tune this
    PIDController controller = new PIDController(0, 0, 0);

    public static double target;
    public static double currPosition;

    public enum LiftState {
        LIFT_IDLE,
        LIFT_START,
        LIFT_EXTEND,
    }

    ;

    LiftState liftState = LiftState.LIFT_IDLE;

    @Override
    public void runOpMode() throws InterruptedException {
        slideMotor = hardwareMap.get(DcMotorEx.class, "rightSlide");
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        while (opModeIsActive()) {
            currPosition = slideMotor.getCurrentPosition();
            telemetry.addData("slide height", currPosition);
            telemetry.addData("lift state", liftState);
            switch (liftState) {

                case LIFT_IDLE:
                    slideMotor.setPower(0);
                    if (gamepad1.y) {
                        target = 500;
                        double command = controller.update(target, currPosition);
                        slideMotor.setPower(command);
                        liftState = LiftState.LIFT_START;
                    }
                    break;
                case LIFT_START:
                    if (slideMotor.getCurrentPosition() != target) {
                        double command = controller.update(target, currPosition);
                        slideMotor.setPower(command);
                    } else {
                        liftState = LiftState.LIFT_EXTEND;
                    }
                    break;
                case LIFT_EXTEND:
                    if (gamepad1.x) {
                        target = 0;
                        double command = controller.update(target, currPosition);
                        slideMotor.setPower(command);
                        liftState = LiftState.LIFT_START;
//                    } else { //run the slides after motor has reached target position (EXPERIMENTAL USE ONLY IF BRAKING DOES NOT WORK)
//                        double command = controller.update(target, currPosition);
//                        slideMotor.setPower(command);
                    }
                    break;
            }

            //cancel
            if (gamepad1.a) {
                liftState = LiftState.LIFT_IDLE;
            }

            telemetry.update();

        }

    }
}
