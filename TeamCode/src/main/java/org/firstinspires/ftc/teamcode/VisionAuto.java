package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
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
@Autonomous(name = "Webcam Test", group = "17703")
//@Disabled
public class VisionAuto extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";

    int stackGenerator;

    private final double ENCODER_TICKS_PER_REVOLUTION = 1120;
    private final int ENCODER_TICKS_PER_TILE = -500;
    private final double ENCODER_90_TURN_LEFT = 2103;
    private final double ENCODER_90_TURN_RIGHT = -2104;
    private final double ENCODER_STRAFE_LEFT = 2080;
    private final double ENCODER_STRAFE_RIGHT = -2080;

    DcMotor backLeftMotor, frontLeftMotor, frontRightMotor, backRightMotor;
    Servo wristServo;
    DcMotorEx wobbleMotor, shooter, hopper, intake;

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
    private static final String VUFORIA_KEY =
            "AaE0cYT/////AAABmQA9EfxdE0nPnLBfBzgICwFuM0+4pL8yFpe+AfxfvUkzfy/u+lfdKve090kjx9rF3CjcaEWgi/SShmVX7gNbjpPYuzduVf/uN52ZKN2Ex9jjT/kIg6iDrwpjW6t4FAf78VPgOeMbc19LeAhjLurKvqUgfG6FoIGL5ou03s2LCGSER70le7KL8hqoh30jGwxWDo17PrVI4yL1ipHqjXaS8IZa4zuZvecrY9xNvdJawm4J2a5+td8v2Dq1jbuWSvJuGiaJjl4NnUhS/dv8Z9C0bN6s7ATfTq/4DsbBxZXF1JN5tofw59VOe67Z6KYmNIc+d5YrxQU6QhSp24poY5BzIPjFq8QIpK/FyhrEhN033v5/";

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

        backLeftMotor = hardwareMap.dcMotor.get("back_left_motor");
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
        wobbleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        wristServo = hardwareMap.servo.get("hand_servo");

        shooter = (DcMotorEx) hardwareMap.dcMotor.get("shooter_motor");
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        hopper = (DcMotorEx) hardwareMap.dcMotor.get("hopper_motor");

        intake = (DcMotorEx) hardwareMap.dcMotor.get("intake_motor");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);


        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {

            resetEnc();

            backLeftMotor.setTargetPosition(-500);
            frontLeftMotor.setTargetPosition(-500);
            frontRightMotor.setTargetPosition(-500);
            backRightMotor.setTargetPosition(-500);
            while ((frontRightMotor.getCurrentPosition() >= frontRightMotor.getTargetPosition()) && (frontLeftMotor.getCurrentPosition() >= frontLeftMotor.getTargetPosition())) {
                moveForward();
            }
            stopBot();
            sleep(100);
            resetEnc();
            backLeftMotor.setTargetPosition(-700);
            frontLeftMotor.setTargetPosition(700);
            frontRightMotor.setTargetPosition(-700);
            backRightMotor.setTargetPosition(700);
            while ((frontRightMotor.getCurrentPosition() >= frontRightMotor.getTargetPosition()) && (frontLeftMotor.getCurrentPosition() <= frontLeftMotor.getTargetPosition())) {
                strafeLeft();
            }
            stopBot();
            sleep(200);

            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() == 0) {
                            // empty list.  no objects recognized.
                            telemetry.addData("TFOD", "No items detected.");
                            telemetry.addData("Target Zone", "A");
                            stackGenerator = 0;
                            telemetry.addData("Stack Variable Value", stackGenerator);
                            //code for strafe

                            backLeftMotor.setTargetPosition(-500);
                            frontLeftMotor.setTargetPosition(500);
                            frontRightMotor.setTargetPosition(-500);
                            backRightMotor.setTargetPosition(500);
                            while ((frontRightMotor.getCurrentPosition() >= frontRightMotor.getTargetPosition()) && (frontLeftMotor.getCurrentPosition() <= frontLeftMotor.getTargetPosition())) {
                                strafeLeft();
                            }
                            resetEnc();
                            //Code for forward to A zone
                            backLeftMotor.setTargetPosition(ENCODER_TICKS_PER_TILE * 2);
                            frontLeftMotor.setTargetPosition(ENCODER_TICKS_PER_TILE * 2);
                            frontRightMotor.setTargetPosition(ENCODER_TICKS_PER_TILE * 2);
                            backRightMotor.setTargetPosition(ENCODER_TICKS_PER_TILE * 2);
                            while ((frontRightMotor.getCurrentPosition() >= frontRightMotor.getTargetPosition()) && (frontLeftMotor.getCurrentPosition() <= frontLeftMotor.getTargetPosition())) {
                                moveForward();
                            }
                            stopBot();

                        } else {
                            // list is not empty.
                            // step through the list of recognitions and display boundary info.
                            int i = 0;
                            for (Recognition recognition : updatedRecognitions) {
                                telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                                telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                        recognition.getLeft(), recognition.getTop());
                                telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                        recognition.getRight(), recognition.getBottom());

                                // check label to see which target zone to go after.

                                if (recognition.getLabel().equals("Single")) {
                                    telemetry.addData("Target Zone", "B");
                                    stackGenerator = 1;
                                    telemetry.addData("Stack Variable Value", stackGenerator);
                                    //code for strafe
                                    backLeftMotor.setTargetPosition(1000);
                                    frontLeftMotor.setTargetPosition(-1000);
                                    frontRightMotor.setTargetPosition(1000);
                                    backRightMotor.setTargetPosition(-1000);
                                    while ((frontRightMotor.getCurrentPosition() <= frontRightMotor.getTargetPosition()) && (frontLeftMotor.getCurrentPosition() >= frontLeftMotor.getTargetPosition())) {
                                        strafeRight();
                                    }
                                    resetEnc();
                                    //Code for forward to B zone
                                    backLeftMotor.setTargetPosition(ENCODER_TICKS_PER_TILE * 3);
                                    frontLeftMotor.setTargetPosition(ENCODER_TICKS_PER_TILE * 3);
                                    frontRightMotor.setTargetPosition(ENCODER_TICKS_PER_TILE * 3);
                                    backRightMotor.setTargetPosition(ENCODER_TICKS_PER_TILE * 3);
                                    while ((frontRightMotor.getCurrentPosition() >= frontRightMotor.getTargetPosition()) && (frontLeftMotor.getCurrentPosition() >= frontLeftMotor.getTargetPosition())) {
                                        moveForward();
                                    }
                                    stopBot();


                                }

                                //This is the code for a stack of 4
                                else if (recognition.getLabel().equals("Quad")) {
                                    telemetry.addData("Target Zone", "C");
                                    stackGenerator = 2;
                                    telemetry.addData("Stack Variable Value", stackGenerator);
                                    //code for strafe
                                 /*   strafeLeft();
                                    sleep(500);
                                    moveForward();
                                    sleep(1500);
                                    stopBot(); */
                                    backLeftMotor.setTargetPosition(-500);
                                    frontLeftMotor.setTargetPosition(500);
                                    frontRightMotor.setTargetPosition(-500);
                                    backRightMotor.setTargetPosition(500);
                                    while ((frontRightMotor.getCurrentPosition() >= frontRightMotor.getTargetPosition()) && (frontLeftMotor.getCurrentPosition() <= frontLeftMotor.getTargetPosition())) {
                                        strafeLeft();
                                    }
                                    resetEnc();
                                    //Code for forward to C zone
                                    backLeftMotor.setTargetPosition(ENCODER_TICKS_PER_TILE * 4);
                                    frontLeftMotor.setTargetPosition(ENCODER_TICKS_PER_TILE * 4);
                                    frontRightMotor.setTargetPosition(ENCODER_TICKS_PER_TILE * 4);
                                    backRightMotor.setTargetPosition(ENCODER_TICKS_PER_TILE * 4);
                                    while ((frontRightMotor.getCurrentPosition() >= frontRightMotor.getTargetPosition()) && (frontLeftMotor.getCurrentPosition() >= frontLeftMotor.getTargetPosition())) {
                                        moveForward();
                                    }
                                    stopBot();


                                } else {
                                    telemetry.addData("Target Zone", "UNKNOWN");
                                }



                            }

                        }
                        telemetry.update();
                    }
                }
            }
        }
        stopBot();
        if (tfod != null) {
            tfod.shutdown();
        }
    }



            /*if (stackGenerator == 0) {
                while (frontLeftMotor.getCurrentPosition() <= ENCODER_STRAFE_LEFT + 100) {
                    strafeLeft();
                }
                resetEnc();
                while (frontLeftMotor.getCurrentPosition() >= 1.8 * ENCODER_TICKS_PER_TILE) {
                    moveForward();
                }
                stopBot();
            }

            //Code for B Square
            if (stackGenerator == 1) {
                while (frontLeftMotor.getCurrentPosition() >= (ENCODER_STRAFE_RIGHT / 2) + 100) {
                    strafeRight();
                }
                resetEnc();
                while (frontLeftMotor.getCurrentPosition() >= 2.8 * ENCODER_TICKS_PER_TILE) {
                    moveForward();
                }
                resetEnc();
                while (frontLeftMotor.getCurrentPosition() <= ENCODER_STRAFE_LEFT / 2) {
                    strafeLeft();
                }
                stopBot();
            }

            //Code for C Square
            if (stackGenerator == 2) {
                while (frontLeftMotor.getCurrentPosition() <= ENCODER_STRAFE_LEFT + 100) {
                    strafeLeft();
                }
                resetEnc();
                while (frontLeftMotor.getCurrentPosition() >= 3.8 * ENCODER_TICKS_PER_TILE) {
                    moveForward();
                }
                stopBot();
            }*/






    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        //Sets the camera name
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam");

        //  Instantiate the Vuforia engine
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
        backLeftMotor.setPower(p1);
        frontLeftMotor.setPower(p2);
        frontRightMotor.setPower(p3);
        backRightMotor.setPower(p4);
    }

    void stopBot() {
        setPower(0, 0, 0);
    }
    void moveForward() {
        setPower(0, -.25f, 0);
    }
    void moveBackward() {
        setPower(0, .25f, 0);
    }
    void strafeLeft() {
        setPower(.25f, 0, 0);
    }
    void strafeRight() {
        setPower(-.25f, 0, 0);
    }
    void turnLeft() {
        setPower(0, 0, .25f);
    }
    void turnRight() {
        setPower(0, 0, -.25f);
    }
    public void resetEnc() {
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}

