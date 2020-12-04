package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp (name = "Test Forward", group = "FMF")
public class TestForwardDrive extends LinearOpMode {

    DcMotor m1, m2, m3, m4;

    @Override
    public void runOpMode() {

        m1 = hardwareMap.dcMotor.get("back_left_motor");
        m2 = hardwareMap.dcMotor.get("front_left_motor");
        m3 = hardwareMap.dcMotor.get("front_right_motor");
        m4 = hardwareMap.dcMotor.get("back_right_motor");
        m1.setDirection(DcMotorSimple.Direction.REVERSE);
        m2.setDirection(DcMotorSimple.Direction.REVERSE);

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
            m1.setPower(testPower);
            m2.setPower(testPower);
            m3.setPower(testPower);
            m4.setPower(testPower);

        }

    }
}
