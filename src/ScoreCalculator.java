import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class ScoreCalculator {
    private int totalScore;
    private int[] tempScoreHolder;
    private int[] scoreboard;
    private int currentFrame;
    private int numPins;
    private int finalFrameRoll;
    private int countRollsAfterStrike;

    private boolean isStrike;
    private boolean isSpare;
    private boolean isFirstRoll;

    private Queue<Integer> strikeTracker;
    //private Queue<Integer> rollsAfterStrikeTracker;

    private LinkedList<String> rollTracker; //Tracks what rolls occurred at what frame. Purely for printing purposes.

    public ScoreCalculator() {
        totalScore = 0;
        tempScoreHolder = new int[10];
        scoreboard = new int[10];
        currentFrame = 0;
        numPins = 10;
        finalFrameRoll = 1;
        countRollsAfterStrike = 0;

        isStrike = false;
        isSpare = false;
        isFirstRoll = true;

        strikeTracker = new LinkedList<Integer>();
        //rollsAfterStrikeTracker = new LinkedList<Integer>();

        rollTracker = new LinkedList<String>();

        for(int i = 0; i < 10; i++){
            scoreboard[i] = 0;
        }
    }

    public void addStrike () {
        //TODO: calculate according to strike rules

        scoreboard[currentFrame] = 10;
        rollTracker.add(currentFrame, "X");

        if(strikeTracker.size() > 0) {
            countRollsAfterStrike++;
        }
        strikeTracker.add(currentFrame); //save index of where strike occurred

        if(countRollsAfterStrike >= 2) {
            resolveScore();
            System.out.println(countRollsAfterStrike);
            System.out.println(strikeTracker);
        }

        if(currentFrame < 9){
            currentFrame++;
        }

        //isStrike = true;
    }

    public void addSpare () {
        //TODO: calculate according to spare rules
        scoreboard[currentFrame] += numPins;
        rollTracker.add(currentFrame, "/");

        if(strikeTracker.size() > 0){
            countRollsAfterStrike++;
        }

        resolveScore();

        isSpare = true;
        advanceFrameOrRoll();
    }

    public void addScore (int points) {
        //TODO: calculate normally
        scoreboard[currentFrame] += points;
        numPins -= points;
        rollTracker.add(currentFrame, Integer.toString(points));

        if(strikeTracker.size() > 0){
            countRollsAfterStrike++;
        }

        if(isFirstRoll == false || isSpare == true || countRollsAfterStrike >= 2){
            resolveScore();
        }

        advanceFrameOrRoll();

    }

    //After scoring, check if player should advance to next frame or roll
    private void advanceFrameOrRoll() {
        if(isFirstRoll == true){
            isFirstRoll = false;
        }
        else if(currentFrame < 9){
            isFirstRoll = true;
            numPins = 10;
            currentFrame++;
        }
    }

    private void resolveScore () {
        //TODO: begin calculating scores after certain number of rolls
        if(countRollsAfterStrike >= 2){
            int frame = strikeTracker.poll();
            if(scoreboard[frame + 1] == 10){
                scoreboard[frame] += scoreboard[frame + 1] + scoreboard[frame + 2];
                countRollsAfterStrike--;
            }
            else{
                scoreboard[frame] += scoreboard[frame + 1];
                countRollsAfterStrike -= 2;
            }
            if(frame > 0){
                scoreboard[frame] += scoreboard[frame - 1];
            }
            if(strikeTracker.size() == 0){
                scoreboard[currentFrame] += scoreboard[currentFrame - 1];
            }
        }
        else if(isSpare == true){
            scoreboard[currentFrame - 1] += scoreboard[currentFrame];
            isSpare = false;
        }
        else if(currentFrame > 0){
            scoreboard[currentFrame] += scoreboard[currentFrame - 1];
        }

    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public int getNumPins() {
        return numPins;
    }

    public boolean getIsFirstRoll() {
        return isFirstRoll;
    }

    //For debugging purposes
    public void printScoreArray() {
        for(int i = 0; i < 10; i++){
            System.out.print(scoreboard[i] + ", ");
        }
        System.out.println();
    }

    public void printScoreboard() {
        //TODO: Prints current score
    }
}
