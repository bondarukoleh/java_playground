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

### Stub priority
By default, WireMock will use the most recently added matching stub to satisfy the request. However, in some cases it is
useful to exert more control.

```json
{
    "priority": 1,
    "request": {
        "method": "GET",
        "url": "/api/specific-resource"
    },
    "response": {
        "status": 200
    }
}
```

#### Editing stubs
In Java, Existing stub mappings can be modified, provided they have been assigned an ID. Or removed.

#### Get all mapings
With http://<host>:<port>/__admin/mappings URL


## Request Matching
Stub matching and verification queries can use the following request attributes:
- URL
- HTTP Method
- Query parameters
- Form parameters
- Headers
- Basic authentication (a special case of header matching)
- Cookies
- Request body
- Multipart/form-data

```json
{
    "request": {
        "urlPath": "/mock",
        "url": "/your/url?and=query",
        "urlPattern": "/your/([a-z]*)\\?and=query",
        "method": "ANY",
        "headers": {
            "Accept": {
                "contains": "xml"
            }
        },
        "queryParameters": {
            "search_term": {
                "equalTo": "WireMock"
            }
        },
        "cookies": {
            "session": {
                "matches": ".*12345.*"
            }
        },
        "bodyPatterns": [
            {
                "equalToXml": "<search-results />"
            },
            {
                "matchesXPath": "//search-results"
            }
        ],
        "multipartPatterns": [
            {
                "matchingType": "ANY",
                "headers": {
                    "Content-Disposition": {
                        "contains": "name=\"info\""
                    },
                    "Content-Type": {
                        "contains": "charset"
                    }
                },
                "bodyPatterns": [
                    {
                        "equalToJson": "{}"
                    }
                ]
            }
        ],
        "basicAuthCredentials": {
            "username": "jeff@example.com",
            "password": "jeffteenjefftyjeff"
        },
        "formParameters": {
          "tool": {
            "equalTo": "WireMock"
          }
        }
    },
    "response": {
        "status": 200
    }
}
```

#### URL Match
- `"url": "/your/url?and=query"` - Equality matching on path and query
- `"urlPattern": "/your/([a-z]*)\\?and=query"` - Regex matching on path and query
- `"urlPath": "/your/url"` - Equality matching on the path only
- `"urlPathPattern": "/your/([a-z]*)"` - Regex matching on the path only
- `"urlPathTemplate": "/contacts/{contactId}/addresses/{addressId}"` - Path templates, powerful feature that relies on
wiremock's _The request model_, with a bunch of stuff available, that you can dynamically get from formed request.

#### JSON/XML match
You can match a lot if thing in JSON/XML, including negative matches, and regex/xpath.

### Response Templating
