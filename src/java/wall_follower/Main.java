/* Program name: Main
     Description: A wall-following robot
     Interesting Features:

Hardware:
Port                  Sensor
1                      Touch
2                      Ultrasonic
4                      Touch

Port                Motor
B                    Left
C                    Right

Course Number: CSCI 372, Fall 2013
Student Name: Geoff Shannon
References: lejos
*/

package wall_follower;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;

import lejos.hardware.Button;
import lejos.hardware.LCD;
import lejos.hardware.Sound;

import lejos.robotics.SampleProvider;

import lejos.utility.Delay;

import java.lang.Comparable;

import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;

public class Main
{
    private EV3TouchSensor leftTouch;
    private EV3TouchSensor rightTouch;

    private NXTRegulatedMotor leftMotor;
    private NXTRegulatedMotor rightMotor;

    private EV3UltrasonicSensor distance;
    private SampleProvider distanceSampler;

    public static void main(String[] args) {
        Main current = new Main();
        boolean done = false;
        LCD.clear();
        current.promptForStartPush();
        current.followWall();
        LCD.clear();
        LCD.drawString("Done!", 1, 1);
        Delay.msDelay(1000);
    }

    public Main() {
        setupSensors();
        setupMotors();
    }

    void setupSensors() {
        leftTouch = new EV3TouchSensor(SensorPort.S1);
        rightTouch = new EV3TouchSensor(SensorPort.S4);

        distance = new EV3UltrasonicSensor(SensorPort.S2);
        distance.enable();
        distanceSampler = distance.getDistanceMode();
    }

    void setupMotors() {
        leftMotor = Motor.B;
        rightMotor = Motor.C;
        leftMotor.setAcceleration(3000);
        rightMotor.setAcceleration(3000);
        leftMotor.setSpeed(200);
        rightMotor.setSpeed(200);
    }

    void promptForStartPush() {
        Sound.twoBeeps();
        Sound.beep();
        LCD.clear();
        LCD.drawString("Please push any", 1, 1);
        LCD.drawString("button to begin.", 1, 3);
        Button.waitForAnyPress();
        LCD.clear();
        LCD.drawString("Running...", 1, 1);
    }

    void startMotors() {
        leftMotor.backward();
        rightMotor.backward();
    }

    void stopMotors() {
        leftMotor.stop(true);
        rightMotor.stop();
        leftMotor.flt(true);
        rightMotor.flt();
    }

    float checkDistance() {
        float[] distance = new float[1];
        distanceSampler.fetchSample(distance, 0);
        return distance[0];
    }

    void followWall() {
        startMotors();
        ArrayList<Move> moves = new ArrayList<Move>();
        Move move = new Move();
        move.leftSpeed = -300;
        move.rightSpeed = -300;
        move.duration = 1000;
        moves.add(move);
        SensorReading prevReading;
        SensorReading curReading = readSensors();


        boolean done = false;
        while (!done) {
            move = chooseMove(moves, curReading);
            execute(move);

            prevReading = curReading;
            curReading = readSensors();

            if (lastMoveWasGood(prevReading, curReading)) {

            }

            if (Button.readButtons() != 0) {
                done = true;
            }
        }
    }

    Move chooseMove(List<Move> moves, SensorReading reading) {
        return moves.get(0);
    }

    void execute(Move move) {
        leftMotor.setSpeed(move.leftSpeed);
        rightMotor.setSpeed(move.rightSpeed);
        startMotors();
        Delay.msDelay(move.duration);
        stopMotors();
    }

    SensorReading readSensors() {
        SensorReading r = new SensorReading();
        r.distance = checkDistance();
        r.leftPressed = leftTouch.isPressed();
        r.rightPressed = rightTouch.isPressed();
        return r;
    }

    boolean lastMoveWasGood(SensorReading previous,
                            SensorReading current) {
        return false;
    }

    private class SensorReading {
        public float distance;
        public boolean leftPressed;
        public boolean rightPressed;
    }

    private class Move implements Comparable<Move> {
        public static final float DISTANCE_RANGE = 5.0f;

        float minDistance;
        // maxDistance is a constant value larger than minDistance
        boolean leftPressed;
        boolean rightPressed;

        int leftSpeed;
        int rightSpeed;
        int duration;

        @Override
        public int compareTo(Move m) {
            Float mine = new Float(this.minDistance);
            return mine.compareTo(m.minDistance);
        }
    }

    private class MovePopulation {
        TreeSet<Move> nonePressed;
        TreeSet<Move> leftPressed;
        TreeSet<Move> rightPressed;
        TreeSet<Move> leftAndRight;

        public MovePopulation() {
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

        public void getForReading(SensorReading reading) {
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
        }
    }
}
