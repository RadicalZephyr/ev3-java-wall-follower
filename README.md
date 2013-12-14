To compile this, first, rename `compile` to `compile.bat`.  Now, go to
a command prompt in this directory, and run:

    compile.bat

Then the jar file will be in the target directory, and called
ev3main.jar.

Sources are under "src/java/wall_follower/"

Implementation Notes
--------------------

Main.java contains the main loop and basic algorithm for running a
series of populations to try and learn to follow a wall.  The rest of
the logic is distributed among the various classes fairly rationally.
The MovePopulation class contains all the code regarding generating
new generations and keeping track of the population as whole, as well
as differentiating between the four different situations which
correspond to what combination of touch sensor are depressed.

The Move class contains the logic for generating random members of the
population as well as breeding two individuals together.

MoveFitness is basically just the fitness function embodied in a
class.  The fitness function is basically we track how many good moves
and bad moves a particular Move has resulted in.  The percentage of
good moves of the total is the fitness value of that individual.

I/O Representation
==================

Each move has several attributes that identify it.  The inputs of
distance (float), left and right touch sensors (boolean), and the
outputs of left and right motor speed (degrees per second), as well as
duration of motor run (milliseconds), and motor direction (boolean).
During breeding, all of these attributes are considered to be genes
except for the motor direction which is based on the state of the
sensors.

Selection Strategy
==================

The selection strategy I chose to employ is a combination of steady
state and elitism.  All moves in the population are concatenated into
one list, which is then sorted by their fitness.  I then pass all
moves that haven't been tested yet to the next generation.  From the
remaining set of tested Moves I remove somewhere between the bottom
1/6 and 1/3.  The remaining Moves are then used to breed replacements
for however many were removed by the cutoff.  Finally, the top three
are always copied directly to the new population.

Breeding Strategy
=================

The breeding strategy utilizes both crossover and random mutation.
For each "gene" in the Move representation, the child flips a coin to
determine which parent to take it from, both having an equal chance.
Then that gene is mutated with a low probability (2%) to be multiplied
by some amount between 0.5 and 1.5.  I felt that this would give a
good amount of mutation without going to crazy.

### Flaws

One of the major flaws with my selection and breeding strategy is that
I breed the entire population together.  I think it would have been
more effective to keep the populations segregated based on their touch
sensor status.

### Ideas for Improvement

One idea that I had very late in development that I would have liked
to implement (but lacked the time to pursue) was a very different I/O
representaion.  One of the most important pieces of information about
the robot's state that my current implemenation totally ignored was
the robot's attitude with respect to the nearest wall.  A simple way
to determine this within the limitations of our hardware would be to
take two readings from the ultrasonic sensor while moving a short
distance forward between them.  The change in distance reading would
allow you to determine the relative orientation of the robot with
respect to the wall.  This information would be useful because it
could then be used to further identify different populations of moves
that should be kept seperate.

Another area that was probablimatic was the physical design of my
robot.  In my attempt to make the robot small enough to fit into the
smaller maze, I inadvertantly moved all of the weight to the rear
ball-bearing.  This had the consequence that the wheel no longer had
the friction to fully depress both touch sensors reliably.  This led
my robot to run into the wall quite a lot.  There was also too large a
gap between the distance sensors on the front of the robot.  This
caused an similar issue where the robot would end up stuck on the
corners of the 180 degree turns because the wall was small enough to
get between the touch sensors.
