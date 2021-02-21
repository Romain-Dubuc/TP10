package server;

/**
 * Subject is a class that represents a subject in a high school
 *
 * @author Romain DUBUC
 * @version 1.0
 */
public class Subject {
    /**
     * Subject's name
     */
    private final String name;

    /**
     * Number of marks for this subject
     */
    private final int number_marks;

    /**
     * When does this subject begin (the level of the class)
     */
    private final int begins_to;

    /**
     * Main or optional subject
     */
    private final boolean optional;

    /**
     * Instance with a name, number of marks, when the subject begins and whether it is optional or not
     * @param name The name of subject
     * @param number_marks The number of marks for the subject
     * @param begins_to When the subject begins
     * @param optional The subject is optional or not
     */
    public Subject(String name, int number_marks, int begins_to, boolean optional) {
        this.name = name;
        this.number_marks = number_marks;
        this.begins_to = begins_to;
        this.optional = optional;
    }

    /**
     * Getter for the number of marks
     * @return (int) The number of marks for the subject
     */
    public int getNumberMarks() {
        return number_marks;
    }

    /**
     * Getter to know when the subject begins
     * @return (int) the level where the subject begins
     */
    public int getBeginsTo() {
        return begins_to;
    }

    /**
     * Getter for the name
     * @return (String) The name of the subject
     */
    public String getName() {
        return name;
    }

    /**
     * Getter to know if the subject is optional or not
     * @return (boolean) if it is a main or optional subject
     */
    public boolean isOptional() {
        return optional;
    }

    /**
     * Make the class displayable
     * @return the subject name
     */
    public String toString() {
        return this.name;
    }
}
