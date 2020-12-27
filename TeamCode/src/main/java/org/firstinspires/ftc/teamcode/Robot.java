package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (group = "17703", name = "Basic Robot")
public class Robot extends LinearOpMode {

    DcMotor m1, m2, m3, m4;
    Servo wristServo;
    DcMotorEx wobbleMotor, shooter, hopper;
    private final double SHOOTER_MAX_VELOCITY = 2180;

    @Override
    public void runOpMode() {

        m1 = hardwareMap.dcMotor.get("back_left_motor");
        m2 = hardwareMap.dcMotor.get("front_left_motor");
        m3 = hardwareMap.dcMotor.get("front_right_motor");
        m4 = hardwareMap.dcMotor.get("back_right_motor");
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m1.setDirection(DcMotorSimple.Direction.REVERSE);
        m2.setDirection(DcMotorSimple.Direction.REVERSE);

        wobbleMotor = (DcMotorEx) hardwareMap.dcMotor.get("arm_motor");
        //Because we want the wobble motor to only rotate down, the mode will need to run to a certain position (90 degrees = wobbleEncoderMax)
        wobbleMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        wobbleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        wristServo = hardwareMap.servo.get("hand_servo");

        shooter = (DcMotorEx) hardwareMap.dcMotor.get("shooter_motor");
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        hopper = (DcMotorEx) hardwareMap.dcMotor.get("hopper_motor");

        waitForStart();

        telemetry.addData("Status", "Initializing");
        telemetry.update();


        while (opModeIsActive()) {

            telemetry.addData("Status", "Running");
            telemetry.addData("Shooter Velocity", shooter.getVelocity());
            telemetry.update();


            double powerStrafe = -gamepad1.left_stick_x;
            if (Math.abs(powerStrafe) < 0.05) powerStrafe = 0;
            double powerForward = gamepad1.left_stick_y;
            if (Math.abs(powerForward) < 0.05) powerForward = 0;
            double powerTurn = gamepad1.right_stick_x;
            if (Math.abs(powerTurn) < 0.05) powerTurn = 0;
            double p1 = -powerStrafe + powerForward - powerTurn;
            double p2 = powerStrafe + powerForward - powerTurn;
            double p3 = -powerStrafe + powerForward + powerTurn;
            double p4 = powerStrafe + powerForward + powerTurn;
            double max = Math.max(1.0, Math.abs(p1));
            max = Math.max(max, Math.abs(p2));
            max = Math.max(max, Math.abs(p3));
            max = Math.max(max, Math.abs(p4));
            p1 /= max;
            p2 /= max;
            p3 /= max;
            p4 /= max;
            m1.setPower(p1);
            m2.setPower(p2);
            m3.setPower(p3);
            m4.setPower(p4);

            if (gamepad2.left_bumper) {
                wobbleMotor.setPower(-.30);
            } else if (gamepad2.right_bumper) {
                wobbleMotor.setPower(.30);
            } else {
                wobbleMotor.setPower(0);
            }

            if (gamepad2.x) {
                //Opens the wrist
                wristServo.setPosition(1);
            } else if (gamepad2.b) {
                //Closes the wrist
                wristServo.setPosition(.3);
            }

            if (gamepad2.right_trigger > .05) {
                shooter.setVelocity(1750);
            } else {
                shooter.setPower(0);
            }

            if (gamepad2.left_trigger > .05) {
                hopper.setPower(1);
            } else {
                hopper.setPower(0);
            }
        }
    }
}