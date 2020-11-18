import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class ScoreCalculator {
    private int[] scoreboard; //Tracks current scores
    private int currentFrame;
    private int numPins; //Tracks remaining pins
    private int countRollsAfterStrike;
    private int finalFrameExtraRolls; //Determines if player gets third roll on final frame

    private boolean isSpare;
    private boolean isFirstRoll; //Tracks if first roll of a frame

    private Queue<Integer> strikeTracker; //Tracks which frames a strike occurred

    private LinkedList<String> rollTracker; //Tracks what rolls occurred at what frame. Purely for printing purposes.

    public ScoreCalculator() {
        scoreboard = new int[10];
        currentFrame = 0;
        numPins = 10;
        countRollsAfterStrike = 0;
        finalFrameExtraRolls = 0;

        isSpare = false;
        isFirstRoll = true;

        strikeTracker = new LinkedList<Integer>();

        rollTracker = new LinkedList<String>();

        for(int i = 0; i < 10; i++){
            scoreboard[i] = 0;
        }
    }

    //Calculate based on Strike ruling
    public void addStrike () {
        scoreboard[currentFrame] = 10;
        rollTracker.add("X");

        if(strikeTracker.size() > 0) {
            countRollsAfterStrike++;
        }
        strikeTracker.add(currentFrame); //save index of where strike occurred

        if(countRollsAfterStrike >= 2 || isSpare == true) {
            resolveScore();
            System.out.println(countRollsAfterStrike);
            System.out.println(strikeTracker);
        }

        if(currentFrame < 9){
            currentFrame++;
            isSpare = false;
        }
        else if(currentFrame == 9 && isFirstRoll == true){
            //do something
            finalFrameExtraRolls = 2;
        }
        else{
            //do something
            resolveFinalFrame();
            finalFrameExtraRolls -= 1;
        }
    }

    //Calculate based on Spare ruling
    public void addSpare () {
        //TODO: calculate according to spare rules
        scoreboard[currentFrame] += numPins;
        rollTracker.add("/");

        if(strikeTracker.size() > 0){
            countRollsAfterStrike++;
        }

        resolveScore();

        isSpare = true;
        advanceFrameOrRoll();
    }

    //Calculate normally
    public void addScore (int points) {
        //TODO: calculate normally
        scoreboard[currentFrame] += points;
        numPins -= points;

        if(points == 0){
            rollTracker.add("-");
        }
        else{
            rollTracker.add(Integer.toString(points));
        }

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
        else if(currentFrame == 9){
            if(numPins == 0){
                numPins = 10;
            }
            finalFrameExtraRolls -= 1;
        }
    }

    //Calculate scores after a certain number of rolls or under certain conditions.
    private void resolveScore () {
        //Two rolls after a Strike, begin adding in bonus points
        if(countRollsAfterStrike >= 2){
            int frame = strikeTracker.poll();
            //If there are multiple strikes, need to get points from two separate frames
            //Do not decrease by 2 right away to ensure proper calculation
            if(scoreboard[frame + 1] == 10 && isSpare == false){
                scoreboard[frame] += scoreboard[frame + 1] + scoreboard[frame + 2];
                countRollsAfterStrike--;
            }
            //scoreboard[frame + 1] should have the combined total of two rolls
            else{
                scoreboard[frame] += scoreboard[frame + 1];
                countRollsAfterStrike -= 2;
            }
            //Add in the previous frame's score to the strike
            if(frame > 0){
                scoreboard[frame] += scoreboard[frame - 1];
            }
            //Do not add up non-strike scores until all strikes have been resolved
            if(strikeTracker.size() == 0 && isFirstRoll == false){
                scoreboard[currentFrame] += scoreboard[currentFrame - 1];
            }
        }
        //After a spare, begin adding in bonus points
        else if(isSpare == true){
            scoreboard[currentFrame - 1] += scoreboard[currentFrame];
            isSpare = false;
        }
        //For all other scenarios, combine previous frame's score with current rolls
        else if(currentFrame > 0){
            scoreboard[currentFrame] += scoreboard[currentFrame - 1];
        }
    }

    //Check if conditions for extra rolls are met at 10th frame and calculate score
    public void resolveFinalFrame() {
        //TODO: Resolve final frame then end the game
        //Increment frame to end Main's while loop (and end the game)
        if(finalFrameExtraRolls == 0){
            currentFrame++;
        }
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

    public void printScoreboard() {
        //Print numbers 1 - 10
        System.out.print("Frame: | ");
        for(int i = 0; i < 10; i++){
            System.out.print(i + 1);
            System.out.print(" | ");
        }
        System.out.println();

        //Print rolls
        int countNumRolls = 0;
        System.out.print("Rolls: | ");
        for(int j = 0; j < rollTracker.size(); j++){
            System.out.print(rollTracker.get(j));
            if(rollTracker.get(j) == "X" || rollTracker.get(j) == "/"){
                System.out.print(" | ");
                countNumRolls = 0;
            }
            else if(countNumRolls == 1){
                System.out.print(" | ");
                countNumRolls = 0;
            }
            else{
                System.out.print(" ");
                countNumRolls++;
            }
        }
        System.out.println();

        //Print scores
        System.out.print("Score: | ");
        for(int k = 0; k < scoreboard.length; k++){
            System.out.print(scoreboard[k]);
            System.out.print(" | ");
        }
        System.out.println();
        System.out.println();
    }
}
