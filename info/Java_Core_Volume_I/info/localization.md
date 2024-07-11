# Localization

## Time
In Java, an Instant represents a point on the time line.
### Local Dates
There are two kinds of human time in the Java API, local date/time and zoned time. Local date/time has a date and/or
time of day, but no associated time zone information. An example of a local date is June 14, 1903

In contrast, July 16, 1969, 09:32:00 EDT (the launch of Apollo 11) is a zoned date/time, representing a precise instant
on the time line.

A _LocalDate_ is a date with a year, month, and day of the month. To construct one, you can use the now or of static 
methods:
```java
LocalDate today = LocalDate.now(); // Today's date 
LocalDate alonzosBirthday = LocalDate.of(1903, 6, 14); 
alonzosBirthday = LocalDate.of(1903, Month.JUNE, 14); // Uses the Month enumeration
```

Recall that the difference between two time instants is a Duration. The equivalent for local dates is a _Period_

### Date Adjusters
The _TemporalAdjusters_ class provides a number of static methods for common adjustments.
```java
LocalDate firstTuesday = LocalDate.of(year, month, 1).with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
```
As always, the with method returns a new LocalDate object without modifying the original.

### Local Time
A LocalTime represents a time of day, such as 15:30:00. There is a LocalDateTime class representing a date and time.

### Zoned Time
The Internet Assigned Numbers Authority (IANA) keeps a database of all known time zones around the world. Java uses the
IANA database. \
Each time zone has an ID, such as America/New_York or Europe/Berlin.

### Formatting and Parsing
The DateTimeFormatter class provides three kinds of formatters to print a date/time value:
- Predefined standard formatters
- Locale-specific formatters 
- Formatters with custom patterns

```java
formatted = formatter.withLocale(Locale.FRENCH).format(apollo11launch); // 16 juillet 1969 09:32:00 EDT
formatter = DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm");
```

## Internationalization
### Specifying Locales
A locale is made up of up to five components:
1. A language, specified by two or three lowercase letters, such as en (English), de (German), or zh (Chinese).
2. Optionally, a script, specified by four letters with an initial uppercase, such as Latn (Latin), Cyrl (Cyrillic).
3. Optionally, a country or region, specified by two uppercase letters or three digits, such as US or CH (Switzerland).
4. Optionally, a variant, specifying miscellaneous features such as dialects or spelling rules.
5. Optionally, an extension that describe local preferences for calendars, numbers, and so on.

Locales are described by tags—hyphenated strings of locale elements such as en-US or de-DE.

### Message Formatting
```java
String msg = MessageFormat.format("On {2}, a {0} destroyed {1} houses and caused {3} of damage.",
        "hurricane", 99, new GregorianCalendar(1999, 0, 1).getTime(), 10.0E8);
// or
"On {2,date,long}, a {0} destroyed {1} houses and caused {3,number,currency} of damage."
// prints: On January 1, 1999, a hurricane destroyed 99 houses and caused $100,000,000 of damage.
```
In general, the placeholder index can be followed by a type and a style.

### Text Input and Output
On Windows, text files are expected to use \r\n at the end of each line, where UNIX-based systems only require a \n
character.

### Resource Bundles
When localizing an application, you’ll probably have a large number of message strings, button labels, and so on, that
all need to be translated. You’ll want to define the message strings in an external location, usually called a _resource_.

When localizing an application, you produce a set of _resource bundles_. Each bundle is a property file or a class that
describes locale-specific items (such as messages, labels, and so on). For each bundle, you have to provide versions for
all locales that you want to support.

### Property Files
Internationalizing strings is quite straightforward. You place all your strings into a property file such as 
MyProgramStrings.properties. This is simply a text file with one key/value pair per line. A typical file would look like
this:
```text
computeButton=Rechnen
colorName=black
defaultPaperSize=210×297
```
Then you name your property files:
```text
MyProgramStrings.properties
MyProgramStrings_en.properties
MyProgramStrings_de_DE.properties
```
You can load the bundle simply as
```java
ResourceBundle bundle = ResourceBundle.getBundle("MyProgramStrings", locale);
```

### Bundle Classes
To provide resources that are not strings, define classes that extend the ResourceBundle class. Use the standard naming
convention to name your classes, for example
```text
MyProgramResources.java
MyProgramResources_en.java
MyProgramResources_de_DE.java
```
Load the class with the same `getBundle` method that you use to load a property file:
```java
ResourceBundle bundle = ResourceBundle.getBundle("MyProgramResources", locale);
```




