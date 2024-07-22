import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

//@RunWith(Parameterized.class)
public class ParameterizedTest {
    private final int actual;
    private final int expected;

    public ParameterizedTest(int actual, int expected){
        this.actual = actual;
        this.expected = expected;
    }

    /* Or this way:
    @Parameterized.Parameter(0)
    public int actual;

    @Parameterized.Parameter(1)
    public int expected;
    *  */

//    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{{3, 4}, {2, 5}});
    }

    @Test
    public void test() {
        assertNotEquals(expected, expected);
    }
}
