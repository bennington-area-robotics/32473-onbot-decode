package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "TeleOpMajorTom (Blocks to Java)")
public class TeleOpMajorTom extends LinearOpMode {

  private CRServo right_feeder;
  private CRServo left_feeder;
  private DcMotor launcher;
  private DcMotor left_drive;
  private DcMotor right_drive;
  private DcMotor collector;

  double driveFactor;
  String IDLE;
  String launchState;
  String SPIN_UP;
  int LAUNCHER_TARGET_VELOCITY;
  String LAUNCH;
  String LAUNCHING;
  int LAUNCHER_MIN_VELOCITY;
  ElapsedTime VelocityTimer;
  ElapsedTime launchTime;

  /**
   * Describe this function...
   */
  private void Run_Feeders() {
    if (launchState.equals(IDLE)) {
      if (gamepad1.dpad_up) {
        right_feeder.setDirection(CRServo.Direction.FORWARD);
        right_feeder.setPower(1);
        left_feeder.setDirection(CRServo.Direction.REVERSE);
        left_feeder.setPower(1);
      } else if (gamepad1.dpad_down) {
        right_feeder.setDirection(CRServo.Direction.REVERSE);
        right_feeder.setPower(1);
        left_feeder.setDirection(CRServo.Direction.FORWARD);
        left_feeder.setPower(1);
      } else {
        left_feeder.setPower(0);
        right_feeder.setPower(0);
      }
    }
  }

  /**
   * Describe this function...
   */
  private void initVariables() {
    driveFactor = 0.5;
    IDLE = "IDLE";
    SPIN_UP = "SPIN_UP";
    LAUNCH = "LAUNCH";
    LAUNCHING = "LAUNCHING";
    launchState = IDLE;
    LAUNCHER_TARGET_VELOCITY = 1300;
    LAUNCHER_MIN_VELOCITY = 1200;
    launchTime = new ElapsedTime();
    VelocityTimer = new ElapsedTime();
  }

  /**
   * Describe this function...
   */
  private void initMotors() {
    launcher.setDirection(DcMotor.Direction.FORWARD);
    left_drive.setDirection(DcMotor.Direction.REVERSE);
    right_drive.setDirection(DcMotor.Direction.FORWARD);
    left_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    right_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    collector.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    left_feeder.setDirection(CRServo.Direction.REVERSE);
    left_feeder.setPower(0);
    right_feeder.setDirection(CRServo.Direction.FORWARD);
    right_feeder.setPower(0);
    launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    left_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    left_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    right_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    right_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    collector.setPower(0);
    collector.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    collector.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    collector.setDirection(DcMotor.Direction.FORWARD);
    ((DcMotorEx) launcher).setVelocityPIDFCoefficients(300, 0, 0, 10);
  }

  /**
   * Describe this function...
   */
  private void arcadeDrive(double forward, double turn) {
    left_drive.setPower(forward + turn);
    right_drive.setPower(forward - turn);
  }

  /**
   * This file includes a teleop (driver-controlled) file for the goBILDA® StarterBot for the
   * 2025-2026 FIRST® Tech Challenge season DECODE™. It leverages a differential/Skid-Steer
   * system for robot mobility, one high-speed motor driving two "launcher wheels",
   * and two servos which feed that launcher. Likely the most niche concept we'll
   * leverage in this example is closed-loop motor velocity control. This control
   * method reads the current speed as reported by the motor's encoder and applies
   * a varying amount of power to reach, and then hold a target velocity. The FTC
   * SDK calls this control method "RUN_USING_ENCODER". This contrasts to the
   * default "RUN_WITHOUT_ENCODER" where you control the power applied to the
   * motor directly. Since the dynamics of a launcher wheel system varies greatly
   * from those of most other FTC mechanisms, we will also need to adjust the
   * "PIDF" coefficients with some that are a better fit for our application.
   */
  @Override
  public void runOpMode() {
    float rightStickX;
    float leftStickY;

    right_feeder = hardwareMap.get(CRServo.class, "right_feeder");
    left_feeder = hardwareMap.get(CRServo.class, "left_feeder");
    launcher = hardwareMap.get(DcMotor.class, "launcher");
    left_drive = hardwareMap.get(DcMotor.class, "left_drive");
    right_drive = hardwareMap.get(DcMotor.class, "right_drive");
    collector = hardwareMap.get(DcMotor.class, "collector");

    // Put initialization blocks here.
    initVariables();
    initMotors();
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        Velocity_Change();
        rightStickX = gamepad1.right_stick_x;
        leftStickY = gamepad1.left_stick_y;
        arcadeDrive(-leftStickY * driveFactor, rightStickX);
        if (gamepad1.left_bumper) {
          driveFactor = 1;
        } else if (gamepad1.right_bumper) {
          driveFactor = 0.25;
        } else {
          driveFactor = 0.5;
        }
        if (gamepad1.y) {
          ((DcMotorEx) launcher).setVelocity(LAUNCHER_TARGET_VELOCITY);
        } else if (gamepad1.a) {
          ((DcMotorEx) launcher).setVelocity(0);
        }
        if (gamepad1.start) {
          collector.setPower(0);
        }
        if (gamepad1.back) {
          collector.setPower(1);
        }
        Run_Feeders();
        launch(gamepad1.b);
        telemetry.addData("Launch State", launchState);
        telemetry.addData("Launcher Motor Velocity", ((DcMotorEx) launcher).getVelocity());
        telemetry.addData("Launch Time", launchTime);
        telemetry.addData("leftDrive Position", left_drive.getCurrentPosition());
        telemetry.addData("rightDrive Position", right_drive.getCurrentPosition());
        telemetry.addData("leftStickY", Double.parseDouble(JavaUtil.formatNumber(leftStickY, 2)));
        telemetry.addData("rightStickX", Double.parseDouble(JavaUtil.formatNumber(rightStickX, 2)));
        telemetry.addData("Launcher Target Velocity", LAUNCHER_TARGET_VELOCITY);
        telemetry.addData("Launcher Minimum Velocity", LAUNCHER_MIN_VELOCITY);
        telemetry.update();
      }
    }
  }

  /**
   * Describe this function...
   */
  private void launch(boolean shotRequested) {
    if (launchState.equals(IDLE)) {
      if (shotRequested == true) {
        launchState = SPIN_UP;
        right_feeder.setDirection(CRServo.Direction.FORWARD);
        left_feeder.setDirection(CRServo.Direction.REVERSE);
      }
    } else if (launchState.equals(SPIN_UP)) {
      ((DcMotorEx) launcher).setVelocity(LAUNCHER_TARGET_VELOCITY);
      if (((DcMotorEx) launcher).getVelocity() > LAUNCHER_MIN_VELOCITY) {
        launchState = LAUNCH;
      }
    } else if (launchState.equals(LAUNCH)) {
      left_feeder.setPower(1);
      right_feeder.setPower(1);
      launchTime.reset();
      launchState = LAUNCHING;
    } else if (launchState.equals(LAUNCHING)) {
      if (launchTime.seconds() > 0.75) {
        left_feeder.setPower(0);
        right_feeder.setPower(0);
        launchState = IDLE;
      }
    }
  }

  /**
   * Describe this function...
   */
  private void Velocity_Change() {
    if (VelocityTimer.time() > 0.25) {
      if (gamepad1.left_trigger == 1) {
        LAUNCHER_MIN_VELOCITY += 10;
        LAUNCHER_TARGET_VELOCITY += 10;
      } else if (gamepad1.right_trigger == 1) {
        LAUNCHER_MIN_VELOCITY += -10;
        LAUNCHER_TARGET_VELOCITY += -10;
      }
      VelocityTimer.reset();
    }
  }
}
