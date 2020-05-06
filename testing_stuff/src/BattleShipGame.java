package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BattleShipGame {
    public static void main(String[] args) {
        GameField gameField = new GameField();
        IOHelper ioHelper = new IOHelper();
        int numberOfGuesses = 0;
        int randomNum = (int) (Math.random() * 5);
        int[] shipLocation = {randomNum, randomNum + 1, randomNum + 2};
        gameField.setShipLocation(shipLocation);

        String shipKilled = "miss";

        while (!shipKilled.equals("kill")) {
            numberOfGuesses++;
            System.out.println("Please enter your guess to hit:");
            String userHit = ioHelper.getUserGuess();

            shipKilled = gameField.checkHit(userHit);
        }
        System.out.printf("You've killed the ship after %d guesses!", numberOfGuesses);
    }
}

class GameField {
    private int[] shipLocation;
    private int hits;

    public void setShipLocation(int[] location) {
        shipLocation = location;
    }

    public int[] getShipLocation() {
        return shipLocation;
    }

    public String checkHit(String hitLocation) {
        int userHit = Integer.parseInt(hitLocation);
        int[] shipLocation = getShipLocation();
        String hitResult = "miss";
        for (int cell : shipLocation) {
            if (cell == userHit) {
                hitResult = "hit";
                hits++;
                break;
            }
        }

        if (shipLocation.length == hits) {
            hitResult = "kill";
        }

        System.out.printf("Hit result is: '%s' \n", hitResult);
        return hitResult;
    }
}

class IOHelper {
    public String getUserGuess() {
        String userEnter = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            userEnter = reader.readLine();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        return userEnter;
    }
}

class TestGame {
    public static void main(String[] args) {
        GameField gameField = new GameField();

        int[] shipLocation = {2, 3, 4};
        gameField.setShipLocation(shipLocation);

        String userHit = "2";
        String hitResult = gameField.checkHit(userHit);
        System.out.println(hitResult);
    }
}



