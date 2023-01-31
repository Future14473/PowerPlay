package org.firstinspires.ftc.teamcode.Opmodes.TeleOp.Testing;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.MotionProfile;
import org.firstinspires.ftc.teamcode.util.PIDController;

@Config
@TeleOp
public class PIDControllerSlides extends LinearOpMode {
    DcMotorEx slideMotor;

    private FtcDashboard dashboard = FtcDashboard.getInstance();

    //todo tune this
    PIDController controller = new PIDController(0.023, 0.000098, 0.0008);

    public static double target;
    public static double currPosition;
    public static double currVelocity;

    public enum LiftState {
        LIFT_IDLE,
        LIFT_START,
        LIFT_EXTEND,
        LIFT_RETRACT
    }

    LiftState liftState = LiftState.LIFT_IDLE;
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        slideMotor = hardwareMap.get(DcMotorEx.class, "m");
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Telemetry telemetry = new MultipleTelemetry(this.telemetry, dashboard.getTelemetry());

        waitForStart();

        while (opModeIsActive()) {
            currPosition = slideMotor.getCurrentPosition();
            currVelocity = slideMotor.getVelocity();
            telemetry.addData("slide height", currPosition);
            telemetry.addData("lift state", liftState);
            telemetry.addData("target", target);

            switch (liftState) {

                case LIFT_IDLE:
                    slideMotor.setPower(0);
                    if (gamepad1.y) {
                        target = 2000;
                        liftState = LiftState.LIFT_START;
                    }
                    break;
                case LIFT_START:
                    MotionProfile goToMax = new MotionProfile(telemetry,75,100);
                    double entireDT = goToMax.calculateEntiredt(target);
                    double command = goToMax.generateMotionProfile(target, entireDT, timer.seconds());
                    double power = controller.update(command, currPosition);
                    telemetry.addData("power", power);
                    telemetry.addData("motion profile", command);
                    telemetry.addData("entireDT",entireDT);
                    telemetry.addData("currentDT", timer.seconds());
                    slideMotor.setPower(power);

                    break;
                case LIFT_EXTEND:
                    if (gamepad1.a) {
                        target = 0;
                        liftState = LiftState.LIFT_RETRACT;
                    } else { //run the slides after motor has reached target position )
                        double command2 = controller.update(target, currPosition);
                        slideMotor.setPower(command2);
                    }
                    break;
                case LIFT_RETRACT:
                    if (slideMotor.getCurrentPosition() != target) {
//                        double command3 = generateMotionProfile(30.0, 30.0, target, timer.seconds());
                        double power1 = controller.update(target, currPosition);
                        slideMotor.setPower(power1);
                    } else {
                        liftState = LiftState.LIFT_IDLE;
                    }
            }

            //cancel
            if (gamepad1.x) {
                liftState = LiftState.LIFT_IDLE;
            }

            telemetry.update();

        }

    }
}
