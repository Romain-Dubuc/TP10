package server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Student is a class that represents a student in a high school.
 *
 * @author Romain DUBUC
 * @version 1.0
 */
public class Student {
    /**
     * Student's last name
     */
    private final String last_name;

    /**
     * Student's first name
     */
    private final String first_name;

    /**
     * Student's overall average
     */
    private double overall_average;

    /**
     * Student marks of all subjects
     * Map with as keys (the name of a subject) / values (marks of this subject)
     *
     * @see Subject
     */
    private final Map<Subject, List<Double>> marks;

    /**
     * Student averages of all subjects
     * Map with as keys (the name of a subject) / values (average of this subject)
     *
     * @see Subject
     */
    private final Map<Subject, Double> averages;

    /**
     * Instance with a last name and a first name
     * @param last_name The last name of student
     * @param first_name The first name of student
     */
    public Student(String last_name, String first_name) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.overall_average = 0.0;
        this.marks = new HashMap<>();
        this.averages = new HashMap<>();
    }

    /**
     * Getter for the last name
     * @return (String) The last name of student
     */
    public String getLastName() {
        return last_name;
    }

    /**
     * Getter for the first name
     * @return (String) The first name of student
     */
    public String getFirstName() {
        return first_name;
    }

    /**
     * Getter for the overall average
     * @return (String) The overall average of student
     */
    public double getOverallAverage() {
        return overall_average;
    }

    /**
     * Get the average of a student's subject
     * @param subject Instance of a subject
     * @return The average of a given subject
     *
     * @see Subject
     */
    public double getAverage(Subject subject) {
        return averages.get(subject);
    }

    /**
     * Getter for the marks
     * @return (Map) The marks of student
     */
    public Map<Subject, List<Double>> getMarks() {
        return marks;
    }

    /**
     * Add a mark for a given subject
     * @param subject Instance of a subject
     * @param marks The mark to add
     */
    public void addMark(Subject subject, List<Double> marks) {
        this.marks.put(subject, marks);
    }

    /**
     * Calculate the overall average and all averages of student's subjects
     */
    public void calculateAverage() {
        double bonus_point = 0.0;
        int number_subjects = 0;

        // We go through all the student's marks
        for (Map.Entry<Subject, List<Double>> item : getMarks().entrySet()) {
            double average = item.getValue().stream().mapToDouble(mark -> mark).sum() / item.getValue().size();
            this.averages.put(item.getKey(), average);
            if (!item.getKey().isOptional()) {
                this.overall_average += average;
                number_subjects++;
            }else {
                // If it is a optional subject, we add 0.1 point per point above 10
                if (average > 10) {
                    bonus_point += ((int) average - 10) * Math.pow(10, -1);
                }
            }
        }

        // We calculate the overall average for the main subjects
        if (number_subjects != 0) {
            this.overall_average /= number_subjects;
        }
        // Then we add the bonus point (optional subject)
        this.overall_average += bonus_point;
    }

    /**
     * Convert Student class to object
     * @return Map with all the student's information
     */
    public Map<String, Object> toObject() {
        Map<String, Object> object = new HashMap<>();
        object.put("nom", getLastName());
        object.put("prenom", getFirstName());
        object.put("moyenne générale", getOverallAverage());

        Map<String, Object> subjects = new HashMap<>();
        // Create a Map with all information of student's subjects
        getMarks().forEach((subject, student_marks) -> {
            Map<String, Object> subject_info = new HashMap<>();
            subject_info.put("moyenne", getAverage(subject));
            subject_info.put("notes", student_marks);
            subjects.put(subject.toString(), subject_info);
        });

        object.put("matieres", subjects);

        return object;
    }
}
