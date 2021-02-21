package server;

import java.util.ArrayList;
import java.util.List;

/**
 * Class is a class that represents a class in a high school.
 *
 * @author Romain DUBUC
 * @version 1.0
 */
public class Class {
    /**
     * The level of the class.
     * Example : 6eme, 5eme, ...
     */
    private final int level;
    /**
     * The letter of the class.
     * Example : A, B, C, ...
     */
    private final String letter;
    /**
     * The list of students in the class.
     *
     * @see Student A student
     */
    private final List<Student> students;

    /**
     * Instance with a level and a letter to make a class.
     * @param level The level of a class
     * @param letter The letter of a class
     */
    public Class(int level, String letter) {
        this.level = level;
        this.letter = letter;
        this.students = new ArrayList<>();
    }

    /**
     * Getter for the level.
     * @return (Integer) The level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Getter for the letter.
     * @return (String) The letter
     */
    public String getLetter() {
        return letter;
    }

    /**
     * Add a student to the class.
     * @param student A Student
     *
     * @see Student
     */
    public void setStudent(Student student) {
        this.students.add(student);
    }

    /**
     * Getter for the list of students
     * @return List of students
     *
     * @see Student
     */
    public List<Student> getStudents() {
        return this.students;
    }

    /**
     * Return the level and the letter of the class.
     * @return (String) Level + Letter
     */
    public String toString() {
        return getLevel() + getLetter();
    }

    /**
     * Change the class to an object with all the properties.
     * @return List with properties
     */
    public List<Object> toObjectStudents() {
        List<Object> students_list = new ArrayList<>();

        // For every students of the class, we add the object of this one
        getStudents().forEach(student -> students_list.add(student.toObject()));

        return students_list;
    }
}
