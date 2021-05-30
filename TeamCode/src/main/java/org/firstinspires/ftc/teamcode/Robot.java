package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (group = "17703", name = "Basic Robot")
public class Robot extends LinearOpMode {

    DcMotor backLeftMotor, frontLeftMotor, frontRightMotor, backRightMotor;
    Servo wristServo;
    DcMotorEx wobbleMotor, shooter, hopper, intake;
    private final double SHOOTER_MAX_VELOCITY = 2180;
    DigitalChannel digitalTouch;

    @Override
    public void runOpMode() {

        backLeftMotor = hardwareMap.dcMotor.get("back_left_motor");
        frontLeftMotor = hardwareMap.dcMotor.get("front_left_motor");
        frontRightMotor = hardwareMap.dcMotor.get("front_right_motor");
        backRightMotor = hardwareMap.dcMotor.get("back_right_motor");
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        wobbleMotor = (DcMotorEx) hardwareMap.dcMotor.get("arm_motor");
        wobbleMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        wobbleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        wristServo = hardwareMap.servo.get("hand_servo");

        shooter = (DcMotorEx) hardwareMap.dcMotor.get("shooter_motor");
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        hopper = (DcMotorEx) hardwareMap.dcMotor.get("hopper_motor");

        intake = (DcMotorEx) hardwareMap.dcMotor.get("intake_motor");
        //intake.setDirection(DcMotorSimple.Direction.REVERSE);

        digitalTouch = hardwareMap.get(DigitalChannel.class, "digital_touch");
        digitalTouch.setMode(DigitalChannel.Mode.INPUT);

        waitForStart();

        telemetry.addData("Status", "Initializing");
        telemetry.update();


        while (opModeIsActive()) {

            telemetry.addData("Status", "Running");
            telemetry.addData("Wobble Encoder Value", wobbleMotor.getCurrentPosition());
            telemetry.addData("Drive Base Encoders", backLeftMotor.getCurrentPosition());
            telemetry.addData("Shooter Velocity", shooter.getVelocity());
            telemetry.addData("Front Right Power", frontLeftMotor.getPower());
            telemetry.addData("Front Left Power", frontLeftMotor.getPower());
            telemetry.addData("Back Right Power", backRightMotor.getPower());
            telemetry.addData("Back Left Power", backLeftMotor.getPower());
            telemetry.update();

            if (gamepad1.x) {
                setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                setTargetPos(-50, false, false, false, true);
                setMode(DcMotor.RunMode.RUN_TO_POSITION);
                turnLeft();

                while (frontLeftMotor.isBusy()) {
                    telemetry.update();
                }

                stopBot();
                setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            } else if (gamepad1.b) {
                setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                setTargetPos(-50, false, false, true, false);
                setMode(DcMotor.RunMode.RUN_TO_POSITION);
                turnRight();

                while (frontLeftMotor.isBusy()) {
                    telemetry.update();
                }

                stopBot();
                setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }

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
                backLeftMotor.setPower(p1 * .3);
                frontLeftMotor.setPower(p2 * .3);
                frontRightMotor.setPower(p3 * .3);
                backRightMotor.setPower(p4 * .3);
                //.2
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
                //.4
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
                backLeftMotor.setPower(p1 * .7);
                frontLeftMotor.setPower(p2 * .7);
                frontRightMotor.setPower(p3 * .7);
                backRightMotor.setPower(p4 * .7);
            }


            if (gamepad2.left_bumper && (digitalTouch.getState() == true)) {
                wobbleMotor.setPower(.2);
            } else if (gamepad2.right_bumper) {
                wobbleMotor.setPower(-.2);
            } else {
                wobbleMotor.setPower(0);
            }

            /*
            if (gamepad2.x) {
                //Closes the wrist
                wristServo.setPosition(.9);
            } else if (gamepad2.b) {
                //Opens the wrist.
                wristServo.setPosition(.3);
            }
             */
            if (gamepad2.x) {
                //Closes the wrist
                wristServo.setPosition(.1);
            } else if (gamepad2.b) {
                //Opens the wrist.
                wristServo.setPosition(.8);
            } else {
                //Reset wrist
                wristServo.setPosition(0.2);
            }

            if (gamepad2.y) {
                shooter.setVelocity(1625);
            } else if (gamepad2.a) {
                shooter.setVelocity(1400);
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


        }
    }

    void setPower(float powerStrafe, float powerForward, float powerTurn) {
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
        backLeftMotor.setPower(p1);
        frontLeftMotor.setPower(p2);
        frontRightMotor.setPower(p3);
        backRightMotor.setPower(p4);
    }

    private void stopBot() {
        backLeftMotor.setPower(0);
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);
    }

    void resetEnc() {
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wobbleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wobbleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    void moveForward() {
        setPower(0, -.3f, 0);
    }

    void moveBackward() {
        setPower(0, .3f, 0);
    }

    void strafeLeft() {
        setPower(.3f, 0, 0);
    }

    void strafeRight() {
        setPower(-.3f, 0, 0);
    }

    void turnLeft() {
        setPower(0, 0, -.3f);
    }

    void turnRight() {
        setPower(0, 0, .3f);
    }

    void setTargetPos(int encTicks, boolean isStrafingRight, boolean isStrafingLeft, boolean isTurningRight, boolean isTurningLeft) {
        int pos1 = encTicks;
        int pos2 = encTicks;
        int pos3 = encTicks;
        int pos4 = encTicks;
        //In Android Studio this would be for strafing right
        if (isStrafingRight == true) {
            pos1 = -encTicks;
            pos2 = encTicks;
            pos3 = -encTicks;
            pos4 = encTicks;
        }
        //In Android Studio this would be for strafing left
        if (isStrafingLeft == true) {
            pos1 = encTicks;
            pos2 = -encTicks;
            pos3 = encTicks;
            pos4 = -encTicks;
        }
        if (isTurningRight == true) {
            pos1 = encTicks;
            pos2 = encTicks;
            pos3 = -encTicks;
            pos4 = -encTicks;
        }
        if (isTurningLeft) {
            pos1 = -encTicks;
            pos2 = -encTicks;
            pos3 = encTicks;
            pos4 = encTicks;
        }
        backLeftMotor.setTargetPosition(pos1);
        frontLeftMotor.setTargetPosition(pos2);
        frontRightMotor.setTargetPosition(pos3);
        backRightMotor.setTargetPosition(pos4);
    }

    void setMode(DcMotor.RunMode mode) {
        frontRightMotor.setMode(mode);
        frontLeftMotor.setMode(mode);
        backRightMotor.setMode(mode);
        backLeftMotor.setMode(mode);
    }
}