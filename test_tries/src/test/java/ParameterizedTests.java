import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.provider.ValueSource;

public class ParameterizedTests {
    @ParameterizedTest
    @ValueSource(ints = { 2, 4 })
    void checkEvenNumber(int number) {
        assertEquals(0, number % 2,
                "Supplied number is not an even number");
    }
}
