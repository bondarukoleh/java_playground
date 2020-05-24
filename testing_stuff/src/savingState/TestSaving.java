package savingState;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestSaving {
    public static void main(String[] args) throws IOException {
        SomeGameHero spider = new SomeGameHero("SpiderMan", new Power("Spider sense"));
        SomeGameHero superMan = new SomeGameHero("SuperMan", new Power("Flying"));
        Path filePath = Paths.get(System.getProperty("user.dir"), "data", "SuperHeroes.ser");
        FileOutputStream file = new FileOutputStream(filePath.toAbsolutePath().toString());
        ObjectOutputStream obj = new ObjectOutputStream(file);
        obj.writeObject(spider);
        obj.writeObject(superMan);
        obj.close();
    }
}


class SomeGameHero extends Hero {
    String name = null;
    transient Power powerType = null;

    public SomeGameHero(String name, Power power) {
        this.name = name;
        powerType = power;
    }
}

class Hero implements Serializable {
    boolean superPower = true;

    public Hero() {
    }
}

class Power {
    private String type = null;

    public Power(String powerType) {
        type = powerType;
    }
}
