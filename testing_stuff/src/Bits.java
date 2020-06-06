public class Bits {
    public static void main(String[] args) {
        int afterNot = ~10;       // bits are now 11110101
        Print.out("~ not. ~10 = " + afterNot);
        int x = 10;    // bits are 00001010
        int y =  6;    // bits are 00000110
        {
            int a = x & y; // bits are 00000010
            Print.out("& and. 10 & 6 = " + a);
        }
        {
            int a = x | y;  // bits are 00001110
            Print.out("| or. 10 | 6 = " + a);
        }
        {
            int a = x ^ y;  // bits are 00001100
            Print.out("^ xor. 10 ^ 6 = " + a);
        }
        int X = -11;   //  bits are 11110101
        {
            int a = x >> 2;  // bits are 11111101
            Print.out(">> Right Shift. -11 >> 2 = " + a);
        }
        {
            int a = x >>> 2;  // bits are 00111101
            Print.out(">>> Unsigned Right Shift. -11 >>> 2 = " + a);
        }
        {
            int a = x << 2;  // bits are 11010100
            Print.out("<< Left Shift. -11 << 2 = " + a);
        }
    }
}

class Print {
    public static void out(String toPrint) {
        System.out.println(toPrint);
    }
}

