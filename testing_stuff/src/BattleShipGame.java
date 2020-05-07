package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class BattleShipGame {
    public static void main(String[] args) {
        GameField gameField = new GameField();
        gameField.setupGame();
        for (Ship ship : gameField.ships){
            for (Cell cell : ship.getLocation().values()){
                System.out.println(cell);
            }
        }
        gameField.startGame();
    }
}

class GameField {
    public final ArrayList<Ship> ships = new ArrayList<>();
    private final IOHelper ioHelper = new IOHelper();
    private final String[] columns = {"a", "b", "c", "d", "e", "f", "g"};

    public void setupGame() {
        ships.add(new Ship("Big"));
        ships.add(new Ship("Medium"));
        ships.add(new Ship("Small"));

        ioHelper.printGreeting();

        for (Ship ship : ships) {
            HashMap<String, Cell> location = getShipRandomLocation();
            ship.setLocation(location);
        }
    }

    public void startGame() {
        int numberOfGuesses = 0;

        while (!ships.isEmpty()) {
            numberOfGuesses++;
            System.out.println("Please enter your guess to hit:");
            String userHit = ioHelper.getUserGuess();
            checkHit(userHit);
        }
        System.out.printf("You've killed the ship after %d guesses!", numberOfGuesses);
    }

    public HashMap<String, Cell> getShipRandomLocation() {
        int randomNum = (int) (Math.random() * 4);
        int next = randomNum + 1;
        int next2 = randomNum + 2;

        String id = String.format("%d%s", randomNum, columns[randomNum]);
        String id2 = String.format("%d%s", next, columns[next]);
        String id3 = String.format("%d%s", next2, columns[next2]);

        HashMap<String, Cell> shipLocation = new HashMap<>();
        shipLocation.put(id, new Cell(id));
        shipLocation.put(id2, new Cell(id2));
        shipLocation.put(id3, new Cell(id3));
        return shipLocation;
    }

    public void checkHit(String hitLocation) {
        for (Ship ship : ships) {
            String hitResult = ship.checkHit(hitLocation);
            if (hitResult.equals("hit")) {
                System.out.printf("You've hit the: '%s' \n", ship.getName());
                return;
            }
            if (hitResult.equals("kill")) {
                System.out.printf("You've killed the: '%s' \n", ship.getName());
                ships.remove(ship);
                return;
            }
        }
        System.out.println("You've missed.");
    }
}

class Cell {
    private String id;
    private String shipName = null;
    private boolean hit = false;

    public Cell(String id) {
        this.id = id;
    }

    public void setShipName(String name) {
        this.shipName = name;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "id='" + id + '\'' +
                ", shipName='" + shipName + '\'' +
                ", hit=" + hit +
                '}';
    }
}


class Ship {
    private String name;
    private HashMap<String, Cell> location;

    public Ship(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLocation(HashMap<String, Cell> location) {
        this.location = location;
    }

    public HashMap<String, Cell> getLocation() {
        return location;
    }

    public String checkHit(String hitLocation) {
        Cell hitCell = location.get(hitLocation);
        String hitResult = "miss";

        if (hitCell != null) {
            location.remove(hitLocation);
            if (location.isEmpty()) {
                hitResult = "kill";
            } else {
                hitResult = "hit";
            }
        }
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
        return userEnter.toLowerCase();
    }

    public void printGreeting() {
        System.out.println("You need to sink ships");
        System.out.println("Make your guess on 7X7 board. So your guess should be from A0-7 to G0-7");
    }
}

class TestGame {
    public static void main(String[] args) {
        GameField gameField = new GameField();
        gameField.setupGame();
        for (Ship ship : gameField.ships){
            for (Cell cell : ship.getLocation().values()){
                System.out.println(cell);
            }
        }
    }
}



