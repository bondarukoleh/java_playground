import java.time.*;
import java.util.Objects;

/**
 * This program tests the Employee class.
 *
 * @author Cay Horstmann
 * @version 1.13 2018-04-10
 */
public class EmployeeTest {
    public static void main(String[] args) {
        // fill the staff array with three Employee objects
        Employee[] staff = new Employee[3];

        staff[0] = new Employee("Carl Cracker", 75000, 1987, 12, 15);
        staff[1] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
        staff[2] = new Employee("Tony Tester", 40000, 1990, 3, 15);

        // raise everyone's salary by 5%
        for (Employee e : staff)
            e.raiseSalary(5);

        // print out information about all Employee objects
        for (Employee e : staff)
            System.out.println("name=" + e.getName() + ",salary=" + e.getSalary() + ",hireDay=" + e.getHireDay());
    }
}

class Employee {
    private final String name;
    private double salary;
    private LocalDate hireDay;
    private static int nextId = 1;
    private int id;

    public Employee(String n, double s, int year, int month, int day) {
        this.name = Objects.requireNonNullElse(n,"unknown");
        this.salary = s;
        this.hireDay = LocalDate.of(year, month, day);
        this.id = nextId;
        nextId++;
    }

    public String getName() {
        return this.name;
    }

    public boolean isNameTheSameAs(Employee other) {
        return this.name.equals(other.name);
    }

    public double getSalary() {
        return this.salary;
    }

    public LocalDate getHireDay() {
        return this.hireDay;
    }

    public void raiseSalary(double byPercent) {
        double raise = this.salary * byPercent / 100;
        this.salary += raise;
    }
}
