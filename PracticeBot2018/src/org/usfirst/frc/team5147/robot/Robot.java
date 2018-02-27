/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5147.robot;



import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SPI;

		
public class Robot extends IterativeRobot {
	
	
	
	/* Defines the autonomous chooser */
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();

	
	/* Defines the Drivetrain */
	WPI_TalonSRX frontRight = new WPI_TalonSRX(0);
	WPI_TalonSRX backRight = new WPI_TalonSRX(1);
	WPI_TalonSRX frontLeft = new WPI_TalonSRX(2);
	WPI_TalonSRX backLeft = new WPI_TalonSRX(3);

	DifferentialDrive _drive = new DifferentialDrive(frontLeft, frontRight);
	
	
	
	/* Defines Operator console */
	Joystick driverJoy = new Joystick(0);
	boolean [] _joystickButtons = {false,false,false,false,false,false,false,false,false,false,false,false};
	
	Joystick operatorBoard = new Joystick(1);
	boolean [] _operatorBoardButtons = {false,false,false,false,false,false,false,false};
	
	Joystick operatorJoy = new Joystick(2);
	boolean [] _operatorJoystickButtons = {false,false,false,false,false,false,false,false};
	
	/* Defines Pneumatic Solenoids */
	Solenoid armClamperino = new Solenoid(0);
	
	
	
	/* Defines the Cube Elevator Talon */
	WPI_TalonSRX cubeElevatorTalon = new WPI_TalonSRX(4);
	
	
	
	/* Defines the arm extender Talon */
	WPI_TalonSRX armExtenderTalon = new WPI_TalonSRX(5);

	
	
	/* Defines the climber Talon */
	WPI_TalonSRX climberTalon = new WPI_TalonSRX(6);


	
	/* Defines sensors */
	Timer autoTimer = new Timer();
	DigitalInput bottomLimit = new DigitalInput(0);
	DigitalInput topLimit = new DigitalInput(3);
	AHRS gyroMXP = new AHRS(SPI.Port.kMXP);
	private String gameData;


	
	
	@Override
	public void robotInit() {
		
	/* Sensors */
		gyroMXP.reset();

		
	/* Defines the autonomous chooser */
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		
		
	/* Sets the back motors to slave for the front motors */
		backRight.follow(frontRight);
		backLeft.follow(frontLeft);
		
		
		
	/* Starts the Camera Server */
		UsbCamera mainCamera = CameraServer.getInstance().startAutomaticCapture();
		mainCamera.setResolution(640, 480);
		
	}

	
	@Override
	public void autonomousInit() {
		
		
		
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);	
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		
		
	}

	
	@Override
	public void autonomousPeriodic() {
		
		
		
		/* The following code confirms that we have the proper switch locations */
			while (gameData.isEmpty() == true) {
				gameData = DriverStation.getInstance().getGameSpecificMessage();
			}
		
	
		
		
		
		switch (m_autoSelected) {
			case kCustomAuto:
				_drive.setSafetyEnabled(false);
				_drive.arcadeDrive(.75, 0);
				Timer.delay(2);
				_drive.arcadeDrive(0, 0);
				break;
			case kDefaultAuto:
			default:
				/* Default Auto, which does nothing, for safety */
				break;
		}
	}

	
	@Override
	public void teleopPeriodic() {
		
		

		
		
		
		/* The following lines dicates how the robot is driven */
		double forward = driverJoy.getY();
    	double turn = driverJoy.getZ();
    	double driveSpeedMultiplier = 1;
    
    	
    	
    	
    	/* The Following lines dictates how the multiplier is set by the third Axis */
    	if (driverJoy.getRawAxis(3) <= 1 && driverJoy.getRawAxis(3) >=0.8) {
    		driveSpeedMultiplier = 0.1;
    	}else if(driverJoy.getRawAxis(3) <= 0.8 && driverJoy.getRawAxis(3) >= 0.6) {
    		driveSpeedMultiplier = 0.2;
    	}else if(driverJoy.getRawAxis(3) <= 0.6 && driverJoy.getRawAxis(3) >= 0.4) {
    		driveSpeedMultiplier = 0.3;
    	}else if(driverJoy.getRawAxis(3) <= 0.4 && driverJoy.getRawAxis(3) >= 0.2) {
    		driveSpeedMultiplier = 0.4;
    	}else if(driverJoy.getRawAxis(3) <= 0.2 && driverJoy.getRawAxis(3) >= 0) {
    		driveSpeedMultiplier = 0.5;
    	}else if(driverJoy.getRawAxis(3) <= 0 && driverJoy.getRawAxis(3) >= -0.2) {
    		driveSpeedMultiplier = 0.6;
    	}else if(driverJoy.getRawAxis(3) <= -0.2 && driverJoy.getRawAxis(3) >= -0.4) {
    		driveSpeedMultiplier = 0.7;
    	}else if(driverJoy.getRawAxis(3) <= -0.4 && driverJoy.getRawAxis(3) >= -0.6) {
    		driveSpeedMultiplier = 0.8;
    	}else if(driverJoy.getRawAxis(3) <= -0.6 && driverJoy.getRawAxis(3) >= -0.8) {
    		driveSpeedMultiplier = 0.9;
    	}else if(driverJoy.getRawAxis(3) <= -0.8 && driverJoy.getRawAxis(3) >= -1) {
    		driveSpeedMultiplier = 1.0;
    	}else {
    		driveSpeedMultiplier = 1.0;
    	}
    	
    	_drive.arcadeDrive(forward * -driveSpeedMultiplier, turn * driveSpeedMultiplier);
    	
    	
    	
    	
        /* Defines Buttons */
    	boolean [] joyBtns= new boolean [_joystickButtons.length];
		for(int i=1;i<_joystickButtons.length;++i)
			joyBtns[i] = driverJoy.getRawButton(i); 

		boolean [] operatorBtns= new boolean [_operatorBoardButtons.length];
		for(int i=1;i<_operatorBoardButtons.length;++i)
			operatorBtns[i] = operatorJoy.getRawButton(i); 
		
		
		if (operatorBtns[2] == true) {
			armClamperino.set(true);
		} else if (operatorBtns[1] == true) {
			armClamperino.set(false);
		}
		
		
		
		
		if (operatorBtns[3] == true && bottomLimit.get() == true) {
			cubeElevatorTalon.set(1);
		}else if (operatorBtns[4] == true && topLimit.get() == true) {
			cubeElevatorTalon.set(-1);
		}else {
			cubeElevatorTalon.set(0);
		}
		
		
		
		
		if (operatorBtns[6] == true) {
			armExtenderTalon.set(1);
		}else if (operatorBtns[5] == true) {
			armExtenderTalon.set(-1);
		}else {
			armExtenderTalon.set(0);
		}
		
		
		if (joyBtns[3] == true) {
			climberTalon.set(-1);
		}else if (joyBtns[4] == true) {
			climberTalon.set(1);
		}else {
			climberTalon.set(0);
		}
		
	}

	
	@Override
	public void testPeriodic() {
		
		
	}

	
	@Override
	public void robotPeriodic() {
		Timer.delay(0.020);
		SmartDashboard.putNumber("Gyro Roll:", gyroMXP.getRoll());
		SmartDashboard.putNumber("Gyro Pitch:", gyroMXP.getPitch());
		SmartDashboard.putNumber("Gyro Yaw:", gyroMXP.getYaw());
		SmartDashboard.putBoolean("Gyro Connected?", gyroMXP.isConnected());
		SmartDashboard.putBoolean("Gyro is calibarting?", gyroMXP.isCalibrating());
		SmartDashboard.putNumber("Stuff", gyroMXP.getAngle());
	}
}