/* @(#)Main.java
 */
/**
 *
 *
 * @author <a href="mailto:shannog@CF405-17.cs.wwu.edu">Geoff Peter Shannon</a>
 */
package wall_follower;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;

import lejos.hardware.Button;
import lejos.hardware.LCD;
import lejos.hardware.Sound;

import lejos.utility.Delay;

public class Main
{

    private EV3TouchSensor leftTouch;
    private EV3TouchSensor rightTouch;

    private EV3ColorSensor color;
    private EV3UltrasonicSensor distance;

    private NXTRegulatedMotor leftMotor;
    private NXTRegulatedMotor rightMotor;

    public static void main(String[] args) {
        Main current = new Main();
        boolean done = false;
        LCD.clear();
        current.promptForStartPush();
        current.startMotors();
        while (!done) {
            current.mainMove();
            if (Button.waitForAnyPress(100) != 0) {
                done = true;
            }
        }
        LCD.clear();
        LCD.drawString("Done!", 1, 1);
        Delay.msDelay(100);
    }

    public Main() {
        setupSensors();
        setupMotors();
    }

    void setupSensors() {
        leftTouch = new EV3TouchSensor(SensorPort.S1);
        rightTouch = new EV3TouchSensor(SensorPort.S4);

        color = new EV3ColorSensor(SensorPort.S2);
        distance = new EV3UltrasonicSensor(SensorPort.S3);
    }

    void setupMotors() {
        leftMotor = Motor.B;
        rightMotor = Motor.C;
    }

    void promptForStartPush() {
        Sound.twoBeeps();
        Sound.beep();
        LCD.clear();
        LCD.drawString("Please push any", 1, 1);
        LCD.drawString("button to begin.", 1, 3);
        Button.waitForAnyPress();
        LCD.clear();
        LCD.drawString("Running...", 1, 1);
    }

    void startMotors() {
        leftMotor.setSpeed(200);
        rightMotor.setSpeed(200);
        leftMotor.forward();
        rightMotor.forward();
    }

    void mainMove() {
        if (leftTouch.isPressed()) {
            int speed = rightMotor.getSpeed();
            leftMotor.setSpeed(speed*2);
            Delay.msDelay(1000);
            leftMotor.setSpeed(speed);
        }
        if (rightTouch.isPressed()) {
            int speed = rightMotor.getSpeed();
            rightMotor.setSpeed(speed*2);
            Delay.msDelay(1000);
            rightMotor.setSpeed(speed);
        }
    }

    void printSensors() {
        int maxLen = 18;
        int line = 0;
        LCD.clear();
        LCD.drawString("Printing data:", 0, line++);

        LCD.drawString(String.format("L= %b ",
                                     leftTouch.isPressed()),
                       0, line++);
        LCD.drawString(String.format("R= %b ",
                                     rightTouch.isPressed()),
                       0, line++);


    }
}
