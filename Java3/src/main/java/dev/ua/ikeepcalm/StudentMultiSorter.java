package dev.ua.ikeepcalm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@ToString
class Student {
    private String name;
    private double averageGrade;

    public Student(String name, double averageGrade) {
        this.name = name;
        this.averageGrade = averageGrade;
    }

}

public class StudentMultiSorter {

    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        students.add(new Student("Олег Запара", 10.5));
        students.add(new Student("Свячений Євген", 9.3));
        students.add(new Student("Марія Глинка", 8.9));
        students.add(new Student("Юлія Зелюк", 9.3));
        students.add(new Student("Карпець Даніїл", 8.5));

        students.forEach(System.out::println);

        students.sort(Comparator.comparing(Student::getAverageGrade)
                .reversed()
                .thenComparing(Student::getName)
        );

        students.forEach(System.out::println);
    }
}