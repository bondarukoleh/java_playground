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
Response headers and bodies, as well as proxy URLs, can optionally be rendered using _Handlebars templates_.
This can help with generating the data instead of hard coding it. \
The response will be templated, replacing {{request.query.name}} with the value of the name query parameter. \
Transformers line tells WireMock to apply the response templating to this particular stub.
```json
{
  "request": {
    "method": "GET",
    "url": "/some/endpoint"
  },
  "response": {
    "status": 200,
    "body": "Hello, {{request.query.name}}!",
    "transformers": ["response-template"]
  }
}
```

There is a ton of helpers that can generate any data you like, dates, names, numbers, random values, arrays. There is 
conditionals. 

#### Simulating Faults
Delays, chunked responses, different MALFORMED and corrupted responses.

#### Stateful Behaviour
Scenarios - A scenario is essentially a state machine whose states can be arbitrarily assigned. Its starting state is
always Scenario.STARTED. Stub mappings can be configured to match on scenario state, such that stub A can be returned
initially, then stub B once the next scenario state has been triggered.
You can reset scenario individually and all of them.

#### Proxy
You can selectively proxy and intercept requests to other services, but not the body of the request. There is ability
to record te requests. Wiremock can be setup as forward proxy (difference with regular, that wiremock is a gatekeeper,
and in control of all requests, not just passing along) for browser.
To change the body of the requests, you need to register to wiremock a request transformer that uses RequestWrapper,
where you will filter and change whatever you like in the request before sending it.

#### Verify requests
You can verify the requests to Wiremock weather it got them or not, how many requests are matched and so on.
You can find the requests by this URL http://host:port/__admin/requests. Some queries available:
- http://localhost:8080/__admin/requests?since=2016-06-06T12:00:00&limit=3
- http://localhost:8080/__admin/requests?unmatched=true
- http://localhost:8080/__admin/requests/unmatched for unmatched
- /__admin/requests/unmatched/near-misses for near misses
- http://localhost:8080/__admin/requests?matchingStub=59651373-6deb-4707-847c-9e8caf63266e

You can post a search criteria to get filter out something special
POST to http://<host>:<port>/__admin/requests/find
```json
{
    "method": "POST",
    "url": "/resource/to/find",
    "headers": {
        "Content-Type": {
            "matches": ".*/xml"
        }
    }
}
```
You can find near matched requests.
You can remove the records from logs.

### Using templates in WireMock 
There are some templates pre-setup for some big platforms, if you need to mock them.

### Record and Playback
WireMock can create stub mappings from requests it has received. Combined with its proxying feature this allows you to 
“record” stub mappings from interaction with existing APIs. \
Two approaches are available: Recording or snapshotting.

You can setup record on http://localhost:8080/__admin/recorder. After the start the recording from some URL, you need to
send requests to Wiremock, and it will proxy them to URL, recording the responses in the same time. Once you click stop -
new stubs will appear in mappings. This all can be setup via Wiremock API also.

Snapshoting - is to create a snapshot of current state of wiremock. It has a powerful filtering and preparing settings
where you can choose what requests do you need, filter and match them. Snapshoting gives you clean and configurable way
of saving the requests that were passed along by Wiremock (via Proxy in most cases). If there are a few identical requests
to the same endpoint - you can make a scenario automatically from it.

#### Webhooks and Callbacks
You can add a webhook or a callback to some stub. When something hit it, callback request is sent.

#### JWT
There is an extantion for generating JWT tokens, configurable.

#### Multi-domain Mocking
You can setup a multi-domain mocking, where one instance of wiremock will behave as a few services with different host,
with all aspects of domain interactions — like CORS, cookies, and SSL. Useful when you need to mock a few services at
once.  