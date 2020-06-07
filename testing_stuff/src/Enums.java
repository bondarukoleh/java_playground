import java.util.ArrayList;
import java.util.Arrays;

public class Enums {
    enum SimplePersonEnum {
        JERRY,
        PHIL
    }

    public static void main(String[] args) {
        Enums.tellAbout(SimplePersonEnum.PHIL);
        SimplePersonEnum p = SimplePersonEnum.JERRY;
        System.out.println(p.equals(SimplePersonEnum.PHIL)); // false
        System.out.println(p == SimplePersonEnum.JERRY); // true

        SolidPersonEnum[] persons = new SolidPersonEnum[]{SolidPersonEnum.JERRY, SolidPersonEnum.PHIL};
        for (SolidPersonEnum person : persons){
            person.sayHello();
            person.tellAboutYOurSelf();
        }
        SolidPersonEnum person = persons[0];
        System.out.println(person == SolidPersonEnum.JERRY);

        // looping on the enum
        for (SolidPersonEnum solidPersonEnum : SolidPersonEnum.values()){
            solidPersonEnum.sayHello();
        }
    }

    static void tellAbout(SimplePersonEnum person){
        switch (person){
            case PHIL:
                Enums.storyAbout("PHIL");
                break;
            case JERRY:
                Enums.storyAbout("JERRY");
                break;
        }
    }

    static void storyAbout(String person){
        System.out.println("This is the story about " + person);
    }
}

enum SolidPersonEnum {
    JERRY("JERRY"),
    PHIL("Phil"){
        @Override
        public void tellAboutYOurSelf() {
            System.out.println("I'm a plumber.");
        }
    };

    private String name;
    SolidPersonEnum(String name) {
        this.name = name;
    }

    public void sayHello(){
        System.out.printf("Hi! I'm %s. \n", name);
    }

    public void tellAboutYOurSelf(){
        System.out.println("I don't do much.");
    }
}
