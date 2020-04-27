Maven - program that compiles and builds you project. You can build your java code from console, via IDEA, or via some
helper program like Ant, Maven, or Gradle.

Many projects are using maven as a build tool, and it needs some configuration for itself. To not create this structure
by yourself, we can use mvn to generate a project.
Maven parses settings file, understands what is going on and passes control to his plugins.

```shell script
mvn archetypr:generate;
```

groupId - url of your company. For example if it would google - it would be "com.google".
artifactId - name of your project.

pom.xml - file with maven settings.

After project structure with simple code is generated we can compile it from root folder. 
```shell script
mvn compile;
mvn test; #to run the tests
mvn package; #to create a jar file
java -cp target/my_jar_file.jar com.olehbondaruk.App
         #class pass            #what class I want to run    
```

src/main/java - for app code
src/main/resources - for media, html, and other data
src/main/test - for tests
