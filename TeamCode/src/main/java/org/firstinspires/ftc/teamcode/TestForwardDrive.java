package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp (name = "Test Forward", group = "FMF")
public class TestForwardDrive extends LinearOpMode {

    DcMotor frontLeft, frontRight, backLeft, backRight;

    @Override
    public void runOpMode() {

        frontLeft = hardwareMap.dcMotor.get("front_left_motor");
        frontRight = hardwareMap.dcMotor.get("front_right_motor");
        backLeft = hardwareMap.dcMotor.get("back_left_motor");
        backRight = hardwareMap.dcMotor.get("back_right_motor");


        telemetry.addData("Status", "Stopped");
        telemetry.update();

        waitForStart();

        telemetry.addData("Status", "Initializing");
        telemetry.update();

        double testPower = 0;
        while (opModeIsActive()) {
            testPower = gamepad1.left_stick_y;
            telemetry.addData("Status", "Running");
            telemetry.update();
            frontLeft.setPower(testPower);
            frontRight.setPower(testPower);
            backLeft.setPower(testPower);
            backRight.setPower(testPower);
            telemetry.addData("Target Power", testPower);
            telemetry.addData("Actual Power", String.valueOf(frontLeft.getPower()), frontRight.getPower(), backRight.getPower(), backLeft.getPower());
            telemetry.update();
        }

    }
}
