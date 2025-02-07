package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RamseteCommand;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.RamseteController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.RevIMU;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.DifferentialDriveKinematics;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

@Autonomous(name = "robot")
public class AutonomousFrenzy extends CommandOpMode {

    private Motor frontLeft, backLeft, frontRight, backRight;
    private MotorGroupTemp leftDrive, rightDrive;

    private RevIMU imu;

    private DifferentialDriveKinematics driveKinematics;
    private DriveSubsystem driveSubsystem;
    private RamseteCommand ramseteCommand;

    @Override
    public void initialize() {
        frontLeft = new Motor(hardwareMap, "fL");
        backLeft = new Motor(hardwareMap, "bL");
        frontRight = new Motor(hardwareMap, "fR");
        backRight = new Motor(hardwareMap, "bR");

        leftDrive = new MotorGroupTemp(frontLeft, backLeft);
        rightDrive = new MotorGroupTemp(frontRight, backRight);
        rightDrive.setInverted(true);
        
        imu = new RevIMU(hardwareMap);
        imu.init();
        driveKinematics = new DifferentialDriveKinematics(DriveConstants.TRACK_WIDTH);

        driveSubsystem = new DriveSubsystem(leftDrive, rightDrive, imu);
        ramseteCommand = new RamseteCommand(TestTrajectory.generateTrajectory(), driveSubsystem::getPose,
                new RamseteController(DriveConstants.B, DriveConstants.ZETA),
                new SimpleMotorFeedforward(DriveConstants.kS, DriveConstants.kV, DriveConstants.kA),
                driveKinematics, driveSubsystem::getWheelSpeeds,
                new PIDController(DriveConstants.kP, DriveConstants.kI, DriveConstants.kD),
                new PIDController(DriveConstants.kP, DriveConstants.kI, DriveConstants.kD),
                driveSubsystem::driveAuton);

        schedule(ramseteCommand.andThen(new InstantCommand(() -> driveSubsystem.driveAuton(0, 0))));
    }

}
