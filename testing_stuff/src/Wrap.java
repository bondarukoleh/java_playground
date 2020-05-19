public class Wrap {
    public static void main(String[] args) {
        int i = 42;
        Integer integer = Integer.valueOf(i);
        int unboxed = Wrap.absoluteValue(integer);
        int unboxed2 = integer;

        int primitive = Wrap.returnIntWrapper(i);

        boolean parsed = Boolean.valueOf("true");

        String stringFromNum = 42 + "";
        String stringFromNum2 = Integer.toString(42);
        System.out.println(stringFromNum);
        System.out.println(stringFromNum instanceof String); // true
        System.out.println(stringFromNum2 instanceof String); // true
    }

    static int absoluteValue(int i){
        return (i < 0) ? -i : i;
    }

    static Integer returnIntWrapper(int i){
        return Integer.valueOf(i);
    }
}

