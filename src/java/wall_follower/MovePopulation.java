/* @(#)MovePopulation.java
 */
/**
 *
 *
 * @author <a href="mailto:geoff@minty-dark-tower">Geoff Shannon</a>
 */
package wall_follower;

import java.util.Random;
import java.util.NavigableSet;
import java.util.TreeSet;


public class MovePopulation {
    TreeSet<Move> nonePressed;
    TreeSet<Move> leftPressed;
    TreeSet<Move> rightPressed;
    TreeSet<Move> leftAndRight;
    Random rand;

    public MovePopulation(Random rand) {
        this.rand = rand;
        this.nonePressed = new TreeSet<Move>();
        this.leftPressed = new TreeSet<Move>();
        this.rightPressed = new TreeSet<Move>();
        this.leftAndRight = new TreeSet<Move>();
    }

    public void add(Move move) {
        if (move.leftPressed && move.rightPressed) {
            leftAndRight.add(move);
        } else if (move.leftPressed) {
            leftPressed.add(move);
        } else if (move.rightPressed) {
            rightPressed.add(move);
        } else {
            nonePressed.add(move);
        }
    }

    public Move getMoveForReading(SensorReading reading) {
        Move minMove = new Move();
        Move move = new Move();
        move.minDistance = reading.distance;
        minMove.minDistance = reading.distance - Move.DISTANCE_RANGE;

        NavigableSet<Move> legalMoves;

        if (move.leftPressed && move.rightPressed) {
            legalMoves = leftAndRight.subSet(minMove, true,
                                             move, true);
        } else if (move.leftPressed) {
            legalMoves = leftPressed.subSet(minMove, true,
                                            move, true);
        } else if (move.rightPressed) {
            legalMoves = rightPressed.subSet(minMove, true,
                                             move, true);
        } else {
            legalMoves = nonePressed.subSet(minMove, true,
                                            move, true);
        }

        if (legalMoves.size() == 0) {
            seedPopulationForReading(reading);
            return getMoveForReading(reading);
        }

        Move finalMove = legalMoves.first();
        int size = legalMoves.size();
        int item = rand.nextInt(size);
        int i = 0;
        for (Move m : legalMoves) {
            if (i == item) {
                finalMove = m;
            }
            i++;
        }
        return finalMove;
    }

    /**
     *  This method MUST ensure that getMoveForReading will find at
     *  least one legal move.  It is used to populate populations on
     *  the fly for new situations.
     */
    public void seedPopulationForReading(SensorReading reading) {

    }
}
