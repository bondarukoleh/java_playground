public class TestLoops {
    public static void main(String[] args) {
        new TestWhile().bottleGame();
    }
}

class TestWhile {
    public void runSimpleWhile() {
        int i = 3;
        while (i > 0) {
            System.out.println("Hello from loop: " + i);
            i -= 1;
        }
    }

    public void bottleGame() {
        int bottles = 3;

        while (bottles > 0) {
            System.out.printf("%d bottle%s of beer on the wall. \n", bottles, this.getBottleWordEnd(bottles));
            System.out.println("Take one down, pass it around.");
            bottles -= 1;
            System.out.printf("And %d bottle%s left on the wall. \n", bottles, this.getBottleWordEnd(bottles));
            System.out.println("New round...");
        }
        System.out.println("And no more beer on the wall");
    }

    private String getBottleWordEnd(int numOfBottles) {
        return numOfBottles == 1 ? "" : "s";
    }
}
