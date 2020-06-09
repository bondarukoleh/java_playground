import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FirstTest {
    @Test
    public void testAddition() {
        Calculator calculator = new Calculator();
        int result = calculator.addition(1, 2);
        System.out.println(result);
        assertEquals(String.format("Result should be %s", 3), 3, result);
    }
}
