/* @(#)Move.java
 */
/**
 *
 *
 * @author <a href="mailto:geoff@minty-dark-tower">Geoff Shannon</a>
 */
package wall_follower;

import java.lang.Comparable;

import java.util.Random;

public class Move  implements Comparable<Move> {

    // In ??? units.  Meters???
    public static final float DISTANCE_RANGE = 5.0f;

    // In degrees per second
    public static final int MIN_MOTOR_SPEED = 50;
    public static final int MAX_MOTOR_SPEED = 300;

    // In milliseconds
    public static final int MIN_DURATION = 500;
    public static final int MAX_DURATION = 2000;


    float minDistance;
    // maxDistance is a constant value larger than minDistance
    boolean leftPressed;
    boolean rightPressed;

    int leftSpeed;
    int rightSpeed;
    int duration;

    MoveFitness fitness;

    public Move(SensorReading r) {
        minDistance = r.distance;
        leftPressed = r.leftPressed;
        rightPressed = r.rightPressed;

        leftSpeed = 0;
        rightSpeed = 0;
        duration = 0;

        fitness = new MoveFitness();
    }

    public Move(SensorReading r, Random rand) {
        this(r);
        // Randomly distribute the minDistance over a range that will
        // always include the distance from the SensorReading
        float n = rand.nextFloat();
        n *= -DISTANCE_RANGE;
        minDistance += n;

        leftSpeed = -(rand.nextInt(MAX_MOTOR_SPEED-MIN_MOTOR_SPEED)+
                       MIN_MOTOR_SPEED);
        rightSpeed = -(rand.nextInt(MAX_MOTOR_SPEED-MIN_MOTOR_SPEED)+
                       MIN_MOTOR_SPEED);

        // Invert motor direction if both touch sensors are pressed
        if (leftPressed && rightPressed) {
            leftSpeed *= -1;
            rightSpeed *= -1;
        }

        duration = rand.nextInt(MAX_DURATION-MIN_DURATION)+MIN_DURATION;
    }

    @Override
        public int compareTo(Move m) {
        Float mine = new Float(this.minDistance);
        return mine.compareTo(m.minDistance);
    }
}
