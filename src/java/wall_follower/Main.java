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


    private Map<Side, EV3TouchSensor> touches;

    private Map<Side, NXTRegulatedMotor> motors;

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
        Delay.msDelay(100);
    }

    public Main() {
        setupSensors();
        setupMotors();
    }

    void setupSensors() {
        touches = new EnumMap<Side, EV3TouchSensor>(Side.class);
        touches.put(Side.LEFT, new EV3TouchSensor(SensorPort.S1));
        touches.put(Side.RIGHT, new EV3TouchSensor(SensorPort.S4));

        color = new EV3ColorSensor(SensorPort.S2);
        distance = new EV3UltrasonicSensor(SensorPort.S3);
    }

    void setupMotors() {
        motors = new EnumMap<Side, NXTRegulatedMotor>(Side.class);
        motors.put(Side.LEFT, Motor.B);
        motors.put(Side.RIGHT, Motor.C);
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
        motors.get(Side.LEFT).setSpeed(200);
        motors.get(Side.RIGHT).setSpeed(200);
        motors.get(Side.LEFT).forward();
        motors.get(Side.RIGHT).forward();
    }

    void stopMotors() {
        for (NXTRegulatedMotor motor : motors.values()) {
            motor.stop();
            motor.flt();
        }
    }

    void mainLoop() {
        startMotors();

        Side followSide;
        // Forward until wall contact with one sensor.  Prefer left
        // sensor, by checking it first.
        boolean done = false;
        while (!done) {
            if (touches.get(Side.LEFT).isPressed()) {
                followSide = Side.LEFT;
                done = true;
            }
            if (touches.get(Side.RIGHT).isPressed()) {
                followSide = Side.RIGHT;
                done = true;
            }
        }
        stopMotors();
        followWall(followSide);
    }

    void followWall(Side followSide) {

    }
}
