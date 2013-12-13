/* @(#)MovePopulation.java
 */
/**
 *
 *
 * @author <a href="mailto:geoff@minty-dark-tower">Geoff Shannon</a>
 */
package wall_follower;

import java.util.Random;
import java.util.Collections;

import java.util.NavigableSet;
import java.util.TreeSet;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovePopulation {

    final Logger logger = LoggerFactory.getLogger(MovePopulation.class);

    private static final int BASE_ITERATIONS_PER_GEN = 10;
    private static final int ITERATIONS_MULTIPLIER = 2;

    private TreeSet<Move> nonePressed;
    private TreeSet<Move> leftPressed;
    private TreeSet<Move> rightPressed;
    private TreeSet<Move> leftAndRight;
    private Random rand;

    private int generation;
    private int iteration;
    private int nextGeneration;

    private int totalPopulation;

    public MovePopulation(Random rand) {
        generation = 0;
        iteration = 0;
        nextGeneration = BASE_ITERATIONS_PER_GEN;

        totalPopulation = 0;

        this.rand = rand;
        this.nonePressed = new TreeSet<Move>();
        this.leftPressed = new TreeSet<Move>();
        this.rightPressed = new TreeSet<Move>();
        this.leftAndRight = new TreeSet<Move>();
    }

    public void add(Move move) {
        logger.trace("addMove");

        NavigableSet<Move> set = getSetForMove(move);
        set.add(move);
        totalPopulation++;
    }

    public Move getMoveForReading(SensorReading reading) {
        logger.trace("getMoveForReading");

        // Assume that get is run once for each iteration
        iteration++;
        if (iteration >= nextGeneration) {
            incrementGeneration();
        }

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

    private NavigableSet<Move> getSetForReading(SensorReading reading) {
        return getSet(reading.leftPressed, reading.rightPressed);
    }

    private NavigableSet<Move> getSetForMove(Move move) {
        return getSet(move.leftPressed, move.rightPressed);
    }

    private NavigableSet<Move> getSet(boolean left, boolean right) {
        if (left && right) {
            return leftAndRight;
        } else if (left) {
            return leftPressed;
        } else if (right) {
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
        logger.trace("seedPopulation");

        NavigableSet<Move> set = getSetForReading(reading);
        for (int i = 0; i < 10; i++) {
            set.add(new Move(reading, rand));
            totalPopulation++;
        }
    }

    private void incrementGeneration() {
        logger.trace("incrementGeneration");
        logger.debug("population size before: {}", totalPopulation);
        generation++;
        iteration = 0;
        nextGeneration *= ITERATIONS_MULTIPLIER;

        List<Move> allMoves = new ArrayList<Move>();
        allMoves.addAll(leftAndRight);
        allMoves.addAll(leftPressed);
        allMoves.addAll(rightPressed);
        allMoves.addAll(nonePressed);

        // Empty the current population
        // NOTE: Instead of clearing these, I could use retainAll once
        // a final population has been generated.
        leftAndRight.clear();
        leftPressed.clear();
        rightPressed.clear();
        nonePressed.clear();
        totalPopulation = 0;

        // Do a steady-state selection, order by fitness value
        Collections.sort(allMoves, new Move.CompareFitness());

        // Find the first move that has been tested.
        // Preserve moves that have not yet been tested.
        int i = 0;
        for (Move m : allMoves) {
            if (m.fitness.getFitness() < 0) {
                add(m);
            } else {
                break;
            }
            i++;
        }
        List<Move> testedMoves = allMoves.subList(i, allMoves.size());

        // Now, discard the bottom percentage.  Somewhere between the
        // bottom third and bottom sixth will be discarded.
        int size = testedMoves.size();
        int cutoff = (int)size/(rand.nextInt(3)+3);

        // Now practice elitism.  The top three will be copied
        // straight to the next population.
        int minTop = size-3 >= 0 ? size-3 : 0;
        for (Move m : testedMoves.subList(minTop, size)) {
            add(m);
        }

        breedReplacements(testedMoves.subList(cutoff, size), cutoff);
        logger.debug("population size after: {}", totalPopulation);
    }

    private void breedReplacements(List<Move> breeders, int removed) {
        logger.trace("breedReplacements");
        int childrenBred = 0;

        int size = breeders.size();
        while (childrenBred < removed) {
            Collections.shuffle(breeders, rand);

            for (int i = 0 ; childrenBred < removed &&
                     i < size-1; i += 2) {
                add(breeders.get(i).breedWith(breeders.get(i+1), rand));
                childrenBred++;
            }
        }
    }

}
