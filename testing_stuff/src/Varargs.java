public class Varargs {
    public static void main(String[] args) {
        Varargs.varargsMethod("some string", 1, 2, 3);
        Varargs.varargsMethod("some string", 1, 2, 3, 4, 5);
    }

    static void varargsMethod(String some, int ...arr){
        System.out.println(arr instanceof int[]);
    }
}
