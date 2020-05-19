package src;

public class Format {
    public static void main(String[] args) {
//        System.out.println(String.format("%,d %d", 1230000000, 1230000000)); //1,230,000,000
//        System.out.println(String.format("%.2f", 1230000000.098098)); //1230000000.10
//        System.out.println(String.format("%.3f", 1230000000.098098)); //1230000000.098
//        System.out.println(String.format("%,.3f", 1230000000.098098)); //1,230,000,000.098
//        System.out.println(String.format("%,d", 1230000000)); //1,230,000,000.098
        System.out.println(String.format("%2$,6.1f", 24.000, 30.000, 42.000)); //1,230,000,000.098
    }
}
