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

        if (opModeIsActive()) {

            telemetry.addData("Status", "Running");
            telemetry.addData("Initial Wobble Encoder Ticks", wobbleMotor.getCurrentPosition());
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

            /*
            setTargetPos(-800, true, false);
            strafeRight();
            while (m1.isBusy() && m2.isBusy() && m3.isBusy() && m4.isBusy()) {
                //Wait for them to stop
                telemetry.addData("Status", "Moving Forward");
                telemetry.addData("Encoders", m1.getCurrentPosition());
                telemetry.update();
            }
            stopBot();
*/
            //Move to starting position

            setTargetPos(-550, true, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            testMove();


            //Make code for the powershots using IMU

            while (m1.isBusy() && m2.isBusy() && m3.isBusy() && m4.isBusy()) {
                telemetry.addData("Status", "Strafing Left");
                telemetry.update();
            }

           stopBot();

/*
            stopBot();
            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            setTargetPos(0, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //Could disable motors while shoot and then re-enable them
            shootDiscDif();
            sleep(1000);
            shootDiscDif();
            sleep(200);

            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            setTargetPos(-200, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            moveForward();

            while (m1.isBusy() && m2.isBusy() && m3.isBusy() && m4.isBusy()) {
                telemetry.addData("Status", "Moving Forward");
                telemetry.update();
            }

            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            turnRight();
            sleep(50);

            stopBot();

            //Test the wobble
            /*
            //Works

            dropWobble();
            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            moveForward();
            sleep(100);
            stopBot();
            sleep(200);
            raiseWobble();
            stopBot();

*/







            /*
            telemetry.addData("Status", "Paused");
            telemetry.update();
            setPower(0, 0,0);
            sleep(500);

            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            setTargetPos(-1500, true, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            strafeRight();

            while (m1.isBusy() && m2.isBusy() && m3.isBusy() &&m4.isBusy()) {
                telemetry.addData("Status", "Strafing");
                telemetry.update();
            }
            telemetry.addData("Status", "Stopped");
            telemetry.update();
            setPower(0, 0, 0);

*/
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




            telemetry.addData("Wrist Position", wristServo.getPosition());
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
        setPower(0, 0, -.3f);
    }

    void turnRight() {
        setPower(0, 0, .3f);
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
        wobbleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Will have to revise this with accurate sign value
        wobbleMotor.setTargetPosition(-1600);
        wobbleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wobbleMotor.setPower(-.15);

        while (wobbleMotor.isBusy()) {
            telemetry.addData("Status", "Wobble Lowering");
            telemetry.update();
        }
        wobbleMotor.setPower(0);
        wristServo.setPosition(.3);
    }

    void raiseWobble() {
        wobbleMotor.setTargetPosition(0);
        wobbleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wobbleMotor.setPower(.15);

        while (wobbleMotor.isBusy()) {
            telemetry.addData("Status", "Wobble Raising");
            telemetry.update();
        }
        wobbleMotor.setPower(0);
        wristServo.setPosition(.9);
    }


    void setMode(DcMotor.RunMode mode) {
        m1.setMode(mode);
        m2.setMode(mode);
        m3.setMode(mode);
        m4.setMode(mode);
    }

    void shootDisc() {
        shooter.setVelocity(1675);
        sleep(1000);
        while (shooter.isMotorEnabled()) {
            hopper.setPower(1);
            sleep(1000);
            hopper.setPower(-1);
            sleep(100);
            shooter.setMotorDisable();
        }
        shooter.setMotorEnable();
        shooter.setPower(0);
        hopper.setPower(0);
    }

    void testMove() {
        m1.setPower(.5);
        m2.setPower(.5);
        m3.setPower(.5);
        m4.setPower(.5);
    }

}

