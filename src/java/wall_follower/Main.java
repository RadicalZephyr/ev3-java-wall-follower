/* @(#)Main.java
 */
/**
 *
 *
 * @author <a href="mailto:shannog@CF405-17.cs.wwu.edu">Geoff Peter Shannon</a>
 */
package wall_follower;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.Button;
import lejos.hardware.LCD;

public class Main
{

    private EV3TouchSensor leftTouch;
    private EV3TouchSensor rightTouch;

    private EV3UltrasonicSensor distance;

    public static void main(String[] args) {
        Main current = new Main();
        Button.waitForAnyEvent();
        LCD.drawString("Hello, lein java world!", 1, 3);
    }

    public Main() {
        setupSensors();
    }

    void setupSensors() {
        leftTouch = new EV3TouchSensor(SensorPort.S1);
        rightTouch = new EV3TouchSensor(SensorPort.S4);

        distance = new EV3UltrasonicSensor(SensorPort.S3);
    }


}
