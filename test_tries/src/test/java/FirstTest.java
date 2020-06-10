import org.hamcrest.Matcher;
import org.junit.*;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

public class FirstTest {
    static String commonErrorText = "Result should be %s";
    static int expectedResult;
    static int actualResult;
    Calculator calculator;

//    @Rule
//    public Timeout globalTimeout = new Timeout(3000); // 10 seconds max per test method

    public FirstTest() {
        System.out.println("NEW INSTANCE");
        this.calculator = new Calculator();
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

    private String getErrorMessage(String result){
        return String.format(commonErrorText, result);
    }

    private String getErrorMessage(int result){
        return String.format(commonErrorText, result);
    }
}
