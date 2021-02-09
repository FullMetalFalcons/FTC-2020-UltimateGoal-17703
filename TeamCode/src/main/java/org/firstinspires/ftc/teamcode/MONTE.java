package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class MONTE {

        protected DcMotor frontLeftMotor = null;
        protected DcMotor backLeftMotor = null;
        protected DcMotor frontRightMotor = null;
        protected DcMotor backRightMotor = null;

        protected Servo wristServo = null;

        protected DcMotorEx shooter = null;
        protected DcMotorEx hopper = null;
        protected DcMotorEx intake = null;
        protected DcMotorEx wobbleMotor = null;

        public void init(HardwareMap hardwareMap) {
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
        }

        public void driveToTick(int ticks, double power) {
            frontLeftMotor.setTargetPosition(ticks);
            frontRightMotor.setTargetPosition(ticks);
            backLeftMotor.setTargetPosition(ticks);
            frontRightMotor.setTargetPosition(ticks);

            frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            frontLeftMotor.setPower(power);
            frontRightMotor.setPower(power);
            backRightMotor.setPower(power);
            backLeftMotor.setPower(power);

            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backRightMotor.setPower(0);
            backLeftMotor.setPower(0);
        }

        public void strafeRightToTick(int ticks, double power){
            frontLeftMotor.setTargetPosition(ticks);
            frontRightMotor.setTargetPosition(-ticks);
            backLeftMotor.setTargetPosition(-ticks);
            frontRightMotor.setTargetPosition(ticks);

            frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            frontLeftMotor.setPower(power);
            frontRightMotor.setPower(power);
            backRightMotor.setPower(power);
            backLeftMotor.setPower(power);
        }

        public void strafeLeftToTick(int ticks, double power) {
            frontLeftMotor.setTargetPosition(-ticks);
            frontRightMotor.setTargetPosition(ticks);
            backLeftMotor.setTargetPosition(ticks);
            frontRightMotor.setTargetPosition(-ticks);

            frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            frontLeftMotor.setPower(power);
            frontRightMotor.setPower(power);
            backRightMotor.setPower(power);
            backLeftMotor.setPower(power);

        }

        public void stopBot() {
            frontRightMotor.setPower(0);
            frontLeftMotor.setPower(0);
            backLeftMotor.setPower(0);
            backRightMotor.setPower(0);
        }

        public void resetEncoders() {
            frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
}
