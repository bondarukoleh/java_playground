import java.util.ArrayList;

public class TestArrayList {
    public static void main(String[] args) {
        ArrayList<Egg> basket = new ArrayList<Egg>();
        Egg egg1 = new Egg("small", "white");
        Egg egg2 = new Egg("big", "brown");

        basket.add(egg1);
        basket.add(egg1);
        basket.add(egg2);

//        TestArrayList.printList(basket);
//        System.out.println("basket.contains(egg1) " + basket.contains(egg1));
//        basket.remove(egg1);
//        System.out.println("basket.contains(egg1) " + basket.contains(egg1));
//        TestArrayList.printList(basket);

        ArrayList<Integer> ints = new ArrayList<Integer>();
        ints.add(1);
        for (int i : ints) {
            System.out.println(i);
        }

//        basket.remove();
//        basket.remove();
//        System.out.println("basket.contains(egg1)" + basket.contains(egg1));
//        System.out.println("basket.isEmpty()" + basket.isEmpty());
//        System.out.println("basket.indexOf(egg2)" + basket.indexOf(egg2));
//        System.out.println("basket.size()" + basket.size());
//        System.out.println("basket.get(1)" + basket.get(1));
    }

    public static void printList(ArrayList<Egg> eggs) {
        for (Egg egg : eggs) {
            System.out.println(egg);
        }
    }
}


class Egg {
    private String size;
    private String color;

    Egg(String size, String color) {
        this.color = color;
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Egg{" +
                "size='" + size + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}