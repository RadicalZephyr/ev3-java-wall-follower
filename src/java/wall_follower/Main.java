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
        current.startMotors();
        while (!done) {
            current.mainMove();
            if (Button.waitForAnyPress(100) != 0) {
                done = true;
            }
        }
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

    void startMotors() {
        leftMotor.setSpeed(100);
        rightMotor.setSpeed(100);
        leftMotor.forward();
        rightMotor.forward();
    }

    void mainMove() {
        if (leftTouch.isPressed()) {
            leftMotor.rotate(180);
        }
        if (rightTouch.isPressed()) {
            rightMotor.rotate(180);
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
