package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (group = "17703", name = "Basic Robot")
public class Robot extends LinearOpMode {

    DcMotor backLeftMotor, frontLeftMotor, frontRightMotor, backRightMotor;
    Servo wristServo;
    DcMotorEx wobbleMotor, shooter, hopper, intake;
    private final double SHOOTER_MAX_VELOCITY = 2180;

    @Override
    public void runOpMode() {

        backLeftMotor = hardwareMap.dcMotor.get("back_left_motor");
        frontLeftMotor = hardwareMap.dcMotor.get("front_left_motor");
        frontRightMotor = hardwareMap.dcMotor.get("front_right_motor");
        backRightMotor = hardwareMap.dcMotor.get("back_right_motor");
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        wobbleMotor = (DcMotorEx) hardwareMap.dcMotor.get("arm_motor");
        //Because we want the wobble motor to only rotate down, the mode will need to run to a certain position (90 degrees = wobbleEncoderMax)
        wobbleMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        wobbleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        wristServo = hardwareMap.servo.get("hand_servo");

        shooter = (DcMotorEx) hardwareMap.dcMotor.get("shooter_motor");
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        hopper = (DcMotorEx) hardwareMap.dcMotor.get("hopper_motor");

        intake = (DcMotorEx) hardwareMap.dcMotor.get("intake_motor");
        //intake.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        telemetry.addData("Status", "Initializing");
        telemetry.update();


        while (opModeIsActive()) {

            telemetry.addData("Status", "Running");
            telemetry.addData("Wobble Encoder Value", wobbleMotor.getCurrentPosition());
            telemetry.addData("Drive Base Encoders", backLeftMotor.getCurrentPosition());
            telemetry.addData("Shooter Velocity", shooter.getVelocity());
            telemetry.update();


            double powerStrafe = -gamepad1.left_stick_x;
            if (Math.abs(powerStrafe) < 0.05) powerStrafe = 0;
            double powerForward = gamepad1.left_stick_y;
            if (Math.abs(powerForward) < 0.05) powerForward = 0;
            double powerTurn = gamepad1.right_stick_x;
            if (Math.abs(powerTurn) < 0.05) powerTurn = 0;
            //If the right trigger is held down, robot goes into slow mode where it moves at 1/5 of full power compared to 4/5
            if (gamepad1.left_trigger > .05) {
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
                backLeftMotor.setPower(p1 * .2);
                frontLeftMotor.setPower(p2 * .2);
                frontRightMotor.setPower(p3 * .2);
                backRightMotor.setPower(p4 * .2);
            } else if (gamepad1.right_trigger > .05) {
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
                backLeftMotor.setPower(p1 * .4);
                frontLeftMotor.setPower(p2 * .4);
                frontRightMotor.setPower(p3 * .4);
                backRightMotor.setPower(p4 * .4);
            } else {
                //Allows the robot to move
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
                backLeftMotor.setPower(p1 * .8);
                frontLeftMotor.setPower(p2 * .8);
                frontRightMotor.setPower(p3 * .8);
                backRightMotor.setPower(p4 * .8);
            }


            if (gamepad2.left_bumper) {
                wobbleMotor.setPower(.2);
            } else if (gamepad2.right_bumper) {
                wobbleMotor.setPower(-.2);
            } else {
                wobbleMotor.setPower(0);
            }

            if (gamepad2.x) {
                //Closes the wrist
                wristServo.setPosition(.9);
            } else if (gamepad2.b) {
                //Opens the wrist.
                wristServo.setPosition(.3);
            }

            if (gamepad2.y) {
                shooter.setVelocity(1750);
            } else {
                shooter.setPower(0);
            }

            if (gamepad2.right_trigger > .05) {
                hopper.setPower(1);
                intake.setPower(1);
            } else if (gamepad2.left_trigger > .05) {
                hopper.setPower(-1);
                intake.setPower(-1);
            } else {
                hopper.setPower(0);
                intake.setPower(0);
            }

           /* if (gamepad2.right_trigger > .05) {
                intake.setPower(1);
            } else {
                intake.setPower(0);
            } */
        }
    }
}