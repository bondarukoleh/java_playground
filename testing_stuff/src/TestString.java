package src;

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

        String randomWord =  wordListOne[(int) (wordListOne.length * Math.random())];
        String randomWord2 = wordListTwo[(int) (wordListOne.length * Math.random())];
        String randomWord3 = wordListThree[(int) (wordListOne.length * Math.random())];

//        System.out.printf("What you really need is: %s %s %s", randomWord, randomWord2, randomWord3);

        for (String part : wordListOne[0].split("/")) {
            System.out.println(part);
        }
    }
}
