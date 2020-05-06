package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BattleShipGame {
    public static void main(String[] args) {
        GameField gameField = new GameField();
        IOHelper ioHelper = new IOHelper();
        int numberOfGuesses = 0;
        int randomNum = (int) (Math.random() * 5);
        ArrayList<String> shipLocation = new ArrayList<String>();
        shipLocation.add(Integer.toString(randomNum));
        shipLocation.add(Integer.toString(randomNum + 1));
        shipLocation.add(Integer.toString(randomNum + 2));

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
    private ArrayList<String> shipLocation;

    public void setShipLocation(ArrayList<String> location) {
        shipLocation = location;
    }

    public ArrayList<String> getShipLocation() {
        return shipLocation;
    }

    public String checkHit(String hitLocation) {
        ArrayList<String> shipLocation = getShipLocation();
        int userHitIndex = shipLocation.indexOf(hitLocation);
        String hitResult = "miss";

        if (userHitIndex >= 0) {
            shipLocation.remove(userHitIndex);
            if (shipLocation.isEmpty()) {
                hitResult = "kill";
            } else {
                hitResult = "hit";
            }
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

        ArrayList<String > shipLocation = new ArrayList<String>();
        shipLocation.add("1");
        shipLocation.add("2");
        shipLocation.add("3");
        gameField.setShipLocation(shipLocation);

        String userHit = "2";
        String hitResult = gameField.checkHit(userHit);
        System.out.println(hitResult);
    }
}



