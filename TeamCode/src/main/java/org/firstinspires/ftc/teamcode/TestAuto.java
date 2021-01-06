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

    @Override
    public void runOpMode() {

        m1 = hardwareMap.dcMotor.get("back_left_motor");
        m2 = hardwareMap.dcMotor.get("front_left_motor");
        m3 = hardwareMap.dcMotor.get("front_right_motor");
        m4 = hardwareMap.dcMotor.get("back_right_motor");
        m1.setTargetPosition(0);
        m2.setTargetPosition(0);
        m3.setTargetPosition(0);
        m4.setTargetPosition(0);
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
            m1.setTargetPosition(-500);
            m2.setTargetPosition(-500);
            m3.setTargetPosition(-500);
            m4.setTargetPosition(-500);
           while (m3.getCurrentPosition() > m3.getTargetPosition() && m2.getCurrentPosition() > m1.getTargetPosition()) {
               moveForward();
           }
           stopBot();

           telemetry.addData("Final Encoder Ticks", m1.getCurrentPosition());
           telemetry.update();

        }

    }

    void setPower(float powerStrafe, float powerForward, float powerTurn){
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
        setPower(0, -.5f, 0);
    }
    private void stopBot() {
        m1.setPower(0);
        m2.setPower(0);
        m3.setPower(0);
        m4.setPower(0);
    }
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
        setPower(0, 0, .5f);
    }
    void turnRight() {
        setPower(0, 0, -.5f);
    }
}
