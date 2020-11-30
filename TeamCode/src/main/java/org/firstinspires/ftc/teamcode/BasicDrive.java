package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp (group = "17703", name = "Mech Test")
public class BasicDrive extends LinearOpMode {

    DcMotor m1, m2, m3, m4;

    @Override
    public void runOpMode() {

        m1 = hardwareMap.dcMotor.get("back_left_motor");
        m2 = hardwareMap.dcMotor.get("front_left_motor");
        m3 = hardwareMap.dcMotor.get("front_right_motor");
        m4 = hardwareMap.dcMotor.get("back_right_motor");
        m1.setDirection(DcMotorSimple.Direction.REVERSE);
        m2.setDirection(DcMotorSimple.Direction.REVERSE);
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //To strafe right, m1 and m3 should be moving back while m2 and m4 should be moving forward


        waitForStart();

        telemetry.addData("Status", "Initializing");
        telemetry.update();

        while (opModeIsActive()) {

            telemetry.addData("Status", "Running");
            telemetry.update();

            double px = gamepad1.left_stick_x;
            //px is basically power of x axis
            if (Math.abs(px) < 0.05) px = 0;  //This is saying if the value of px is < .05 or > -.05, all while less than 0, it is basically 0 to reduce error
            double py = -gamepad1.left_stick_y;
            //py is  the power of the y axis
            if (Math.abs(py) < 0.05) py = 0;   //This is saying if the value of py is < .05 or > -.05, all while less than 0, it is basically 0 to reduce error
            double pa = -gamepad1.right_stick_x;
            //pa is the power of the angle change
            if (Math.abs(pa) < 0.05) pa = 0;   //This is saying if the value of pa is < .05 or > -.05, all while less than 0, it is basically 0 to reduce error
            //These four p statements are power values of each motor
            double p1 = -px + py - pa;
            double p2 = px + py + -pa;
            double p3 = -px + py + pa;
            double p4 = px + py + pa;
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
}

