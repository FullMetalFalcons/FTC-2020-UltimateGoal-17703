package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous (name = "Optimized Auto", group = "17703")
public class RobotOptimizedAuto extends LinearOpMode {

    int forwardTile;
    int strafeTile;

    @Override
    public void runOpMode() throws InterruptedException {
        MONTE robot = new MONTE();

        robot.init(hardwareMap);

        waitForStart();

        try {
            robot.driveToTick(forwardTile, .5f);
            robot.stopBot();
            robot.resetEncoders();
            robot.driveToTick(strafeTile, .5f);
        } catch (Exception ex){
            telemetry.addData("Path", "Exception Thrown");
        } finally {
            robot.stopBot();
            telemetry.addData("Path", "Complete");
            telemetry.update();
        }





    }
}
