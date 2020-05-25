package savingState;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TestSaving {
    public static void main(String[] args) {
        ArrayList<Hero> heroes = new ArrayList<>();
        heroes.add(new SomeGameHero("SpiderMan", new Power("Spider sense")));
        heroes.add(new SomeGameHero("SuperMan", new Power("Flying")));
        Serialization serialization = new Serialization();
        serialization.serializeHeroes(heroes);
        ArrayList<Hero> deserializeHeroes = serialization.deserializeHeroes();
        for (Hero hero : deserializeHeroes) {
            System.out.println(hero.name);
        }
    }
}

class Serialization {
    private boolean serialized = false;
    private int serializedObjectsCount;

    public void serializeHeroes(ArrayList<Hero> heroes) {
        try {
            Path filePath = Paths.get(System.getProperty("user.dir"), "data", "SuperHeroes.ser");
            FileOutputStream file = new FileOutputStream(filePath.toAbsolutePath().toString());
            ObjectOutputStream obj = new ObjectOutputStream(file);
            for (Hero hero : heroes) {
                obj.writeObject(hero);
            }
            serializedObjectsCount = heroes.size();
            serialized = true;
            obj.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Hero> deserializeHeroes() {
        if (!serialized) {
            throw new Error("You must serialize objects before deserialize them");
        }
        ArrayList<Hero> heroes = new ArrayList<>();
        try {
            Path filePath = Paths.get(System.getProperty("user.dir"), "data", "SuperHeroes.ser");
            FileInputStream fileStream = new FileInputStream((filePath.toAbsolutePath().toString()));
            ObjectInputStream objStream = new ObjectInputStream(fileStream);
            for (int i = 0; i < serializedObjectsCount; i++) {
                heroes.add((Hero) objStream.readObject());
            }
            objStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return heroes;
    }
}


class SomeGameHero extends Hero {
    transient Power powerType = null;

    public SomeGameHero(String name, Power power) {
        this.name = name;
        powerType = power;
    }
}

abstract class Hero implements Serializable {
    boolean superPower = true;
    String name;
}

class Power {
    private String type = null;

    public Power(String powerType) {
        type = powerType;
    }
}
