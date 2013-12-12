/* @(#)Move.java
 */
/**
 *
 *
 * @author <a href="mailto:geoff@minty-dark-tower">Geoff Shannon</a>
 */
package wall_follower;

import java.lang.Comparable;

public class Move  implements Comparable<Move> {
    public static final float DISTANCE_RANGE = 5.0f;

    float minDistance;
    // maxDistance is a constant value larger than minDistance
    boolean leftPressed;
    boolean rightPressed;

    int leftSpeed;
    int rightSpeed;
    int duration;

    MoveFitness fitness;

    @Override
        public int compareTo(Move m) {
        Float mine = new Float(this.minDistance);
        return mine.compareTo(m.minDistance);
    }
}
