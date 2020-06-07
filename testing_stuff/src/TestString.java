package src;

import java.util.Arrays;

public class TestString {
    public static void main(String[] args) {
        TestString.successFormula();
    }

    static void successFormula() {
        String[] wordListOne = {"24/7", "multi-Tier", "30,000 foot", "B-to-B", "win-win", "front-end",
                "web-based", "pervasive", "smart", "six-sigma", "critical-path", "dynamic"};

        String[] wordListTwo = {"empowered", "sticky", "value-added", "oriented", "centric", "distributed",
                "clustered", "branded", "outside-the-box", "positioned", "networked", "focused", "leveraged",
                "aligned", "targeted", "shared", "cooperative", "accelerated"};

        String[] wordListThree = {"process", "tipping-point", "solution", "architecture", "core competency",
                "strategy", "mindshare", "portal", "space", "vision",
                "paradigm", "mission"};

        String randomWord = wordListOne[(int) (wordListOne.length * Math.random())];
        String randomWord2 = wordListTwo[(int) (wordListOne.length * Math.random())];
        String randomWord3 = wordListThree[(int) (wordListOne.length * Math.random())];

//        System.out.printf("What you really need is: %s %s %s", randomWord, randomWord2, randomWord3);

        for (String part : wordListOne[0].split("/")) {
            System.out.println(part);
        }
    }
}

class TestStringBuilder {
    public static void main(String[] args) {
        StringBuilder str = new StringBuilder("Hi! This is a message, I want to tell you.");
        String string = "String value";
        Print pr = new Print();

        pr.out("Value now", str);
        pr.out(".charAt(0)", str.charAt(0) + "");
        pr.out(".length()", str.length() + "");
        pr.out(".substring(0, 3)", str.substring(0, 3));
        pr.out(".toString()", str.toString());
        pr.out(".concat()", str.append(" Some new ending."));
        pr.out("Value now", str);
        pr.out("replace()", str.replace(str.indexOf("I") + 2, str.indexOf("to") - 1, "need"), "Value now", str);
        pr.out("delete", str.delete(0, 3), "Value now", str);
        pr.out("insert(5, 33)", str.insert(5, 33), "Value now", str);
        pr.out("reverse()", str.reverse(), "Value now", str);
        pr.out("String.toCharArray()", string.toCharArray());
        pr.out("String.valueOf(char [])", String.valueOf(string.toCharArray()));
    }


    static class Print {
        void out(String... string) {
            System.out.println(Arrays.toString(string));
        }

        void out(Object... objects) {
            System.out.println(Arrays.toString(objects));
        }

        void out(String message, char... rest) {
            System.out.print(message);
            System.out.println(Arrays.toString(rest));
        }
    }
}
