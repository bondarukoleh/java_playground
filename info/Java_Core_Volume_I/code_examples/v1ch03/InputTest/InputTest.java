//import java.util.*;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.Arrays;

/**
 * This program demonstrates console input.
 *
 * @author Cay Horstmann
 * @version 1.10 2004-02-10
 */
public class InputTest {
    public static void main(String[] args) throws IOException {
//        Scanner in = new Scanner(System.in);
//      System.out.print("What is your name? ");
//      String name = in.nextLine();
//      System.out.print("What is your pass? ");
//      String pass = in.nextLine();
//      System.out.printf("""
//              Hello, "%1$s", Next year, you'll be "%s. So you like it, %1$s?"
//              """, name, pass);
//      System.out.printf("Hello \"%s\"%n%n", name);
//        Scanner fileScanner = new Scanner(Path.of("InputTest.iml"), StandardCharsets.UTF_8);
//       System.out.println(fileScanner.nextLine());
//       try {
//          PrintWriter writer = new PrintWriter("test.txt");
//          writer.println("SDASDASD");
//          writer.close();
//       } catch (IOException e) {
//          System.out.println(e.getMessage());
//       }
//        int[] a = {1, 2, 3}

        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < i + 1; j++) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }
}
