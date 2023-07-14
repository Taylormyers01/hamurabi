package hammurabi.src.main.java;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hammurabi {         // must save in a file named Hammurabi.java
    Random rand = new Random();  // this is an instance variable
    Scanner scanner = new Scanner(System.in);
    private int people;
    private int bushelOfGrain;
    private int acresOfLand;
    private int landValue;
    static Logger logger = Logger.getLogger("Hammurabi.log");

    public static void main(String[] args) {
        logger.setLevel(Level.INFO);
        new Hammurabi().playGame();
    }

    void playGame() {
        logger.info("The game has started");
        people = 100;
        bushelOfGrain = 28000;
        acresOfLand = 1000;
        landValue = 19;


    }

    private int askHowManyAcresToBuy(int price, int bushels){
        String question = "O Great Hammurabi, how many acres do you wish to buy?";
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

