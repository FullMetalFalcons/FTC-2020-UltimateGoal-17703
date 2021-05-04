package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous (name = "Merlins Encoder Auto", group = "Merlins")
public class MerlinsEncAutoMatch2 extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor Intakewheels;
    private DcMotor Transfer;
    private DcMotorEx Shooterwheel;
    private DcMotor Arm;
    private Servo Kicker;
    private Servo Clamp;

    @Override
    public void runOpMode()  {

        frontLeft = hardwareMap.get(DcMotor.class, "Front left");
        backLeft = hardwareMap.get(DcMotor.class, "Back left");
        frontRight = hardwareMap.get(DcMotor.class, "Front right");
        backRight = hardwareMap.get(DcMotor.class, "Back right");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        Intakewheels = hardwareMap.get(DcMotor.class, "Intake wheels");
        Transfer = hardwareMap.get(DcMotor.class, "Transfer ");

        Shooterwheel = (DcMotorEx)hardwareMap.get(DcMotor.class, "Shooter wheel");

        Arm = hardwareMap.get(DcMotor.class, "Arm");

        Kicker = hardwareMap.get(Servo.class,"Kicker");
        Clamp = hardwareMap.get(Servo.class, "Clamp");

        waitForStart();

        if (opModeIsActive()) {
            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            setTargetPos(1000, false, false, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            moveForward();

            while (frontLeft.isBusy()) {
                telemetry.addData("Status", "Moving Forward");
                telemetry.update();
            }

            setPower(0, 0, 0);
            sleep(1000);

            shootDisc();

            telemetry.addData("Status", "Stopped");
            telemetry.update();
            sleep(5000);
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
        backLeft.setPower(p1);
        frontLeft.setPower(p2);
        frontRight.setPower(p3);
        backRight.setPower(p4);
    }

    void setMode(DcMotor.RunMode mode) {
        frontRight.setMode(mode);
        backLeft.setMode(mode);
        backRight.setMode(mode);
        frontLeft.setMode(mode);
    }

    void moveForward() {
        setPower(0, .5f, 0);
    }

    void moveBackward() {
        setPower(0, -.5f, 0);
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
        backLeft.setTargetPosition(pos1);
        frontLeft.setTargetPosition(pos2);
        frontRight.setTargetPosition(pos3);
        backRight.setTargetPosition(pos4);
    }

    void loadDisc() {
        Kicker.setDirection(Servo.Direction.REVERSE);
        Kicker.setPosition(1);
        sleep(1000);

        Kicker.setDirection(Servo.Direction.FORWARD);
        Kicker.setPosition(.5);
        sleep(1000);
    }

    void shootDisc() {
        Shooterwheel.setVelocity(1750);
        while (Shooterwheel.isMotorEnabled()) {
            sleep(1500);
            loadDisc();
            loadDisc();
            loadDisc();
            sleep(1500);
            break;
        }
        Shooterwheel.setVelocity(0);
    }

}

