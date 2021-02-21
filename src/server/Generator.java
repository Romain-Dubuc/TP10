package server;

import com.github.javafaker.Faker;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Generator is a class which allows to generate classes with students and the marks
 *
 * @author Romain DUBUC
 * @version 1.0
 */
public class Generator {

    /**
     * Instance of Random class
     */
    private static final Random random = new Random();

    /**
     * Generate all classes for a level given.
     * @param level classes' level
     * @return List of classes
     *
     * @see Class
     * @see Faker
     * @see Property
     * @see Student
     */
    public static List<Class> generateClass(int level) {
        List<Class> classes = new ArrayList<>();
        int ascii_number = 65;

        Faker faker = new Faker(new Locale("fr"));

        // Get the number of classes to create with the Property class
        for (int cpt = 0; cpt < Property.getProperty("number_classes"); cpt++) {
            // For each class, we create an Instance of Class
            Class a_class = new Class(level, Character.toString((char) ascii_number++));
            // Get the number of students to add to the class with the Property class
            // Then we create and add these students to the class
            for (int cpt_2 = 0; cpt_2 < Property.getProperty("students_per_class"); cpt_2++) {
                // We use the Faker library to create students
                a_class.setStudent(new Student(faker.name().lastName().replaceAll("\\s+",""), faker.name().firstName().replaceAll("\\s+","")));
            }
            classes.add(a_class);
        }

        return classes;
    }

    /**
     * Generate marks for the given subject to the given student
     * @param subject The subject to use to add marks
     * @param a_class The student's class
     * @param student The student who get these marks
     */
    public static void generateMarksSubject(Subject subject, Class a_class, Student student) {
        // Check if subject is studied for this class with the class level
        if (a_class.getLevel() <= subject.getBeginsTo()) {
            List<Double> marks = new ArrayList<>();
            // Generate and add marks to the student
            for (int cpt = 0; cpt < subject.getNumberMarks(); cpt++) {
                marks.add(generateMark());
            }
            student.addMark(subject, marks);
        }
    }

    /**
     * Generate marks for all students in classes given
     * @param array_classes list of classes containing students
     *
     * @see Class
     * @see Faker
     * @see Subject
     * @see Property
     */
    @SafeVarargs
    public static void generateMarks(List<Class> ... array_classes) {
        // Get main and optional subjects with the Property class
        List<Subject> main_subjects = Property.getSubjects(false);
        List<Subject> optional_subjects = Property.getSubjects(true);

        // For all students in the classes
        Arrays.stream(array_classes).forEach(array_class -> array_class.forEach(a_class -> a_class.getStudents().forEach(student -> {
            // Generate marks for all main subjects
            main_subjects.forEach(subject -> generateMarksSubject(subject, a_class, student));

            // Generate a number between 0 and 2 that represents the number of optional subjects of this student
            int number_optional_subjects = random.nextInt(3);

            // Copy the list of optional subjects
            List<Subject> optional_subjects_student = new ArrayList<>(optional_subjects);

            for (int cpt = 0; cpt < number_optional_subjects; cpt++) {
                // Choose an optional subject from the list
                Subject subject = optional_subjects_student.remove(random.nextInt(optional_subjects_student.size()));

                // Student cannot study Greek and Latin at the same time
                // Greek is removed if Latin is chosen and vice versa
                if (subject.getName().equals("Latin")) {
                    optional_subjects_student.removeIf(a_subject -> a_subject.getName().equals("Grec"));
                } else if (subject.getName().equals("Grec")) {
                    optional_subjects_student.removeIf(a_subject -> a_subject.getName().equals("Latin"));
                }

                // Generate marks for the optional subject
                generateMarksSubject(subject, a_class, student);
            }

            // Calcul of all averages and overall average for this student
            student.calculateAverage();
        })));
    }

    /**
     * Generate a mark relative to a gaussian, average : 13 | standard deviation : 3
     * @return (double) the mark
     */
    public static double generateMark() {
        long factor = (long) Math.pow(10, 2);
        double value = (random.nextGaussian() * 3 + 13) * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * Generate a JSON for all given classes
     * @param array_classes the list of classes
     */
    @SafeVarargs
    public static void generateJSON(List<Class> ... array_classes) {
        // Create a JSON object
        JSONObject classes = new JSONObject();

        // For each class, we transform this class into an object
        for (List<Class> array_class : array_classes) {
            array_class.forEach(a_class -> classes.put(a_class.toString(), a_class.toObjectStudents()));
        }

        // We write this JSON object to a file (data.json) and we close it
        try (FileWriter file = new FileWriter("./data.json")){
            file.write(String.valueOf(classes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main function that generates all the classes and students with marks, finally we write them to a file
     * @param args the arguments of the main function
     */
    public static void main(String[] args) {
        List<Class> classes_6eme = generateClass(6);
        List<Class> classes_5eme = generateClass(5);
        List<Class> classes_4eme = generateClass(4);
        List<Class> classes_3eme = generateClass(3);

        generateMarks(classes_6eme, classes_5eme, classes_4eme, classes_3eme);
        generateJSON(classes_6eme, classes_5eme, classes_4eme, classes_3eme);
    }
}
