import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class FirstTest {
    static String commonErrorText = "Result should be %s";
    static int expectedResult;
    static int actualResult;
    Calculator calculator = new Calculator();

//    @Rule
//    public Timeout globalTimeout = new Timeout(3000); // 10 seconds max per test method

    @BeforeClass
    public static void beforeSomething(){
        System.out.println("This is before class 1");
    }

    @BeforeClass
    public static void beforeSomething2(){
        System.out.println("This is before class 2");
    }

    @Before
    public void checkB(){
        System.out.println("This is the expected result before - " + expectedResult);
    }

    @After
    public void checkA(){
        System.out.println("This is the expected result after - " + expectedResult);
    }

    @Test
    public void addition() {
        expectedResult = 3;
        actualResult = calculator.addition(1, 2);

        assertThat(this.getErrorMessage(expectedResult), actualResult, is(expectedResult));
    }

    @Test
    public void subtraction() {
        expectedResult = 1;
        actualResult = calculator.subtraction(2, 1);

        assertThat(this.getErrorMessage(expectedResult), actualResult, is(expectedResult));
    }


    @Test(timeout = 4000) // 10 seconds
    public void longRunningTest() {
        expectedResult = 1;
        for (int i = 0; i < 1000000; i++) {
            actualResult = calculator.subtraction(2, 1);
            assertThat(this.getErrorMessage(expectedResult), actualResult, is(expectedResult));
        }
    }

    @Test(expected = RuntimeException.class)
    public void expectedException(){
        throw new RuntimeException();
    }

    @Ignore
    @Test
    public void longLongRunningTest() {
        while (true);
    }

    @Test
    public void hasItemTest() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("A", "B"));
        assertThat(list, hasItem("CC"));
    }

    @Test
    public void hasItemsTest() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("A", "B"));
        assertThat(list, allOf(hasItem("A"), hasItem("B")));
    }

    private String getErrorMessage(String result){
        return String.format(commonErrorText, result);
    }

    private String getErrorMessage(int result){
        return String.format(commonErrorText, result);
    }
}
