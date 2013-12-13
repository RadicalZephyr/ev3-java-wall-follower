/* @(#)Move.java
 */
/**
 *
 *
 * @author <a href="mailto:geoff@minty-dark-tower">Geoff Shannon</a>
 */
package wall_follower;

import java.lang.Comparable;
import java.util.Comparator;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Move  implements Comparable<Move> {

    final Logger logger = LoggerFactory.getLogger(Move.class);

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

    public Move() {
        minDistance = 0.0f;

        leftPressed = false;
        rightPressed = false;

        leftSpeed = 0;
        rightSpeed = 0;
        duration = 0;

        fitness = new MoveFitness();
    }

    public Move(SensorReading r) {
        this();

        minDistance = r.distance;
        leftPressed = r.leftPressed;
        rightPressed = r.rightPressed;
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

    public Move breedWith(Move that, Random rand) {
        Move child = new Move();

        // Select which parent this gene comes from
        child.minDistance = rand.nextFloat() > 0.5 ?
            this.minDistance : that.minDistance;
        // Randomly mutate it
        child.minDistance *= rand.nextFloat() > 0.98 ?
            (rand.nextFloat() + 0.5f) : 1.0f;

        // Select which parent this gene comes from
        child.leftPressed = rand.nextFloat() > 0.5f ?
            this.leftPressed : that.leftPressed;
        // Randomly mutate it
        child.leftPressed = rand.nextFloat() > 0.98 ?
            !child.leftPressed : child.leftPressed;

        // Select which parent this gene comes from
        child.rightPressed = rand.nextFloat() > 0.5f ?
            this.rightPressed : that.rightPressed;
        // Randomly mutate it
        child.rightPressed = rand.nextFloat() > 0.98 ?
            !child.rightPressed : child.rightPressed;

        // Select which parent this gene comes from
        child.leftSpeed = rand.nextFloat() > 0.5f ?
            this.leftSpeed : that.leftSpeed;
        // Randomly mutate it
        child.leftSpeed *= rand.nextFloat() > 0.98 ?
            (rand.nextFloat() + 0.5f) : 1.0f;

        // Select which parent this gene comes from
        child.rightSpeed = rand.nextFloat() > 0.5f ?
            this.rightSpeed : that.rightSpeed;
        // Randomly mutate it
        child.rightSpeed *= rand.nextFloat() > 0.98 ?
            (rand.nextFloat() + 0.5f) : 1.0f;

        // Select which parent this gene comes from
        child.duration = rand.nextFloat() > 0.5f ?
            this.duration : that.duration;
        // Randomly mutate it
        child.duration *= rand.nextFloat() > 0.98 ?
            (rand.nextFloat() + 0.5f) : 1.0f;

        return child;
    }

    @Override
    public String toString() {
        return String.format("%+0.4f %d %d %4d %4d %4d",
                             minDistance,
                             leftPressed ? 1 : 0,
                             rightPressed ? 1 : 0,
                             leftSpeed,
                             rightSpeed,
                             duration);
    }

    @Override
    public int compareTo(Move m) {
        Float mine = new Float(this.minDistance);
        return mine.compareTo(m.minDistance);
    }

    public static class CompareFitness implements Comparator<Move> {

        @Override
        public int compare(Move l, Move r) {
            return l.compareTo(r);
        }
    }
}
