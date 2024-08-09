import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    @BeforeAll
    public static void someSetup() {
        System.out.println("THIS IS THE SETUP IN BASE TEST");
    }
}
