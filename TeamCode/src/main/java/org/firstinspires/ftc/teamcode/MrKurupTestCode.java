/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

/**
 * This 2020-2021 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Ultimate Goal game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous(name = "Working Vision Encoder Navigation Meet2", group = "FMF")
//@Disabled
public class MrKurupTestCode extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";

    DcMotor backLeftMotor, frontLeftMotor, frontRightMotor, backRightMotor;
    Servo wristServo, wristServoAuto;
    DcMotorEx wobbleMotor, shooter, hopper, intake;
    int strafeTile = -900;
    int forwardTile = -750;
    int dropPosition;

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY = "AaE0cYT/////AAABmQA9EfxdE0nPnLBfBzgICwFuM0+4pL8yFpe+AfxfvUkzfy/u+lfdKve090kjx9rF3CjcaEWgi/SShmVX7gNbjpPYuzduVf/uN52ZKN2Ex9jjT/kIg6iDrwpjW6t4FAf78VPgOeMbc19LeAhjLurKvqUgfG6FoIGL5ou03s2LCGSER70le7KL8hqoh30jGwxWDo17PrVI4yL1ipHqjXaS8IZa4zuZvecrY9xNvdJawm4J2a5+td8v2Dq1jbuWSvJuGiaJjl4NnUhS/dv8Z9C0bN6s7ATfTq/4DsbBxZXF1JN5tofw59VOe67Z6KYmNIc+d5YrxQU6QhSp24poY5BzIPjFq8QIpK/FyhrEhN033v5/";


    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {

        backLeftMotor= hardwareMap.dcMotor.get("back_left_motor");
        frontLeftMotor = hardwareMap.dcMotor.get("front_left_motor");
        frontRightMotor = hardwareMap.dcMotor.get("front_right_motor");
        backRightMotor = hardwareMap.dcMotor.get("back_right_motor");

        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        backLeftMotor.setTargetPosition(0);
        frontLeftMotor.setTargetPosition(0);
        frontRightMotor.setTargetPosition(0);
        backRightMotor.setTargetPosition(0);

        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        wobbleMotor = (DcMotorEx) hardwareMap.dcMotor.get("arm_motor");
        //Because we want the wobble motor to only rotate down, the mode will need to run to a certain position (90 degrees = wobbleEncoderMax)
        wobbleMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        wobbleMotor.setTargetPosition(0);
        wobbleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        wristServo = hardwareMap.servo.get("hand_servo");
        wristServoAuto = hardwareMap.servo.get("hand_servo_auto");

        shooter = (DcMotorEx) hardwareMap.dcMotor.get("shooter_motor");
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        hopper = (DcMotorEx) hardwareMap.dcMotor.get("hopper_motor");

        intake = (DcMotorEx) hardwareMap.dcMotor.get("intake_motor");
        //intake.setDirection(DcMotorSimple.Direction.REVERSE);

        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        initTfod();

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 1.78 or 16/9).

            // Uncomment the following line if you want to adjust the magnification and/or the aspect ratio of the input images.
            //tfod.setZoom(2.5, 1.78);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            if (tfod != null) {
                telemetry.addData("Robot Status", "Tighten the wrist to secure the wobble");
                telemetry.update();
                sleep(100);
                wristServoAuto.setPosition(0.5);

                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    if (updatedRecognitions.size() == 0) {
                        // empty list.  no objects recognized.
                        telemetry.addData("TFOD", "No items detected.");
                        telemetry.addData("Target Zone", "A");
                        telemetry.update();

                        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        setTargetPos(forwardTile*2, false, false, false, false);
                        setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        moveForward();
                        while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                            telemetry.addData("Robot Status", "Moving Forward to Target A");
                            telemetry.update();
                        }
                        stopBot();

                        telemetry.addData("Robot Status", "Turn to drop wobble at Target A");
                        telemetry.update();
                        sleep(200);
                        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        turnLeft();
                        sleep(100);
                        stopBot();


                            /*dropWobble();
                            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                           moveForward();
                            sleep(150);
                            stopBot();
                            sleep(200);
                            raiseWobble();*/
                        telemetry.addData("Robot Status", "Target A: Opened wrist to drop wobble");
                        telemetry.update();
                        sleep(200);
                        wristServoAuto.setPosition(0);


                        telemetry.addData("Robot Status", "Target A: Turn back right before Strafing");
                        telemetry.update();
                        sleep(200);
                        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        turnRight();
                        sleep(100);
                        stopBot();


                        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        setTargetPos(2*strafeTile, true, false, false, false);
                        setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        strafeRight();
                        while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                            telemetry.addData("Robot Status", "Target A: Strafing Right to Shooting Position");
                            telemetry.update();
                        }

                        telemetry.addData("Robot Status", "Target A: Turn to finalize Shooting Position");
                        telemetry.update();
                        sleep(200);
                        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        turnRight();
                        sleep(125);
                        stopBot();

                        shootDisc();
                        sleep(500);
                        shootDisc();
                        while (shooter.getVelocity() > 1000) {
                            stopBot();
                            telemetry.addData("Status", "Target A: Shooting");
                            telemetry.update();
                        }

                        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        setTargetPos(-200, false, false, false, false);
                        setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        moveForward();
                        while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                            telemetry.addData("Robot Status", "Target A: Moving Front to Park");
                            telemetry.update();
                        }
                        stopBot();
                        telemetry.addData("Robot Status", "Target A: Parked");
                        telemetry.update();

                    } else {
                        // step through the list of recognitions and display boundary info.
                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                    recognition.getLeft(), recognition.getTop());
                            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                    recognition.getRight(), recognition.getBottom());

                            if (recognition.getLabel().equals("Single")) {
                                telemetry.addData("Target Zone", "B");
                                telemetry.update();

                                setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                                setTargetPos(3*forwardTile, false, false, false, false);
                                setMode(DcMotor.RunMode.RUN_TO_POSITION);
                                moveForward();
                                while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                                    telemetry.addData("Robot Status", "Target B: Moving Forward");
                                    telemetry.addData("Wobble Encoder Value", wobbleMotor.getCurrentPosition());
                                    telemetry.update();
                                }
                                stopBot();

                                setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                                setTargetPos(strafeTile, false, false, true, false);
                                setMode(DcMotor.RunMode.RUN_TO_POSITION);
                                strafeRight();
                                while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                                    telemetry.addData("Robot Status", "Strafing Right to Target B");
                                    telemetry.update();
                                }
                                stopBot();
                                sleep(100);

                                //Drop wobble here
                                    /*dropWobble();
                                    setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                                    moveForward();
                                    sleep(150);
                                    stopBot();
                                    sleep(200);
                                    raiseWobble();*/
                                telemetry.addData("Robot Status", "Target B: Opened wrist to drop wobble");
                                telemetry.update();
                                sleep(200);
                                wristServoAuto.setPosition(0);

                                setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                                setTargetPos(100, false, false, false, false);
                                setMode(DcMotor.RunMode.RUN_TO_POSITION);
                                moveBackward();
                                while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                                    telemetry.addData("Robot Status", "Target B: Moving Backward to Shooting position");
                                    telemetry.update();
                                }
                                stopBot();

                                setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                                turnRight();
                                sleep(125);
                                stopBot();
                                while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                                    telemetry.addData("Robot Status", "Target B: Turn to finalize Shooting Position");
                                    telemetry.update();
                                }

                                shootDisc();
                                sleep(500);
                                shootDisc();
                                while (shooter.getVelocity() > 1000) {
                                    stopBot();
                                    telemetry.addData("Status", "Target B: Shooting");
                                    telemetry.update();
                                }

                                setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                                setTargetPos(-200, false, false, false, false);
                                setMode(DcMotor.RunMode.RUN_TO_POSITION);
                                moveForward();
                                while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                                    telemetry.addData("Robot Status", "Target B: Moving Front to Park");
                                    telemetry.update();
                                }
                                stopBot();
                                telemetry.addData("Robot Status", "Target B: Parked");
                                telemetry.update();

                            } else if (recognition.getLabel().equals("Quad")) {
                                telemetry.addData("Target Zone", "C");
                                telemetry.update();

                                setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                                setTargetPos((int) (forwardTile*5), false, false, false, false);
                                setMode(DcMotor.RunMode.RUN_TO_POSITION);
                                moveForward();
                                while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                                    telemetry.addData("Robot Status", "Moving Forward to Target C");
                                    telemetry.update();
                                }
                                stopBot();

                                setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                                turnLeft();
                                sleep(100);
                                stopBot();
                                while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                                    telemetry.addData("Robot Status", "Turn to drop wobble at Target C");
                                    telemetry.update();
                                }

                                //Drop wobble here
                                    /*dropWobble();
                                    setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                                    moveForward();
                                    sleep(150);
                                    stopBot();
                                    sleep(200);
                                    raiseWobble();*/
                                telemetry.addData("Robot Status", "Target C: Opened wrist to drop wobble");
                                telemetry.update();
                                sleep(200);
                                wristServoAuto.setPosition(0);

                                setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                                setTargetPos(forwardTile*-2, false, false, false, false);
                                setMode(DcMotor.RunMode.RUN_TO_POSITION);
                                moveBackward();
                                while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                                    telemetry.addData("Robot Status", "Target C: Moving Backward to Shooting Position");
                                    telemetry.update();
                                }
                                stopBot();

                                setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                                setTargetPos(2*strafeTile, true, false, false, false);
                                setMode(DcMotor.RunMode.RUN_TO_POSITION);
                                strafeRight();
                                while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                                    telemetry.addData("Robot Status", "Target C: Strafing Right to Shooting Position");
                                    telemetry.update();
                                }
                                stopBot();

                                setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                                turnRight();
                                sleep(125);
                                stopBot();
                                while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                                    telemetry.addData("Robot Status", "Target C: Turn to finalize Shooting Position");
                                    telemetry.update();
                                }

                                shootDisc();
                                sleep(500);
                                shootDisc();
                                while (shooter.getVelocity() > 1000) {
                                    stopBot();
                                    telemetry.addData("Status", "Target C: Shooting");
                                    telemetry.update();
                                }

                                setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                                setTargetPos(-200, false, false, false, false);
                                setMode(DcMotor.RunMode.RUN_TO_POSITION);
                                moveForward();
                                while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                                    telemetry.addData("Robot Status", "Target C: Moving Front to Park");
                                    telemetry.update();
                                }
                                stopBot();
                                telemetry.addData("Robot Status", "Target C: Parked");
                                telemetry.update();

                            } else {
                                telemetry.addData("Target Zone", "UNKNOWN");
                            }
                        }
                    }
                    //telemetry.update();
                }
            }
            /*shootDisc();
            sleep(500);
            shootDisc();
            while (shooter.getVelocity() > 1000) {
                stopBot();
                telemetry.addData("Status", "Shooting");
                telemetry.update();
            }

            setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            setTargetPos(-200, false, false, false, false);
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            moveForward();
            while (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy()) {
                telemetry.addData("Robot Status", "Moving Front to Park");
                telemetry.update();
            }
            stopBot();
            telemetry.addData("Robot Status", "Parked");
            telemetry.update();*/
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }


    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
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
        if (isTurningLeft == true) {
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

    /*void dropWobble() {
        wobbleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Will have to revise this with accurate sign value
        wobbleMotor.setTargetPosition(-1600);
        wobbleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wobbleMotor.setPower(-.15);

        while (wobbleMotor.isBusy()) {
            telemetry.addData("Status", "Wobble Lowering");
            telemetry.addData("Wobble Target Position", wobbleMotor.getTargetPosition());
            telemetry.addData("Power", wobbleMotor.getPower());
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
            telemetry.addData("Wobble Target Position", wobbleMotor.getTargetPosition());
            telemetry.addData("Power", wobbleMotor.getPower());
            telemetry.update();
        }
        wobbleMotor.setPower(0);
        wristServo.setPosition(.9);
    }*/

    void setMode(DcMotor.RunMode mode) {
        frontRightMotor.setMode(mode);
        frontLeftMotor.setMode(mode);
        backRightMotor.setMode(mode);
        backLeftMotor.setMode(mode);
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
}