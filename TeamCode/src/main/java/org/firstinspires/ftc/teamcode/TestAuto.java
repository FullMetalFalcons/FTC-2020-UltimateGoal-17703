package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous (group = "FTC", name = "Auto Test")
public class TestAuto extends LinearOpMode {

    DcMotor m1, m2, m3, m4;
    Servo wristServo;
    DcMotorEx wobbleMotor, shooter, hopper, intake;

    private final double WHEEL_DIAMETER_INCHES = 4; //10.2cm
    private final double ENCODER_TICKS_PER_REV = 1120;
    int dropPosition = -4776;

    @Override
    public void runOpMode() throws InterruptedException {

        m1 = hardwareMap.dcMotor.get("back_left_motor");
        m2 = hardwareMap.dcMotor.get("front_left_motor");
        m3 = hardwareMap.dcMotor.get("front_right_motor");
        m4 = hardwareMap.dcMotor.get("back_right_motor");
        m1.setTargetPosition(0);
        m2.setTargetPosition(0);
        m3.setTargetPosition(0);
        m4.setTargetPosition(0);
/*        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        */
        m1.setMode(DcMotor.RunMode.RESET_ENCODERS);
        m2.setMode(DcMotor.RunMode.RESET_ENCODERS);
        m3.setMode(DcMotor.RunMode.RESET_ENCODERS);
        m4.setMode(DcMotor.RunMode.RESET_ENCODERS);
        m1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m4.setMode(DcMotor.RunMode.RUN_TO_POSITION);

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

        intake = (DcMotorEx) hardwareMap.dcMotor.get("intake_motor");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        telemetry.addData("Status", "Initializing");
        telemetry.update();

        while (opModeIsActive()) {

            telemetry.addData("Status", "Running");
            telemetry.addData("Initial Encoder Ticks", m1.getCurrentPosition());
            telemetry.update();

            //645 worked at first

            /*setTargetPos(-800, false, false);
            while (m1.getCurrentPosition() > m1.getTargetPosition()) {
                moveForward();
            }
            stopBot();
             */

          /*  setTargetPos(-800, false, false);
            moveForward();
            //resetEncValues();
            sleep(4500);
            resetEncValues();
            */

            setTargetPos(-300, false, false);
            moveForward();

            while (m1.isBusy() && m2.isBusy()) {
                //Wait for them to stop
            }
            setTargetPos(-700, true, false);
            strafeRight();

            while (m1.isBusy() && m2.isBusy()) {
                //Wait for motors to finish previous code
            }

            setPower(0, 0, 0);


            //setTargetPos(-1200, false, false);
            //moveForward();





/*
            //Move backward until at wall - 3 tiles backward
            //Move to the left wall
            //Move forward until our current position equals our target distance
            boolean touchingBackWall = false;
            int ticksToBackWall = -500;

            while (touchingBackWall == false) {
                setTargetPos(3*ticksToBackWall, false, false);
                if (m1.getCurrentPosition() < m1.getTargetPosition()) {
                    touchingBackWall = true;
                }
            }*/





            telemetry.addData("Final Encoder Ticks", m1.getCurrentPosition());
            telemetry.update();

        }
        //...

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
        m1.setPower(p1);
        m2.setPower(p2);
        m3.setPower(p3);
        m4.setPower(p4);
    }

    private void moveForward() {
        setPower(0, -.4f, 0);
    }

    private void stopBot() {
        m1.setPower(0);
        m2.setPower(0);
        m3.setPower(0);
        m4.setPower(0);
    }

    void resetEncValues() {
        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m1.setTargetPosition(0);
        m2.setTargetPosition(0);
        m3.setTargetPosition(0);
        m4.setTargetPosition(0);
        m1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m4.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
/*
    void resetEnc() {
        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wobbleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wobbleMotor.setTargetPosition(0);
        m1.setTargetPosition(0);
        m2.setTargetPosition(0);
        m3.setTargetPosition(0);
        m4.setTargetPosition(0);
        m1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m4.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wobbleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }*/

    void moveBackward() {
        setPower(0, .5f, 0);
    }

    void strafeLeft() {
        setPower(.5f, 0, 0);
    }

    void strafeRight() {
        setPower(-.5f, 0, 0);
    }

    void turnLeft() {
        setPower(0, 0, -.5f);
    }

    void turnRight() {
        setPower(0, 0, .5f);
    }

    void setTargetPos(int encTicks, boolean isStrafingRight, boolean isStrafingLeft) {
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
        m1.setTargetPosition(pos1);
        m2.setTargetPosition(pos2);
        m3.setTargetPosition(pos3);
        m4.setTargetPosition(pos4);
    }

    void dropWobble() {
        //Will have to revise this with accurate sign value
        wobbleMotor.setTargetPosition(dropPosition);
        while (wobbleMotor.getCurrentPosition() >= wobbleMotor.getTargetPosition()) {
            wobbleMotor.setPower(-.3);
        }
        wristServo.setPosition(1);
        sleep(100);
        wristServo.setPosition(.25);
        wobbleMotor.setTargetPosition(0);
        while (wobbleMotor.getCurrentPosition() <= wobbleMotor.getTargetPosition()) {
            wobbleMotor.setPower(.3);
        }
        wobbleMotor.setPower(0);
    }

}
