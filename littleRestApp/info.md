# REST app

### Approximate Structure
```text
src
└── main
    ├── java
    │   └── com
    │       └── example
    │           └── simpleservice
    │               ├── controller   // Controllers go here
    │               ├── service      // Business logic goes here
    │               ├── model        // Data models/entities go here
    │               └── repository   // Data access layer goes here
    └── resources
        ├── application.properties  // Configuration files
        ├── static                  // Static assets (if any)
        └── templates               // Templates for web views (if any)
```


### Some annotations
#### @RestController
A specialized version of the _@Controller_ annotation:
- A combination of _@Controller_ and _@ResponseBody_. It tells Spring that the class is a web controller (), and all the
methods within it should return data directly in the response body rather than rendering a view.
- Handles JSON/XML Responses by Default,  automatically converts the return value of each method into JSON (or XML if
configured) and sends it as the HTTP response. Perfect for creating RESTful services.
- Simplifies REST API Development

@RequestMapping("/api"): Sets a base path for all endpoints in this controller (e.g., /api/greet).
@GetMapping("/greet"): A simple GET endpoint that returns a greeting.
@PostMapping("/echo"): A POST endpoint that takes a message in the request body and echoes it back.
@PutMapping("/update"): A PUT endpoint that simulates updating a message.
@DeleteMapping("/delete"): A DELETE endpoint that simulates deleting a message