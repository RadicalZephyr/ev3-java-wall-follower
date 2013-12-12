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
        NavigableSet<Move> set = getSetForMove(move);
        set.add(move);
    }

    public Move getMoveForReading(SensorReading reading) {
        Move move = new Move(reading);
        Move minMove = new Move(reading);
        minMove.minDistance = reading.distance - Move.DISTANCE_RANGE;

        NavigableSet<Move> set = getSetForMove(move);

        NavigableSet<Move> legalMoves = set.subSet(minMove, true,
                                             move, true);

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

    private NavigableSet<Move> getSetForMove(Move move) {
        if (move.leftPressed && move.rightPressed) {
            return leftAndRight;
        } else if (move.leftPressed) {
            return leftPressed;
        } else if (move.rightPressed) {
            return rightPressed;
        } else {
            return nonePressed;
        }
    }

    /**
     *  This method MUST ensure that getMoveForReading will find at
     *  least one legal move.  It is used to generate populations on
     *  the fly for new situations.
     */
    public void seedPopulationForReading(SensorReading reading) {
        NavigableSet<Move> set = getSetForMove(new Move(reading));
        for (int i = 0; i < 10; i++) {
            set.add(new Move(reading, rand));
        }
    }
}
