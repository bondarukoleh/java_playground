import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class ExecutionOrderTest {
    @Test
    @Order(2)
    void testA() {
        System.out.println("Test A");
    }

    @Test
    @Order(1)
    void testB() {
        System.out.println("Test B");
    }
}



