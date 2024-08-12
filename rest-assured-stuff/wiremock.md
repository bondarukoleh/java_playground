# Wiremock
WireMock is a library that acts as a server that you can configure to return specific responses for HTTP requests.
This allows you to simulate external APIs, making your tests more reliable and independent of external factors.

WireMock can be run in two main ways: as a _standalone server_ or _embedded_ in your tests.
- Standalone Mode:
You can run WireMock as a standalone server, and then configure your application to point to it. This is useful when you
want to test the application in an environment where the real service is not available.
- Embedded Mode (Typical for Unit Tests):
You can start WireMock directly within your test cases.
```java
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

public class MyServiceTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8080);

    @Test
    public void testMyService() {
        stubFor(get(urlEqualTo("/my/resource"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withBody("Hello World")));

        // Your test logic here, e.g., calling the service that hits WireMock
    }
}
```