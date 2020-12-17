package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp (group = "17703", name = "Shooter Only")
public class ShooterOnly extends LinearOpMode {

    DcMotor shooter;

    @Override
    public void runOpMode() {

        waitForStart();

        while (opModeIsActive()) {
            shooter = hardwareMap.dcMotor.get("shooter_motor");
            if (gamepad1.y) {
                shooter.setPower(1);
            } else {
                shooter.setPower(0);
            }
        }
    }
}
