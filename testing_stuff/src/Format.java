package src;

import java.util.Calendar;
import java.util.Date;

import static java.lang.System.out;
import static java.lang.Math.*;

public class Format {
    public static void main(String[] args) {
//        out.println("tan" + tan(60));
        Format.formatNumber();
//        Format.formatDate();
    }

    static void formatNumber() {
        System.out.println(String.format("%,d %d", 1230000000, 1230000000)); //1,230,000,000 1230000000
        System.out.println(String.format("%.2f", 1230000000.098098)); //1230000000.10
        System.out.println(String.format("%.3f", 1230000000.098098)); //1230000000.098
        System.out.println(String.format("%,.3f", 1230000000.098098)); //1,230,000,000.098
        System.out.println(String.format("%,d", 1230000000)); //1,230,000,000
        System.out.println(String.format("%2$,6.1f", 24.000, 30.000, 42.000)); //30.0
    }

    static void formatDate() {
        Date date = new Date();

        System.out.println(String.format("%tc", date)); //Tue May 19 23:19:28 CEST 2020
        System.out.println(String.format("%tr", date)); //11:19:28 PM
        System.out.println(String.format("%tA %tB %td", date, date, date)); //Tuesday May 19
        System.out.println(String.format("%tA %<tB %<td", date)); //Tuesday May 19

        Calendar c = Calendar.getInstance();
        c.set(2004, Calendar.JANUARY,7,15,40); // set date
        System.out.println("hours " + c.get(Calendar.HOUR_OF_DAY)); // new hour 15
        long newDateInMilliseconds = c.getTimeInMillis() + 1000 * 60 * 60;
        c.setTimeInMillis(newDateInMilliseconds);
        System.out.println("new hour " + c.get(Calendar.HOUR_OF_DAY)); // new hour 16
        c.add(Calendar.DATE, 35);
        System.out.println("add 35 days " + c.getTime()); // add 35 days Wed Feb 11 16:40:30 CET 2004
        c.roll(Calendar.DATE, 35);
        System.out.println("roll 35 days " + c.getTime()); // roll 35 days Tue Feb 17 16:40:30 CET 2004
        c.set(Calendar.DATE, 1);
        System.out.println("set to 1 " + c.getTime()); // set to 1 Sun Feb 01 16:40:30 CET 2004
    }
}
