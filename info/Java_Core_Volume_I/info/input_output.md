# Input and Output

The naming of the streams is crooked a bit. Input/Output - it's not from the human Java User perspective, it's from the 
Java program's perspective. Input stream - it's Java opens some source and getting the input from it. Output - it's Java
provides something out to a destination.

In the Java API, an object from which we can read a sequence of bytes is called an input stream. An object to which we
can write a sequence of bytes is called an output stream. These sources and destinations of byte sequences can be, and
often are files, but they can also be network connections and even blocks of memory. The abstract classes InputStream
and OutputStream are the basis for a hierarchy of input/output (I/O) classes.

A separate hierarchy provides classes, inheriting from the abstract Reader and Writer classes, for processing Unicode
characters. These classes have read and write operations that are based on two-byte char values (that is, UTF-16 code
units) rather than byte values.

## Reading and Writing Bytes
The InputStream class has an abstract method:

```java
abstract int read();
```

This method reads one byte and returns the byte that was read, or -1 if it encounters the end of the input source. The
designer of a concrete input stream class overrides this method to provide useful functionality. For example, in the
`FileInputStream` class, this method reads one byte from a file. `System.in` is a predefined object of a subclass of 
InputStream that allows you to read information from “standard input,” that is, the console or a redirected file.

The `InputStream` class also has nonabstract methods to read an array of bytes or to skip a number of bytes. Since Java 9,
there is a very useful method to read all bytes of a stream:
```java
byte[] bytes = in.readAllBytes();
```

There are also methods to read a given number of bytes. Similarly, the `OutputStream` class defines the abstract method
```java
abstract void write(int b);
```

Which writes one byte to an output location. If you have an array of bytes, you can write them all at once:
```java
byte[] values = . . .;
out.write(values);
```

The `transferTo` method transfers all bytes from an input stream to an output stream:
```java
in.transferTo(out);
```

Both the `read` and `write` methods block until the byte is actually read or written. This means that if the input stream
cannot immediately be accessed (usually because of a busy network connection), the current thread blocks.

The available method lets you check the number of bytes that are currently available for reading. This means a fragment
like the following is unlikely to block:
```java
int bytesAvailable = in.available();
if (bytesAvailable > 0) {
    var data = new byte[bytesAvailable];
    in.read(data);
}
```

When you have finished reading or writing to an input/output stream, close it by calling the `close` method. This call 
frees up the operating system resources that are in limited supply. Closing an output stream also flushes the buffer,
manually flush the output with the `flush` method.

Raw read and write functions rarely use them. The data that you are interested in probably contain numbers, strings,
and objects, not raw bytes.

### The Complete Stream Zoo
Java has a whole zoo of more than 60 (!) different input/output stream types. Separate hierarchies for bytes and
characters. To read and write strings and numbers, you need `DataInputStream` and `DataOutputStream` let you read and write
all the primitive Java types in binary format. \
The `ZipInputStream` and `ZipOutputStream` let you read and write files in the familiar ZIP compression format.

There are four additional interfaces: `Closeable`, `Flushable`, `Readable`, and `Appendable`

![input_output_hierarchy](/info/Java_Core_Volume_I/info/media/input_output/input_output_hierarchy.jpg)

![reader_writer_hierarchy](/info/Java_Core_Volume_I/info/media/input_output/reader_writer_hierarchy.jpg)

![other_interfaces](/info/Java_Core_Volume_I/info/media/input_output/closeable_Flushable_Readable_Appendable.jpg)

`OutputStream` and `Writer` implement the `Flushable` interface. The `Readable` interface has a single method
```java
int read(CharBuffer cb)
```

The `Appendable` interface has two methods for appending single characters and character sequences:

Appendable append(char c)
Appendable append(CharSequence s)

The `CharBuffer` class has methods for sequential and random read/write access. It represents an in-memory buffer or a 
memory-mapped file.

The `CharSequence` interface describes basic properties of a sequence of char values. It is implemented by String, 
CharBuffer, StringBuilder, and StringBuffer.

Of the input/output stream classes, only `Writer` implements `Appendable`.

### Combining Input/Output Stream Filters
`FileInputStream` and `FileOutputStream` give you input and output streams attached to a disk file. You need to pass the 
file name or full path name of the file to the constructor.
```java
var fin = new FileInputStream("employee.dat");
```

> All the classes in java.io interpret relative path names as starting from the user’s working directory. You can get
> this directory by a call to System.getProperty("user.dir").

> Since the backslash character is the escape character in Java strings, be sure to use \\ for Windows-style path names
> (for example, C:\\Windows\\win.ini). In Windows, you can also use a single forward slash (C:/Windows/win.ini) because
> most Windows file-handling system calls will interpret forward slashes as file separators. However, this is not 
> recommended

But just as the `FileInputStream` has no methods to read numeric types, the `DataInputStream` has no method to get data from
a file.

You have to combine the two. For example, to be able to read numbers from a file, first create a `FileInputStream` and 
then pass it to the constructor of a `DataInputStream`.
```java
var fin = new FileInputStream("employee.dat");
var din = new DataInputStream(fin);
double x = din.readDouble();
```

You can add multiple capabilities by nesting the filters. If you want buffering and the data input methods for a file
```java
var din = new DataInputStream(new BufferedInputStream(new FileInputStream("employee.dat")));
```

Notice that we put the `DataInputStream` last in the chain of constructors because we want to use the `DataInputStream` 
methods, and we want them to use the buffered read method.

If you often need to peek at the next byte to see if it is the value that you expect. Java provides the 
`PushbackInputStream` for this purpose.
```java
var pbin = new PushbackInputStream(new BufferedInputStream(new FileInputStream("employee.dat")));
```

Now you can speculatively read the next byte
```java
int b = pbin.read();
```

and throw it back if it isn’t what you wanted.
```java
if (b != '<') pbin.unread(b);
```

However, reading and unreading are the only methods that apply to a pushback input stream. If you want to look ahead and
also read numbers, then you need both a pushback input stream and a data input stream reference.

### Text Input and Output
When saving data, you have the choice between binary and text formats. For example, if the integer 1234 is saved in
binary, it is written as the sequence of bytes 00 00 04 D2 (in hexadecimal notation). In text format, it is saved as 
the string "1234".

When saving text strings, you need to consider the character encoding. In the UTF-16 encoding that Java uses internally,
the string "José" is encoded as 00 4A 00 6F 00 73 00 E9 (in hex). However, many programs expect that text files use a
different encoding. In UTF-8, the encoding most commonly used on the Internet it is different.

The `OutputStreamWriter` class turns an output stream of Unicode code units into a stream of bytes, using a chosen character
encoding. Conversely, the `InputStreamReader` class turns an input stream that contains bytes (specifying characters in
some character encoding) into a reader that emits Unicode code units.

```java
var in = new InputStreamReader(new FileInputStream("data.txt"), StandardCharsets.UTF_8);

```

### How to Write Text Output
For text output, use a `PrintWriter`. That class has methods to print strings and numbers in text format.
```java
var out = new PrintWriter("employee.txt", StandardCharsets.UTF_8);
```

To write to a print writer
```java
String name = "Harry Hacker";
double salary = 75000;
out.print(name);
out.print(' ');
out.println(salary);
```


This writes the characters
```java
Harry Hacker 75000.0
```

to the writer out. The characters are then converted to bytes and end up in the file employee.txt.

The `print` methods don’t throw exceptions. You can call the `checkError` method to see if something went wrong with the
output stream.

>Java veterans had PrintStream class.

### How to Read Text Input
The easiest way to process arbitrary text is the `Scanner` class. You can construct a `Scanner` from any input stream.
Alternatively, you can read a short text file into a string like this:
```java
String content = Files.readString(path, charset);
```
But if you want the file as a sequence of lines, call

 ```java
List<String> lines = Files.readAllLines(path, charset);
```

If the file is large, process the lines lazily as a Stream<String>:

```java
try (Stream<String> lines = Files.lines(path, charset)) {
    . . .
}
```

You can also use a scanner to read tokens—strings that are separated by a delimiter. The default delimiter is whitespace.
You can change the delimiter to any regular expression. For example,
```java
Scanner in = . . .;
in.useDelimiter("\\PL+");
```

Calling the next method yields the next token:
```java
while (in.hasNext()) {
String word = in.next();
. . .
}
```

Alternatively, you can obtain a stream of all tokens as
```java
Stream<String> words = in.tokens();
```

> In early Java, there was the BufferedReader class.

### Saving Objects in Text Format
```text
Harry Hacker|35500|1989-10-01
Carl Cracker|75000|1987-12-15
Tony Tester|38000|1990-03-15
```
Since we write to a text file, we use the `PrintWriter` class.
```java
public static void writeEmployee(PrintWriter out, Employee e)  {
    out.println(e.getName() + "|" + e.getSalary() + "|" +
    e.getHireDay());
}
```


To read records, we read in a line at a time and separate the fields. We use a scanner to read each line and then split
the line into tokens with the `String.split` method.
```java
public static Employee readEmployee(Scanner in) {
    String line = in.nextLine();
    String[] tokens = line.split("\\|");
    String name = tokens[0];
    double salary = Double.parseDouble(tokens[1]);
    LocalDate hireDate = LocalDate.parse(tokens[2]);
    int year = hireDate.getYear();
    return new Employee(name, salary, year, month, day);
}
```

### Character Encodings
Input and output streams are for sequences of bytes, but in many cases you will work with texts — that is, sequences of
characters. It then matters how characters are encoded into bytes.

Java uses the Unicode standard for characters. Each character, or “code point,” has a 21-bit integer number. There are
different character encodings—methods for packaging those 21-bit numbers into bytes.

The most common encoding is UTF-8, which encodes each Unicode code point into a sequence of one to four bytes. UTF-8
has traditional ASCII character set, contains all characters used in English, only take up one byte each.

Another common encoding is UTF-16, which encodes each Unicode code point into one or two 16-bit values

In addition to the UTF encodings, there are partial encodings that cover a character range suitable for a given user
population. For example, ISO 8859-1 is a one-byte code that includes accented characters used in Western European 
languages. Shift-JIS is a variable-length code for Japanese

Therefore, you should always explicitly specify the encoding e.g. the _Content-Type_ header.

The StandardCharsets class has static
```text
StandardCharsets.UTF_8
StandardCharsets.UTF_16
StandardCharsets.UTF_16BE
StandardCharsets.UTF_16LE
StandardCharsets.ISO_8859_1
StandardCharsets.US_ASCII
```

> Some methods (such as the String(byte[]) constructor) use the default platform encoding if you don't specify any;
> others (such as Files.readAllLines) use UTF-8.

> Beyond Java 17, UTF-8 will become the JDK default

## Reading and Writing Binary Data
Text format is convenient for testing and debugging, but it is not as efficient as transmitting data in binary format.

### The DataInput and DataOutput Interfaces
The `DataOutput` interface defines the following methods for writing a number, a character, a boolean value, or a string
in binary format.

The resulting output is not human-readable, but it will use the same space for each value of a given type and reading it
back in will be faster than parsing text.

To read the data back in, use the following methods defined in the `DataInput` interface

```java
readInt readDouble readShort readChar readLong readBoolean readFloat readUTF
```

The `DataInputStream` class implements the `DataInput` interface. To read binary data from a file, combine a `DataInputStream`
with a source of bytes such as a `FileInputStream`:
```java
var in = new DataInputStream(new FileInputStream("employee.dat"));
```

Similarly, to write binary data, use the `DataOutputStream` class that implements the `DataOutput` interface:
```java
var out = new DataOutputStream(new FileOutputStream("employee.dat"));
```

### Random-Access Files
The `RandomAccessFile` class lets you read or write data anywhere in a file. Disk files are random-access, but input/output
streams that communicate with a network socket are not. You can open a randomaccess file either for reading only or for
both reading and writing; string "`r`" (for read access) or "`rw`" (for read/write access)
```java
var in = new RandomAccessFile("employee.dat", "r");
var inOut = new RandomAccessFile("employee.dat", "rw");
```

When you open an existing file as a `RandomAccessFile`, it does not get deleted.

A random-access file has a file pointer that indicates the position of the next byte to be read or written. The seek
method can be used to set the file pointer to an arbitrary byte position within the file. It is like a caret in the file,
that you need to move.

The `getFilePointer` method returns the current position of the file pointer. \
The `RandomAccessFile` class implements both the `DataInput` and `DataOutput` interfaces. To read and write from a random-
access file, use methods such as `readInt/writeInt` and `readChar/writeChar`

For example a program stores employee records in a random-access file. Suppose you want to position the file pointer to
the third `record.Simply` set the file pointer to the appropriate byte position and start reading.
```java
long n = 3;
in.seek((n - 1) * RECORD_SIZE); // size in bytes of each employee record in file
var e = new Employee();
e.readData(in);
```


To determine the total number of bytes in a file, use the `length` method. The total number of records is the length 
divided by the size of each record.
```java
long nbytes = in.length(); // length in bytes
int nrecords = (int) (nbytes / RECORD_SIZE);
```


Integers and floating-point values have a fixed size in binary format, but we have to work harder for strings.

The `writeFixedString` writes the specified number of code units, starting at the beginning of the string. If there are
too few code units, the method pads the string, using zero values.

The `readFixedString` method reads characters from the input stream until it has consumed size code units or until it
encounters a character with a zero value. Then, it skips past the remaining zero values in the input field.

### ZIP Archives
ZIP archives store one or more files in a (usually) compressed format. Each ZIP archive has a header with information
such as the name of each file and the compression method that was used. In Java, you can use a `ZipInputStream` to read a
ZIP archive. You need to look at the individual entries in the archive. The `getNextEntry` method returns an object of
type `ZipEntry` that describes the entry. Read from the stream until the end, which is actually the end of the current 
entry. Then call `closeEntry` to read the next entry. Do not close zin until you read the last entry. Here is a typical
code sequence to read through a ZIP file:
```java
var zin = new ZipInputStream(new FileInputStream(zipname));
boolean done = false;
while (!done) {
    ZipEntry entry = zin.getNextEntry();
    if (entry == null) done = true;
    else {
        // read the contents of zin
        zin.closeEntry();
    }
}
zin.close();
```

To write a ZIP file, use a `ZipOutputStream`. For each entry that you want to place into the ZIP file, create a `ZipEntry`
object 
```java
var fout = new FileOutputStream("test.zip");
var zout = new ZipOutputStream(fout);
for all files {
    var ze = new ZipEntry(filename);
    zout.putNextEntry(ze);
    // send data to zout
    zout.closeEntry();
}
zout.close();
```

> JAR files are simply ZIP files with a special entry—the so-called manifest. Use the JarInputStream and JarOutputStream
> classes to read and write the manifest entry.

The ZIP data can also come from a network connection

## Object Input/Output Streams and Serialization
Objects that you create in an objectoriented program are rarely all of the same type. For example an array called 
_staff_ that is nominally an array of `Employee` records but contains objects that are actually instances of a subclass 
such as `Manager`.

The Java language supports a very general mechanism, called object serialization, that makes it possible to write any
object to an output stream and read it again later

### Saving and Loading Serializable Objects
To save object data, you first need to open an `ObjectOutputStream` object:
```java
var out = new ObjectOutputStream(new FileOutputStream("employee.dat"));
```

Now, to save an object, simply use the `writeObject` method of the `ObjectOutputStream`:
```java
var harry = new Employee("Harry Hacker", 50000, 1989, 10, 1);
var boss = new Manager("Carl Cracker", 80000, 1987, 12, 15);
out.writeObject(harry);
out.writeObject(boss);
```


To read the objects back in, first get an `ObjectInputStream` object:
```java
var in = new ObjectInputStream(new FileInputStream("employee.dat"));
```

Then, retrieve the objects in the same order in which they were written, using the `readObject` method:
```java
var e1 = (Employee) in.readObject();
var e2 = (Employee) in.readObject();
```

To store the object that you want the class must implement the `Serializable` interface:
```java
class Employee implements Serializable { . . . }
```

The `Serializable` interface has no methods, to make a class serializable, you do not need to do anything else.

What happens when one object is shared by several objects as part of their state?

Instead, each object is saved with the serial number—hence the name object serialization for this mechanism. Here is the
algorithm:
1. Associate a serial number with each object reference that you encounter.
2. When encountering an object reference for the first time, save the object data to the output stream.
3. If it has been saved previously, just write “same as the previously saved object with serial number x.”

> Caution. For inner classes, some of these names are synthesized by the compiler, and the naming convention may change
> from one compiler to another. If that occurs, deserialization fails. Static inner classes, including inner 
> enumerations and records, are safe to serialize.

### Understanding the Object Serialization File Format

