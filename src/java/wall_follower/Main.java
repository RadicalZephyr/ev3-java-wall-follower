/* Program name: Main
     Description: A wall-following robot
     Interesting Features:

Hardware:
Port                  Sensor
1                      Touch
2                      Ultrasonic
4                      Touch

Port                Motor
B                    Left
C                    Right

Course Number: CSCI 372, Fall 2013
Student Name: Geoff Shannon
References: lejos
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

import lejos.robotics.SampleProvider;

import lejos.utility.Delay;

public class Main
{

    private EV3TouchSensor leftTouch;
    private EV3TouchSensor rightTouch;

    private NXTRegulatedMotor leftMotor;
    private NXTRegulatedMotor rightMotor;

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
        leftTouch = new EV3TouchSensor(SensorPort.S1);
        rightTouch = new EV3TouchSensor(SensorPort.S4);

        color = new EV3ColorSensor(SensorPort.S3);
        distance = new EV3UltrasonicSensor(SensorPort.S2);
        distance.enable();
    }

    void setupMotors() {
        leftMotor = Motor.B;
        rightMotor = Motor.C;
        leftMotor.setSpeed(200);
        rightMotor.setSpeed(200);
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
        leftMotor.forward();
        rightMotor.forward();
    }

    void stopMotors() {
        leftMotor.stop();
        rightMotor.stop();
        leftMotor.flt();
        rightMotor.flt();
    }

    void mainLoop() {
        startMotors();
        followWall();
    }

    float checkDistance(SampleProvider distanceSampler) {
        float[] distance = new float[1];
        distanceSampler.fetchSample(distance, 0);
        return distance[0];
    }

    void followWall() {

        boolean done = false;
        SampleProvider distanceSampler = distance.getDistanceMode();
        boolean touching;
        int speed;

        while (!done) {

            if (rightTouch.isPressed()) {
                stopMotors();
                rightMotor.backward();
                leftMotor.forward();
                Delay.msDelay(800);
                stopMotors();
                continue;
            }

            touching = leftTouch.isPressed();
            if (touching) {
                stopMotors();
                rightMotor.backward();
                Delay.msDelay(800);
                rightMotor.flt();
            } else if (!touching &&
                       checkDistance(distanceSampler) < 0.11) {
                speed = leftMotor.getSpeed();
                rightMotor.setSpeed(speed + 20);
            } else {
                speed = leftMotor.getSpeed();
                leftMotor.setSpeed(90);
                Delay.msDelay(1000);
                leftMotor.setSpeed(speed);
            }

            if (Button.readButtons() != 0) {
                done = true;
            }
            startMotors();
        }
    }
}
