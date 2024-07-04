package linkedList;

import java.util.*;

/**
 * This program demonstrates operations on linked lists.
 * @version 1.12 2018-04-10
 * @author Cay Horstmann
 */
public class LinkedListTest
{
   public static void main(String[] args)
   {
      var a = new LinkedList<String>();
      a.add("Amy");
      a.add("Carl");
      a.add("Erica");

//      var aIter = a.listIterator();
//      aIter.next();
//      aIter.add("ASDASDASD");
//      System.out.println(a);
//      LinkedListTest.print();



      var b = new LinkedList<String>();
      b.add("Bob");
      b.add("Doug");
      b.add("Frances");
      b.add("Gloria");

      // merge the words from b into a

      ListIterator<String> aIter = a.listIterator();
      Iterator<String> bIter = b.iterator();

      while (bIter.hasNext())
      {
         if (aIter.hasNext()) aIter.next();
         aIter.add(bIter.next());
      }

      System.out.println(a);

      // remove every second word from b

      bIter = b.iterator();
      while (bIter.hasNext())
      {
         bIter.next(); // skip one element
         if (bIter.hasNext())
         {
            bIter.next(); // skip next element
            bIter.remove(); // remove that element
         }
      }

      System.out.println(b);

      // bulk operation: remove all words in b from a

      a.removeAll(b);

      System.out.println(a);
   }

   public static void print () {
      System.out.println("++++++++");
      var a = new ArrayList<String>();
      a.add("Amy");
      a.add("Carl");
      a.add("Erica");

      var aIter = a.listIterator();
      aIter.next();
      aIter.add("ASDASDASD");
      aIter.add("asdasd");
      System.out.println(a);
   }
}
