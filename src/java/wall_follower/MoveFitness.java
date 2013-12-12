/* @(#)MoveFitness.java
 */
/**
 *  Class containing all of the business logic regarding whether a
 *  move is considered good or not.
 *
 * @author <a href="mailto:geoff@minty-dark-tower">Geoff Shannon</a>
 */

package wall_follower;

public class MoveFitness {

    // TODO: fix these values!!! Set arbitrarily for right now
    private static final float MIN_GOOD_DISTANCE = 0.1f;
    private static final float MAX_GOOD_DISTANCE = 1.0f;

    private int goodMoves;
    private int badMoves;

    public MoveFitness() {
        goodMoves = 0;
        badMoves = 0;
    }

    /**
     *  Overall, the way that we score moves is this: there is a
     *  predefined "good" range.  If the move caused us to stay in
     *  that range, then it was good.  If we were outside the range,
     *  then if the move put us closer to that range it was good.
     *  Otherwise, it was a bad move.
     */
    public void scoreMove(SensorReading before, SensorReading after) {

        if (inGoodRange(before.distance)) {
            if (inGoodRange(after.distance)) {
                goodMoves++;
            } else {
                badMoves++;
            }
        } else {
            // If deltaDistance is positive then we moved away from
            // the wall, if it is negative then we moved closer.
            float deltaDistance = after.distance - before.distance;

            // If we were too close, and got farther away: good
            if (before.distance < MIN_GOOD_DISTANCE &&
                deltaDistance > 0) {
                goodMoves++;
            // Or if we were too far and got closer: good
            } else if (before.distance > MAX_GOOD_DISTANCE &&
                       deltaDistance < 0) {
                goodMoves++;

            } else {
                badMoves++;
            }
        }
    }

    public float getFitness() {
        if (badMoves == 0)
            return goodMoves;
        return goodMoves / badMoves;
    }

    private static boolean inGoodRange(float distance) {
        return (distance >= MIN_GOOD_DISTANCE) &&
            (distance <= MAX_GOOD_DISTANCE);
    }
}
