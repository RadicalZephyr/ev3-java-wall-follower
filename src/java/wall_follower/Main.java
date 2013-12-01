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

import java.util.Map;
import java.util.EnumMap;

public class Main
{

    public enum Side {
        LEFT, RIGHT;
    }


    private Map<Side, EV3TouchSensor> touch;

    private Map<Side, NXTRegulatedMotor> motor;

    private EV3ColorSensor color;
    private EV3UltrasonicSensor distance;

    public static void main(String[] args) {
        Main current = new Main();
        boolean done = false;
        LCD.clear();
        current.promptForStartPush();
        current.mainLoop();
        LCD.clear();
        LCD.drawString("Done!", 1, 1);
        Delay.msDelay(1000);
    }

    public Main() {
        setupSensors();
        setupMotors();
    }

    void setupSensors() {
        touch = new EnumMap<Side, EV3TouchSensor>(Side.class);
        touch.put(Side.LEFT, new EV3TouchSensor(SensorPort.S1));
        touch.put(Side.RIGHT, new EV3TouchSensor(SensorPort.S4));

        color = new EV3ColorSensor(SensorPort.S2);
        distance = new EV3UltrasonicSensor(SensorPort.S3);
    }

    void setupMotors() {
        motor = new EnumMap<Side, NXTRegulatedMotor>(Side.class);
        motor.put(Side.LEFT, Motor.B);
        motor.put(Side.RIGHT, Motor.C);
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
        NXTRegulatedMotor left = motor.get(Side.LEFT);
        NXTRegulatedMotor right = motor.get(Side.RIGHT);
        left.setSpeed(200);
        right.setSpeed(200);
        left.forward();
        right.forward();
    }

    void stopMotors() {
        NXTRegulatedMotor left = motor.get(Side.LEFT);
        NXTRegulatedMotor right = motor.get(Side.RIGHT);
        left.stop();
        right.stop();
        left.flt();
        right.flt();
    }

    void mainLoop() {
        startMotors();

        Side followSide = Side.LEFT;
        Side offSide = Side.RIGHT;
        // Forward until wall contact with one sensor.  Prefer left
        // sensor, by checking it first.
        boolean done = false;
        while (!done) {
            if (touch.get(Side.LEFT).isPressed()) {
                followSide = Side.LEFT;
                offSide = Side.RIGHT;
                done = true;
            }
            if (touch.get(Side.RIGHT).isPressed()) {
                followSide = Side.RIGHT;
                offSide = Side.LEFT;
                done = true;
            }
        }
        followWall(followSide, offSide);
    }

    void followWall(Side followSide, Side offSide) {

    }
}
