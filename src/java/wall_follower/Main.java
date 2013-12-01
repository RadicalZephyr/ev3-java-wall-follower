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
import lejos.hardware.Button;
import lejos.hardware.LCD;

public class Main
{

    private EV3TouchSensor leftTouch;
    private EV3TouchSensor rightTouch;

    private EV3ColorSensor color;
    private EV3UltrasonicSensor distance;

    public static void main(String[] args) {
        Main current = new Main();
        boolean done = false;

        while (!done) {
            current.printSensors();
            if (Button.waitForAnyPress(100) != 0) {
                done = true;
            }
        }
    }

    public Main() {
        setupSensors();
    }

    void setupSensors() {
        leftTouch = new EV3TouchSensor(SensorPort.S1);
        rightTouch = new EV3TouchSensor(SensorPort.S4);

        color = new EV3ColorSensor(SensorPort.S2);
        distance = new EV3UltrasonicSensor(SensorPort.S3);
    }

    void printSensors() {
        LCD.clear();
        LCD.drawString("Printing sensor data:", 0, 0);
    }
}
