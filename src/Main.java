import java.util.Scanner;

public class Main {
    public static void main (String[] args){
        ScoreCalculator scoreCalculator = new ScoreCalculator();
        Scanner scanner = new Scanner(System.in);
        String points;

        while(scoreCalculator.getCurrentFrame() < 10) {
            System.out.print("Enter a number or word(1-9, Strike, Spare, Miss): ");
            points = scanner.nextLine();

            try{
                //Branching statements to figure out how to best calculate score
                //and check for invalid inputs
                if(points.equals("Strike")){
                    //Strikes can only occur on the first roll of a frame,
                    //with the exception of the 10th frame.
                    if(scoreCalculator.getIsFirstRoll() == true){
                        scoreCalculator.addStrike();
                    }
                    else {
                        System.out.println("Cannot get a Strike on this roll");
                    }
                }
                else if(points.equals("Spare")){
                    //Spares can only occur on the second roll of a frame
                    //On the 10th frame, they can potentially occur on the third roll.
                    if(scoreCalculator.getIsFirstRoll() == false){
                        //Spare occurs when all remaining pins a knocked down on a second roll.
                        scoreCalculator.addSpare();
                    }
                    else{
                        System.out.println("Spare cannot occur at this roll");
                    }
                }
                else if(points.equals("Miss")){
                    scoreCalculator.addScore(0);
                }
                else if(Integer.parseInt(points) > 0){
                    //A roll that exceeds number of remaining pins is invalid.
                    if(Integer.parseInt(points) < scoreCalculator.getNumPins()){
                        scoreCalculator.addScore(Integer.parseInt(points));
                    }
                    else if(Integer.parseInt(points) == scoreCalculator.getNumPins()){
                        System.out.println("Inputted number matches number of remaining pins. Did you mean to input Strike or Spare?");
                    }
                    else{
                        System.out.println("Inputted number exceeds number of remaining pins");
                    }
                }
                scoreCalculator.printScoreboard();
            } catch (Exception e) {
                System.out.println("Invalid input, try again. Words are case-sensitive");
            }
        }
    }
}
