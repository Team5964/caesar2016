package org.usfirst.frc.team5964.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
    CameraServer server;
    Timer auto_time = new Timer();

    // user defined devices
    RobotDrive chassis = new RobotDrive(1, 2);
    Joystick joystick1 = new Joystick(0);
    Joystick joystick2 = new Joystick(1);
    Talon shoot = new Talon(4);
    Talon articulation = new Talon(3);
    Talon leg = new Talon(5);
    DigitalInput limit1 = new DigitalInput(1);
    boolean prev_button4;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        try {
            CameraServer server = CameraServer.getInstance();
            server.setQuality(45);
            server.startAutomaticCapture("cam1");
        } catch (Exception e) {

        }
        // code for camera set up and use
        chassis.setExpiration(1);
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString line to get the auto name from the text box below the Gyro
     *
     * You can add additional auto modes by adding additional comparisons to the
     * switch structure below with additional strings. If using the
     * SendableChooser make sure to add them to the chooser code above as well.
     */
    public void autonomousInit() {
        autoSelected = (String) chooser.getSelected();
        // autoSelected = SmartDashboard.getString("Auto Selector",
        // defaultAuto);
        System.out.println("Auto selected: " + autoSelected);
        auto_time.reset();
        auto_time.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        switch (autoSelected) {
        case customAuto:
            // Put custom auto code here
            break;
        case defaultAuto:
        default:
            // Put default auto code here
            chassis.setSafetyEnabled(false);
            if (auto_time.get() < 3) {
                chassis.drive(1, 0);
            } else {
                chassis.drive(0, 0);
            }

            break;
        }

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        // chassis.setSafetyEnabled(true);

        if (limit1.get() == false) {
            articulation.set(-0.15);
        }
        chassis.arcadeDrive(joystick1.getY() * 0.75, -joystick1.getX() * 0.80);

        double armSpeed = joystick2.getRawAxis(1);
        articulation.set(armSpeed * 0.2);
        // Allows the joystick to control the bot drive motors

        if (joystick2.getRawButton(4)) {
            shoot.set(1);

        } else if (!joystick2.getRawButton(4) && prev_button4) {
            leg.set(1);
        }
        prev_button4 = joystick2.getRawButton(4);
        // Start shoot motors

        if (joystick2.getRawButton(6)) {
            shoot.set(0);
            leg.set(0);
        }
        // Stop shoot motors

        if (joystick1.getRawButton(3)) {
            leg.set(-0.65);
            shoot.set(-0.65);
        }
        // Start ball gathering motor (reverse shoot)

        if (joystick1.getRawButton(5)) {
            leg.set(0);
            shoot.set(0);
        }
        // Stop ball gathering motor (reverse ball gathering motor)

    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}

