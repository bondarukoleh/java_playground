import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestInstanceTests {

    private int sharedResource;

    @BeforeAll
    void setUpAll() {
        sharedResource = 42;
        System.out.println("Before all tests");
    }

    @BeforeEach
    void setUpEach() {
        System.out.println("Before each test");
    }

    @Test
    void test1() {
        System.out.println("Test 1, sharedResource: " + sharedResource);
    }

    @Test
    void test2() {
        System.out.println("Test 2, sharedResource: " + sharedResource);
    }
}