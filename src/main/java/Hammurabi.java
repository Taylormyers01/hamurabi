package hammurabi.src.main.java;

import javax.swing.*;
import java.awt.*;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hammurabi extends JFrame {         // must save in a file named Hammurabi.java
    Random rand = new Random();  // this is an instance variable
    Scanner scanner = new Scanner(System.in);

    static Logger logger = Logger.getLogger("Hammurabi.log");
    boolean uprising = false;
    int overallStarvDeath = 0;
    public static void main(String[] args) {
        //logger.setLevel(Level.INFO);
        logger.setLevel(Level.OFF);
        new Hammurabi().playGame();
    }

    int year = 1;
    void playGame() {
        logger.info("The game has started");
        int people = 100;
        int bushelOfGrain = 28000;
        int acresOfLand = 1000;
        int landValue = 19;
        int outcome, foodForPeasants;
        int numOfImmigrant = 0;
//        JLabel peopleL, bushelOfGrainL, acresOfLandL, landValueL;
//
//        peopleL = new JLabel();
//        peopleL.setFont(new Font("Ink Free",Font.BOLD,15));
//        this.add(peopleL);
//
//        peopleL.setText("People: " + people);
//
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setTitle("Hammurabi");
//        this.setLayout(new FlowLayout());
//        this.setSize(438, 320); //350 220
//        this.setResizable(false);
//
//        this.setVisible(true);

        while(year <= 10 && !uprising) {

            //Checks if you want to buy land. If the number is zero or less, it asks if you want to sell land.
            outcome = askHowManyAcresToBuy(landValue, bushelOfGrain);
            if (outcome > 0) {
                bushelOfGrain = bushelOfGrain - (outcome * landValue);
                acresOfLand += outcome;
                logger.info("Current Bushels: " + bushelOfGrain + " Acres of Land: " + acresOfLand);
            } else if (outcome <= 0) {
                outcome = askHowManyAcresToSell(acresOfLand);
                acresOfLand = acresOfLand - outcome;
                bushelOfGrain = bushelOfGrain + (outcome * landValue);
                logger.info("Current Bushels: " + bushelOfGrain + " Acres of Land: " + acresOfLand);
            }

            //Asks how much grain to feed the peasants
            foodForPeasants = askHowMuchGrainToFeedPeople(bushelOfGrain);
            bushelOfGrain = bushelOfGrain - foodForPeasants; //Current up to date grain value
            logger.info("Food for the people " + foodForPeasants + " left over grain " + bushelOfGrain);

            //Get how many acres to plant
            int acresToPlant = askHowManyAcresToPlant(acresOfLand, people, bushelOfGrain);


            int numPlagueDeath = plagueDeaths(people);
            people = people - numPlagueDeath;

            logger.info("People before starvation count " + people);
            //gathers the number of starvation deaths
            int starvationDeaths = starvationDeaths(people, foodForPeasants);
            overallStarvDeath += starvationDeaths;
            logger.info("Starvation death count: " + starvationDeaths);
            people = people - starvationDeaths;


            //checks to see if enough people died for an uprising
            uprising = uprising(people, starvationDeaths);
            if(uprising){
                break;
            }
            //if no one starves, we can get Immigrants
            if (starvationDeaths == 0) {
                numOfImmigrant = immigrants(people, acresOfLand, bushelOfGrain);
                logger.info("Immigrants generated: " + numOfImmigrant);
                people = people + numOfImmigrant;
            }

            //figures out how much harvest we get
            int harvested = harvest(acresOfLand);


            //Figure out how much if any grain is eaten by rats
            int eatenByRats = grainEatenByRats(bushelOfGrain);

            landValue = newCostOfLand();

            year++;
            bushelOfGrain = bushelOfGrain + harvested - eatenByRats;
            printSummary(year,starvationDeaths, numOfImmigrant, people, harvested, eatenByRats, bushelOfGrain, acresOfLand, landValue);

        }

        gameSummary();

    }


    public void gameSummary(){
        StringBuilder sb = new StringBuilder();
        if(uprising){
            //message about losing due ot uprising
            sb.append("O great Hammurabi, we must leave the city, the people rebel!\n");
            sb.append("Game over - you've been thrown out of office\n");
            sb.append("During your reign, " + overallStarvDeath + " citizens have died of starvation.\n");
        }else if(year == 11){
            //message about being a good ruler and working full term
            sb.append("O great Hammurabi, the people rejoice!!\n");
            sb.append("You win!  - you've maintained your empire for your 10 year reign\n");
            sb.append("During your reign, " + overallStarvDeath + " citizens have died of starvation.\n");
        }
        System.out.println(sb.toString());
    }

    public void printSummary(int currentYear, int starvationDeaths, int immigrants, int currentPop, int harvest, int eatenByRats,
                            int bushelsOfGrian, int land, int landValue ){
        StringBuilder sb = new StringBuilder();
        sb.append("O great Hammurabi!\n");
        sb.append("You are in year " + currentYear + " of your ten year rule\n");
        sb.append("In the previous year " + starvationDeaths + " people starved to death.\n");
        sb.append("In the previous year " + immigrants + " people entered the kingdom.\n");
        sb.append("The population is now " + currentPop + ".\n");
        sb.append("We harvested " + harvest + " bushels at " + (harvest/land) + " bushels per acre.\n");
        sb.append("Rats destroyed " + eatenByRats + " bushels, leaving " + bushelsOfGrian + " bushels in storage.\n");
        sb.append("The city owns "+ land + " acres of land.\n");
        sb.append("Land is currently worth "+ landValue + " bushels per acre.\n");
        System.out.println(sb.toString());
    }


    public int newCostOfLand(){
        return (rand.nextInt(7) +17);
    }
    public int grainEatenByRats(int bushels) {
        double percentEaten = 0;
        if(rand.nextInt(100)+1 <= 40){
            percentEaten = (double)rand.nextInt(21) + 10.0;
            logger.info(percentEaten +"% of grains eaten");
            percentEaten = percentEaten/100;
        }
        if(percentEaten != 0){
            bushels = (int)(bushels * percentEaten);
            logger.info(bushels + " is the number of bushels eaten");
            return bushels;

        }
        return 0;
    }

    public int harvest(int acres){
        int bushelsPerAcre = rand.nextInt(6) + 1;
        logger.info(bushelsPerAcre + " is the bushels per acre");
        return acres* bushelsPerAcre;
    }

    public int immigrants(int population, int acresOwned, int grainInStorage){
       return (20 * acresOwned+ grainInStorage) / (100 * population) + 1;
    }

    public boolean uprising(int population, int howManyPeopleStarved){
        if(howManyPeopleStarved != 0){
            double hold = (double) howManyPeopleStarved/(double) population;
            logger.info("% that starved " + hold);
            if(hold >= 0.45){
                return true;
            }
        }
        return false;
    }

    public int starvationDeaths(int population, int bushelsFedToPeople){
        if(population*20 <= bushelsFedToPeople) {
            return 0;
        }else if (bushelsFedToPeople >= 20){
            return population= population - (bushelsFedToPeople/20);
        }
        else{
            return population;
        }
    }

    public int plagueDeaths(int population){
        int outcome;
        outcome = rand.nextInt(100) + 1;
        if (outcome <= 15){
            logger.info("A plague has been triggered ");
            return population/2;
        }
        logger.info("No plague this year");
        return 0;
    }

    public int askHowManyAcresToPlant(int acresOwned, int population, int bushels){
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


    public int askHowMuchGrainToFeedPeople(int bushels){
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
    public int askHowManyAcresToSell(int acresOwned){
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

    public int askHowManyAcresToBuy(int price, int bushels){
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
    public int getNumber(String message) {
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

