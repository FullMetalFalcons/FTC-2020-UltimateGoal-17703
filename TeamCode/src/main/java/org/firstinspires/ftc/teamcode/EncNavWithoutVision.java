package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous (name = "Encoder Nav Without Vision", group = "17703")
public class EncNavWithoutVision extends LinearOpMode {

    DcMotor backLeftMotor, frontLeftMotor, frontRightMotor, backRightMotor;
    Servo wristServo;
    DcMotorEx wobbleMotor, shooter, hopper, intake;
    int strafeTile = -900;
    int forwardTile = -750;
    int turn90;

    //The encoder tick value for the arm being in position to drop the wobble goal
    int dropPosition = -4100;
    //The encoder tick value for the arm at rest
    int restPosition = 240;

    @Override
    public void runOpMode() {

        backLeftMotor= hardwareMap.dcMotor.get("back_left_motor");
        frontLeftMotor = hardwareMap.dcMotor.get("front_left_motor");
        frontRightMotor = hardwareMap.dcMotor.get("front_right_motor");
        backRightMotor = hardwareMap.dcMotor.get("back_right_motor");

        backLeftMotor.setTargetPosition(0);
        frontLeftMotor.setTargetPosition(0);
        frontRightMotor.setTargetPosition(0);
        backRightMotor.setTargetPosition(0);

        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        wobbleMotor = (DcMotorEx) hardwareMap.dcMotor.get("arm_motor");
        //Because we want the wobble motor to only rotate down, the mode will need to run to a certain position (90 degrees = wobbleEncoderMax)
        wobbleMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        wobbleMotor.setTargetPosition(0);
        wobbleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        wristServo = hardwareMap.servo.get("hand_servo");

        shooter = (DcMotorEx) hardwareMap.dcMotor.get("shooter_motor");
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        hopper = (DcMotorEx) hardwareMap.dcMotor.get("hopper_motor");

        intake = (DcMotorEx) hardwareMap.dcMotor.get("intake_motor");

        waitForStart();

        if (opModeIsActive()) {

            setTargetPos(-550, false, true, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            strafeLeft();

            while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                telemetry.addData("Status", "Strafing Left");
                telemetry.update();
            }

            stopBot();
            sleep(200);

            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            setTargetPos(-200, false, false, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            moveForward();

            while (backLeftMotor.isBusy() && backRightMotor.isBusy() && frontRightMotor.isBusy() && frontLeftMotor.isBusy()) {
                telemetry.addData("Status", "Moving Forward");
                telemetry.update();
            }

            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            turnRight();
            sleep(50);

            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            stopBot();
            sleep(100);


            //Get to square A
            setTargetPos(strafeTile, false, true, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            strafeLeft();

            while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                telemetry.addData("Robot Status", "Strafing Left");
                telemetry.update();
            }
            stopBot();
            sleep(175);

            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            turnRight();
            sleep(150);


            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            setTargetPos(forwardTile*4, false, false, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            moveForward();

            while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                telemetry.addData("Robot Status", "Moving Forward");
                telemetry.update();
            }


            //Code for dropping the wobble, moving forward a bit, and then raising the arm
            //dropWobble();
            //setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //moveForward();
            //sleep(50);
            //stopBot();
            //sleep(100);
            //raiseWobble();


            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            setTargetPos(2*strafeTile, true, false, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            strafeRight();

            while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                telemetry.addData("Robot Status", "Strafing to Right to Avoid Wobble");
                telemetry.update();
            }

            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            setTargetPos(300, false, false, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            moveBackward();

            while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                telemetry.addData("Robot Status", "Moving Back to Park");
                telemetry.update();
            }
            stopBot();
            telemetry.addData("Robot Status", "Parked");
            telemetry.update();


            //Get to Square B

/*
            setTargetPos(strafeTile, true, false, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            strafeRight();

            while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                telemetry.addData("Robot Status", "Strafing Right");
                telemetry.update();
            }

            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            turnLeft();
            sleep(50);
            moveForward();
            sleep(150);

            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            setTargetPos(5*forwardTile, false, false, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            moveForward();

            while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                telemetry.addData("Robot Status", "Moving Forward");
                telemetry.update();
            }

            //Code for dropping the wobble, moving forward a bit, and then raising the arm
           // dropWobble();
            //setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //moveForward();
            //sleep(50);
            //raiseWobble();
            //stopBot();

            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            setTargetPos(strafeTile, false, true, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            strafeLeft();

            while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                telemetry.addData("Robot Status", "Strafing Left");
                telemetry.update();
            }
            stopBot();
            sleep(500);

            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            setTargetPos(strafeTile-400, true, false, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            strafeRight();

            while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                telemetry.addData("Robot Status", "Strafing Right to Avoid Wobble Goal");
                telemetry.update();
            }

            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            setTargetPos(1200, false, false, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            moveBackward();

            while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                telemetry.addData("Robot Status", "Moving Back to Park");
                telemetry.update();
            }
            stopBot();
            telemetry.addData("Robot Status", "Parked");
            telemetry.update();
*/

            //Get to square C
/*

            setTargetPos(strafeTile, false, true, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            strafeLeft();

            while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                telemetry.addData("Robot Status", "Strafing Left");
                telemetry.update();
            }
            stopBot();
            sleep(150);

            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            turnRight();
            sleep(175);

            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            setTargetPos((int) (forwardTile*4.2), false, false, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            moveForward();

            while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                telemetry.addData("Robot Status", "Moving Forward");
                telemetry.update();
            }

            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            turnRight();
            sleep(1750);

            stopBot();
            sleep(300);

            //Drop wobble here

            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            setTargetPos(forwardTile, false, false, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            moveForward();

            while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                telemetry.addData("Robot Status", "Moving to Parking Zone");
                telemetry.update();
            }

            stopBot();
            telemetry.addData("Robot Status", "Parked");
            telemetry.update();
*/
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

    void dropWobble() {
        //Will have to revise this with accurate sign value
        wobbleMotor.setTargetPosition(-1700);
        wobbleMotor.setPower(-.15);
        wristServo.setPosition(.3);
    }

    void raiseWobble() {
        wobbleMotor.setTargetPosition(0);
        wobbleMotor.setPower(.15);
        wristServo.setPosition(.9);
    }

    void shootDisc() {
        shooter.setVelocity(1650);
        sleep(700);
        hopper.setPower(1);
        intake.setPower(1);
        sleep(2300);
        shooter.setPower(0);
        hopper.setPower(0);
        intake.setPower(0);
    }

    void setMode(DcMotor.RunMode mode) {
        frontRightMotor.setMode(mode);
        frontLeftMotor.setMode(mode);
        backRightMotor.setMode(mode);
        backLeftMotor.setMode(mode);
    }

}
