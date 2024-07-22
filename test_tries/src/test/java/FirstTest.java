import java.nio.file.FileStore;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class FirstTest {
    static String commonErrorText = "Result should be %s";
    static int expectedResult;
    static int actualResult;
    Calculator calculator = new Calculator();

//    @Rule
//    public Timeout globalTimeout = new Timeout(3000); // 10 seconds max per test method

    @BeforeAll
    public static void beforeAll(){
        System.out.println("This is the expected result before - " + expectedResult);
    }

    @AfterAll
    public static void afterAll(){
        System.out.println("This is the expected result after - " + expectedResult);
    }

    @Test
    public void addition() {
        expectedResult = 3;
        actualResult = calculator.addition(1, 2);

        assertEquals(expectedResult, actualResult, this.getErrorMessage(expectedResult));
    }

    @Test
    @Disabled
    public void subtraction() {
        expectedResult = 1;
        actualResult = calculator.subtraction(2, 1);

        assertEquals(expectedResult, actualResult, this.getErrorMessage(expectedResult));
    }


    @Test
    @DisplayName("Test that runs longer")
    public void longRunningTest() {
        expectedResult = 1;
        for (int i = 0; i < 1000000; i++) {
            actualResult = calculator.subtraction(2, 1);
            assertEquals(expectedResult, actualResult, this.getErrorMessage(expectedResult));
        }
    }

    @Test
    public void expectedException(){
        assertThrows(ArithmeticException.class, () -> {
            int res = 1 / 0;
        });
    }

    @Disabled
    @Test
    @Tag("long")
    public void longLongRunningTest() {
        while (true);
    }

    @Disabled
    @Test
    public void hasItemTest() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("A", "B"));
        assertEquals(list, hasItem("CC"));
    }

    @Disabled
    @Test
    public void hasItemsTest() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("A", "B"));
//        assert(list, allOf(hasItem("A"), hasItem("B")));
    }

    @Disabled
    @Test
    public void failTest() {
        if(true){
            fail("I know that something goes wrong here");
        }
    }

    private String getErrorMessage(String result){
        return String.format(commonErrorText, result);
    }

    private String getErrorMessage(int result){
        return String.format(commonErrorText, result);
    }
}
