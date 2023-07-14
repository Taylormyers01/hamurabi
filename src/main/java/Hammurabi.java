package hammurabi.src.main.java;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hammurabi {         // must save in a file named Hammurabi.java
    Random rand = new Random();  // this is an instance variable
    Scanner scanner = new Scanner(System.in);

    static Logger logger = Logger.getLogger("Hammurabi.log");

    public static void main(String[] args) {
        logger.setLevel(Level.INFO);
        new Hammurabi().playGame();
    }

    void playGame() {
        logger.info("The game has started");
        int people = 100;
        int bushelOfGrain = 28000;
        int acresOfLand = 1000;
        int landValue = 19;
        int outcome;
        int foodForPeasants;
        //Checks if you want to buy land. If the number is zero or less, it asks if you want to sell land.
        outcome = askHowManyAcresToBuy(landValue, bushelOfGrain);
        if(outcome > 0){
            bushelOfGrain = bushelOfGrain - (outcome * landValue);
            acresOfLand += outcome;
            logger.info("Current Bushels: " + bushelOfGrain + " Acres of Land: " + acresOfLand);
        }else if(outcome <= 0){
            outcome = askHowManyAcresToSell(acresOfLand);
            acresOfLand = acresOfLand - outcome;
            bushelOfGrain = bushelOfGrain + (outcome * landValue);
            logger.info("Current Bushels: " + bushelOfGrain + " Acres of Land: " + acresOfLand);
        }

        //Asks how much grain to feed the peasants
        foodForPeasants = askHowMuchGrainToFeedPeople(bushelOfGrain);
        bushelOfGrain -= foodForPeasants;
        logger.info("Food for the people " + foodForPeasants + " left over grain " + bushelOfGrain);

        //Get how many acres to plant
        outcome = askHowManyAcresToPlant(acresOfLand, people, bushelOfGrain);




    }

    private int askHowManyAcresToPlant(int acresOwned, int population, int bushels){
        String question = "O Great Hammurabi, how many acres would you like to plant? ";
        int outcome;
        while(true) {
            outcome = getNumber(question);
            if (outcome > acresOwned || outcome > (population * 10) || outcome > (bushels / 2)) {
                System.out.println("Oh great Hammurabi, we do not have the resources for that!");
            } else {
                return outcome;
            }
        }

    }


    private int askHowMuchGrainToFeedPeople(int bushels){
        String question = "O Great Hammurabi, how much shall we feed the people? ";
        int outcome;
        while(true) {
            outcome = getNumber(question);
            if (outcome > bushels) {
                System.out.println("Surely you jest O great leader. You only have " + bushels + " bushels of grain!");
            } else {
                return outcome;
            }
        }

    }
    private int askHowManyAcresToSell(int acresOwned){
        String question = "O Great Hammurabi, how many acres would you like to sell? ";
        while(true) {
            int outcome = getNumber(question);
            if (outcome > acresOwned) {
                System.out.println("Surely you jest O great leader. You only have " + acresOwned + " acres of land!");
            } else {
                return outcome;
            }
        }
    }

    private int askHowManyAcresToBuy(int price, int bushels){
        String question = "O Great Hammurabi, how many acres do you wish to buy? ";
        while(true) {
            int outcome = getNumber(question);
            if (outcome * price > bushels) {
                System.out.println("Surely you jest O great leader. You only have " + bushels + " bushels of grain!");
            }else{
                return outcome;
            }
        }
    }

    /**
     * Prints the given message (which should ask the user for some integral
     * quantity), and returns the number entered by the user. If the user's
     * response isn't an integer, the question is repeated until the user
     * does give a integer response.
     *
     * @param message The request to present to the user.
     * @return The user's numeric response.
     */
    private int getNumber(String message) {
        while (true) {
            System.out.print(message);
            try {
                return scanner.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("\"" + scanner.next() + "\" isn't a number!");
            }
        }
    }

    //other methods go here
}

