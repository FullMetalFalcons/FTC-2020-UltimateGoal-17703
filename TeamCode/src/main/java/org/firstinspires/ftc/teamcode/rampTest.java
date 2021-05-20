package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


@Autonomous(group = "FTC", name = "Ramp Test")
public class rampTest extends LinearOpMode {

    private final double ENCODER_TICKS_PER_REVOLUTION = 1120;
    private final double ENCODER_TICKS_PER_TILE = 2130;
    private final double ENCODER_90_TURN_LEFT = -2103;
    private final double ENCODER_90_TURN_RIGHT = 2104;
    private final double ENCODER_STRAFE_LEFT = -2080;
    private final double ENCODER_STRAFE_RIGHT = 2080;

    DcMotor m1, m2, m3, m4;
    BNO055IMU imu;
    Orientation orientation;
    int stackGenerator = (int)(Math.random()*3);

    static final double INCREMENT   = 0.15;     // amount to ramp motor each CYCLE_MS cycle
    static final int    CYCLE_MS    =   50;     // period of each cycle
    static final double MAX_FWD     =  .5;     // Maximum FWD power applied to motor
    static final double MAX_REV     = -.5;     // Maximum REV power applied to motor

    // Define class members
    DcMotor motor;
    double  power   = 0;
    boolean rampUp  = true;

    public void moveForward() {
        setPower(0, .6f, 0);
    }
    public void moveBackwards() {
        setPower(0, -.6f, 0);
    }
    public void strafeLeft() {
        setPower(-1,0,0);
    }
    public void strafeRight() {
        setPower(1, 0, 0);
    }
    public void turnLeft() {
        setPower(0, 0, .5f);
    }
    public void turnRight() {
        setPower(0, 0, -.5f);
    }
    public void stopBot() {
        setPower(0,0,0);
    }
    public void resetEnc() {
        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    void setMotorPower(double power) {
        m1.setPower(power);
        m2.setPower(power);
        m3.setPower(power);
        m4.setPower(power);
    }

    @Override
    public void runOpMode() throws InterruptedException {

        m1 = hardwareMap.dcMotor.get("back_left_motor");
        m2 = hardwareMap.dcMotor.get("front_left_motor");
        m3 = hardwareMap.dcMotor.get("front_right_motor");
        m4 = hardwareMap.dcMotor.get("back_right_motor");
        m1.setDirection(DcMotorSimple.Direction.REVERSE);
        m2.setDirection(DcMotorSimple.Direction.REVERSE);
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(new BNO055IMU.Parameters());

        waitForStart();

        while (opModeIsActive()) {


            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            setEncPos(-2500);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);

            while (power > -.5) {
                rampUp();
                sleep(200);

            }

            while (m1.isBusy()) {
                while (power < .2 && m1.getCurrentPosition() > .2*m1.getTargetPosition()) {
                    rampDown();
                    sleep(200);
                    stopBot();
                }
            }
            stopBot();




        }

    }

    void rampUp() {
        if (rampUp) {

            // Keep stepping up until we hit the max value.
            power -= INCREMENT ;
            if (power >= MAX_FWD ) {
                power = MAX_FWD;
                rampUp = !rampUp;   // Switch ramp direction
            }
        }

        telemetry.addData("Motor Power", "%5.2f", power);
        telemetry.addData("frontLeft Power", m1.getPower());
        telemetry.addData(">", "Press Stop to end test." );
        telemetry.update();

        setMotorPower(power);
        sleep(CYCLE_MS);
        idle();

    }

    void rampDown() {
        if (!rampUp) {

            // Keep stepping down until we hit the min value.
            power += INCREMENT ;

            if (power <= MAX_REV ) {
                power = MAX_REV;
                rampUp = !rampUp;  // Switch ramp direction
            }
        }

        telemetry.addData("Motor Power", "%5.2f", power);
        telemetry.addData("frontLeft Power", m1.getPower());
        telemetry.addData(">", "Press Stop to end test." );
        telemetry.addData("Status", "Ramping down");
        telemetry.update();

        setMotorPower(power);
        sleep(CYCLE_MS);
        idle();
    }

    void setMode(DcMotor.RunMode mode) {
        m1.setMode(mode);
        m2.setMode(mode);
        m3.setMode(mode);
        m4.setMode(mode);
    }

    void setEncPos(int encTicks) {
        m1.setTargetPosition(encTicks);
        m2.setTargetPosition(encTicks);
        m3.setTargetPosition(encTicks);
        m4.setTargetPosition(encTicks);
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
}

