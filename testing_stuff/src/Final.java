public class Final {
    public static void main(String[] args) {
//        new WithFinalStuff().tryToRewrite = "asdas"; error
    }
}


final class SomeFinal {}

//class TryToExtend extends SomeFinal {} error
class WithFinalStuff {
    public final String tryToRewrite = "Aha";
    public final String finalName;
    public static String staticSurname;

    static {
        staticSurname = "Smith";
    }

    public WithFinalStuff(String name) {
        finalName = name;
    }

    public final void tryToOverride(){
        System.out.println("Final method");
    }

    public void tryToOverride(int a){
        System.out.println("Final method");
    }

    public void regular(){

    }
}

class ExtendingWithFinalStuff extends WithFinalStuff {
    //@Override
    //public final void tryToOverride(){} error


    public ExtendingWithFinalStuff(String name) {
        super(name);
    }

    @Override
    public final void regular() {
        super.regular();
    }
}
