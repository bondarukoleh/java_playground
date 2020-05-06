package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BattleShipGame {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        GameField gameField = new GameField();
        int[] shipLocation = {2, 3, 4};
        gameField.setShipLocation(shipLocation);

        String shipKilled = "miss";

        while (!shipKilled.equals("kill")) {
            System.out.println("Please enter your guess to hit:");
            String userHit = reader.readLine();

            shipKilled = gameField.checkHit(userHit);
        }
        System.out.println("You've killed the ship!");
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



