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
    DcMotorEx wobbleMotor, shooter;

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
        wobbleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wobbleMotor.setTargetPosition(0);
        wobbleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        wristServo = hardwareMap.servo.get("hand_servo");

        shooter = (DcMotorEx) hardwareMap.dcMotor.get("shooter_motor");
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();

        telemetry.addData("Status", "Initializing");
        telemetry.update();


        while (opModeIsActive()) {

            telemetry.addData("Status", "Running");
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
                /*Sets the target position for the encoder to be the wobbleEncoderMax (90 degree turn theoretically)
                and it will run forward until it reaches that position*/
                wobbleMotor.setTargetPosition(-400);
                wobbleMotor.setPower(.10);
            } else if (gamepad2.right_bumper) {
                /*Sets the target position for the encoder of the wobble motor to be zero (initial position) and
                the setPower will mean it reverses until it returns to that position*/
                wobbleMotor.setTargetPosition(-100);
                wobbleMotor.setPower(-.10);
            } else if (gamepad2.a) {
                wobbleMotor.setTargetPosition(-50);
                wobbleMotor.setPower(-.1);
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

            if (gamepad2.y) {
                shooter.setPower(1);
                telemetry.addData("Current Velocity", shooter.getVelocity());
                telemetry.update();
            } else {
                shooter.setPower(0);
            }
        }
    }
}