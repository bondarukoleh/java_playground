# Networking
## Connecting to a Server

### Using Telnet
The telnet program is a great debugging tool for network programming.

### Connecting to a Server with Java
The key statements of this simple program are these:
```java
var s = new Socket("time-a.nist.gov", 13);
InputStream inStream = s.getInputStream();
```
The first line opens a _socket_. Once the socket is open, the getInputStream method in java.net.Socket returns an
InputStream object that you can use just like any other stream.

This process continues until the stream is finished and the server disconnects.

### Socket Timeouts
Reading from a socket blocks until data are available. You can decide what timeout value is reasonable
```java
var s = new Socket(. . .);
s.setSoTimeout(10000); // time out after 10 seconds
```

There is one additional timeout issue that you need to address. The constructor
```java
Socket(String host, int port)
```
can block indefinitely until an initial connection to the host is established. You can overcome this problem by first
constructing an unconnected socket and then connecting it with a timeout:
```java
var s = new Socket();
s.connect(new InetSocketAddress(host, port), timeout);
```

## Implementing Servers
### Server Sockets
A server program, when started, waits for a client to attach to its port.
```java
var s = new ServerSocket(8189);
```
The command
```java
Socket incoming = s.accept();
```
tells the program to wait indefinitely until a client connects to that port. this method returns a Socket object that
represents the connection that was made. You can use this object to get input and output streams, as is shown in the
following code:
```java
InputStream inStream = incoming.getInputStream();
OutputStream outStream = incoming.getOutputStream();
```
Everything that the server sends to the server output stream becomes the input of the client program, and all the output
from the client program ends up in the server input stream.
```java
var in = new Scanner(inStream, StandardCharsets.UTF_8);
var out = new PrintWriter(new OutputStreamWriter(outStream, StandardCharsets.UTF_8), true /* autoFlush */);
```
Let’s send the client a greeting:
```java
out.println("Hello! Enter BYE to exit.");
```
In the end, we close the incoming socket.
```java
incoming.close();
```

### Serving Multiple Clients
Every time we know the program has established a new socket connection that is, every time the call to `accept()`
returns a socket—we will launch a new thread to take care of the connection between the server and that client.

```java
while (true) {
    Socket incoming = s.accept();
    var r = new ThreadedEchoHandler(incoming);
    var t = new Thread(r);
    t.start();
}
```

```java
class ThreadedEchoHandler implements Runnable {
    . . .
    public void run() {
        try (InputStream inStream = incoming.getInputStream(); OutputStream outStream = incoming.getOutputStream()) {
            // Process input and send response
        }
        catch(IOException e) {
        //Handle exception
        }
    }
}
```

> You can achieve greater server throughput by using features of the java.nio package

### Half-Close
The half-close allows one end of a socket connection to terminate its output while still receiving data from the other
end.
Suppose you transmit data to the server but you don’t know at the outset how much data you have.

The half-close solves this problem. You can close the output stream of a socket, thereby indicating to the server the
end of the requested data, but keep the input stream open.

### Interruptible Sockets
Similarly, when you read data through a socket, the current thread blocks until the operation is successful or has timed
out. (There is no timeout for writing data.)

To interrupt a socket operation, use a SocketChannel, from java.nio package
```java
SocketChannel channel = SocketChannel.open(new InetSocketAddress(host, port));
```
A channel does not have associated streams. Instead, it has read and write methods that make use of _Buffer_ objects.
If you don’t want to deal with buffers, you can use the Scanner class
```java
var in = new Scanner(channel, StandardCharsets.UTF_8);
```
To turn a channel into an output stream, use the static _Channels.newOutputStream_ method.
```java
OutputStream outStream = Channels.newOutputStream(channel);
```
That’s all you need to do. Whenever a thread is interrupted during an open, read, or write operation, the operation
does not block, but is terminated with an exception.

## Getting Web Data
### URLs and URIs
The _URL_ and _URLConnection_ classes encapsulate much of the complexity of retrieving information from a remote site.
You can construct a URL object from a string:
```java
var url = new URL(urlString);
```
If you simply want to fetch the contents of the resource, use the `openStream` method of the _URL_ class. This method
yields an _InputStream_ object. 
```java
InputStream inStream = url.openStream();
var in = new Scanner(inStream, StandardCharsets.UTF_8);
```
A _URI_ is a purely syntactical construct that contains the various parts of the string specifying a web resource. A _URL_
is a special kind of _URI_, namely one with sufficient information to locate a resource.

The _URI_ class has no methods for accessing the resource that the identifier specifies — its sole purpose is parsing.
In contrast, the _URL_ class can open a stream to the resource. For that reason, the _URL_ class only works with schemes
that the Java library knows how to handle, such as http:, https:, ftp:, the local file system (file:), and JAR files (jar:).
```text
http:/google.com?q=Beach+Chalet
ftp://username:password@ftp.yourserver.com/pub/file.txt
```
The _URI_ specification
```text
[scheme:]schemeSpecificPart[#fragment]
```

If the _scheme_: part is present, the URI is called _absolute_. Otherwise, it is called relative.

All absolute nonopaque URIs and all relative URIs are _hierarchical_. The _schemeSpecificPart_ of a hierarchical _URI_ has
the structure
```text
[//authority][path][?query]
```

For server-based URIs, the authority part has the form
```text
[user-info@]host[:port]
```
You can combine the absolute + relative URI
```text
http://docs.mycompany.com/api/java/net/Socket.html#Socket()
```
This process is called _resolving a relative URL_. The opposite process is called _relativization_.

### Using a URLConnection to Retrieve Information
_URLConnection_ class gives you much more control than the basic _URL_ class.

When working with a URLConnection object, you must carefully schedule your steps:
1. Call the openConnection method of the URL class to obtain the URLConnection object:
   ```java
   URLConnection connection = url.openConnection();
    ```
2. Set any request properties, using the methods
   setDoInput, setDoOutput, setIfModifiedSince, setUseCaches, setAllowUserInteraction, setRequestProperty, setConnectTimeout
   setReadTimeout
3. Connect to the remote resource by calling the connect method:
   ```java
    connection.connect();
   ```
4. After connecting to the server, you can query the header information.
   getContentType, getContentLength, getContentEncoding, getDate, getExpiration, getLastModified
5. Finally, you can access the resource data. Use the getInputStream method to obtain an input stream for reading the
   information.

The URLConnection several methods set properties of the connection before connecting to the server. The most important
ones are setDoInput and setDoOutput. By default, the connection yields an input stream for reading from the server but
no output stream for writing. If you want an output stream (for example, for posting data to a web server), you need
to call
```java
connection.setDoOutput(true);
```
Next, you may want to set some of the request headers.

Finally, you can use the catch-all `setRequestProperty` method to set any name/value pair that is meaningful for
the particular protocol. \
For example, if you want to access a password-protected web page, you must do the following:
```java
// Concatenate the user name, a colon, and the password
String input = username + ":" + password;
// Compute the Base64 encoding of the resulting string
Base64.Encoder encoder = Base64.getEncoder();
String encoding = encoder.encodeToString(input.getBytes(StandardCharsets.UTF_8));
// Call the setRequestProperty
connection.setRequestProperty("Authorization", "Basic " + encoding);
```
Once you call the connect method, you can query the response header information.
```java
String key = connection.getHeaderFieldKey(n);
```
gets the nth key from the response header, where n starts from **1**! It returns null if n is zero or greater than the
total number of header fields. There is no method to return the number of fields; you simply keep calling `getHeaderFieldKey`
until you get null. Similarly, the call 
```java
String value = connection.getHeaderField(n);
```
returns the nth value. \
The method `getHeaderFields` returns a _Map_ of response header fields.
```java
Map<String,List<String>> headerFields = connection.getHeaderFields();
```

## The HTTP Client
### The HttpClient Class
```java
HttpClient client = HttpClient.newBuilder()
    .followRedirects(HttpClient.Redirect.ALWAYS)
    .build();
```
That is, you get a builder, call methods to customize the item that is going to be built, and then call the build method
to finalize the building process. This is a common pattern for constructing immutable objects.


### The HttpRequest class and Body Publishers
You also follow the builder pattern for formulating requests. Here is a GET request:
```java
HttpRequest request = HttpRequest.newBuilder()
    .uri(new URI("http://horstmann.com"))
    .GET()
    .build();
```

### The HttpResponse Interface and Body Handlers
When sending the request, you have to tell the client how to handle the response. \
If you just want the body as a string:
```java
HttpResponse<String> response = client.send(request,
HttpResponse.BodyHandlers.ofString());
```

### Asynchronous Processing
You can process the response asynchronously. When building the client, provide an executor:
```java
ExecutorService executor = Executors.newCachedThreadPool();
HttpClient client = HttpClient.newBuilder().executor(executor).build();
```

Build a request and then invoke the `sendAsync` method on the client. You receive a _CompletableFuture<HttpResponse<T>>_,
where `T` is the type of the body handler. Use the _CompletableFuture API_. 
```java
HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
```

