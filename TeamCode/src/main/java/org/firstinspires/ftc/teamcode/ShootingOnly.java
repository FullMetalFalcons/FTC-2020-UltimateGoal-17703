package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous (name = "Shooter Only no Moving", group = "17703")
public class ShootingOnly extends LinearOpMode {

    DcMotor backLeftMotor, frontLeftMotor, frontRightMotor, backRightMotor;
    Servo wristServo;
    DcMotorEx wobbleMotor, shooter, hopper, intake;

    @Override
    public void runOpMode() throws InterruptedException {
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
        wobbleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        wristServo = hardwareMap.servo.get("hand_servo");

        shooter = (DcMotorEx) hardwareMap.dcMotor.get("shooter_motor");
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        hopper = (DcMotorEx) hardwareMap.dcMotor.get("hopper_motor");

        intake = (DcMotorEx) hardwareMap.dcMotor.get("intake_motor");

        waitForStart();

        if (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();

            shootDiscDif();
            sleep(1000);
            shootDiscDif();

        }
    }

    void shootDisc() {
        shooter.setVelocity(1550);
        sleep(1750);
        hopper.setPower(1);
        intake.setPower(1);
        sleep(3000);
        shooter.setPower(0);
        hopper.setPower(0);
        intake.setPower(0);
    }

    void shootDiscDif() {
        shooter.setVelocity(1600);
        while (shooter.isMotorEnabled()) {
            sleep(2000);
            hopper.setPower(1);
            intake.setPower(1);
            sleep(1000);
            hopper.setPower(-1);
            sleep(100);
            shooter.setMotorDisable();
        }
        shooter.setMotorEnable();
        shooter.setPower(0);
        hopper.setPower(0);
        intake.setPower(0);
        //In the robot, mark the position where all 3 discs will start. From there, get encoder values required to bring the discs to the shooter.
        //So, codewise it could be run shooter while the hopper is moving. Might not need intake to run
    }

    void shootDiscWhileVelocity() {
        shooter.setVelocity(1750);

        while (shooter.getVelocity() > 50) {
            sleep(1500);
            hopper.setPower(1);
            intake.setPower(1);
            sleep(3000);
            shooter.setPower(0);
        }
        hopper.setPower(0);
        intake.setPower(0);
    }

    void shootPowershots() {

    }
}
