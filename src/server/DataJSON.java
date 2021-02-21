package server;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static server.Property.getSubjects;

/**
 * DataJSON is a class which allows to use the JSON file
 *
 * @author Romain DUBUC
 * @version 1.0
 */
public class DataJSON {
    /**
     * All data of the JSON
     */
    private JSONObject data;

    /**
     * Instance that get all data in a JSON file
     */
    public DataJSON() {
        String filePath = "./data.json";
        // Read the file and write all data to "data" variable
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            JSONTokener tokener = new JSONTokener(reader);
            this.data = new JSONObject(tokener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the name of all classes by the class level given
     * @param level The level of classes
     * @return List of class names
     */
    public JSONArray getClassesNameByLevel(int level) {
        JSONArray classes = new JSONArray();

        for (String name_class : this.data.keySet()) {
            // If the class name has the level given then we add this class
            if (name_class.contains(String.valueOf(level))) {
                classes.put(name_class);
            }
        }

        return classes;
    }

    /**
     * Get all marks for a class name given
     * @param name_class The name of a class
     * @return A JSON Object with as keys (The name of a class) / values (All marks of this class)
     */
    public JSONObject getMarksForClass(String name_class) {
        JSONObject a_class = new JSONObject();
        a_class.put(name_class, this.data.getJSONArray(name_class));
        return a_class;
    }

    /**
     * Get names of all students for a given class name
     * @param name_class The name of a class
     * @return A JSON Array with all students name
     */
    public JSONArray getStudentsForClass(String name_class) {
        JSONArray students = new JSONArray();

        this.data.getJSONArray(name_class).forEach(a_student -> {
            JSONObject object_student = new JSONObject(a_student.toString());
            students.put(object_student.get("nom").toString() + " " + object_student.get("prenom").toString());
        });

        return students;
    }

    /**
     * Get all marks for a student given (its last name and first name)
     * @param last_name The last name of a student
     * @param first_name The first name of a student
     * @return A student with all his information
     */
    public JSONObject getMarksForStudent(String last_name, String first_name) {
        // We go through all the classes to find the given student
        for (String name_class : this.data.keySet()) {
            for (Object student : this.data.getJSONArray(name_class)) {
                // We convert a student object to a JSON Object
                JSONObject student_object = new JSONObject(student.toString());
                if (student_object.get("prenom").equals(first_name) && student_object.get("nom").equals(last_name)) {
                    return student_object;
                }
            }
        }
        return null;
    }

    /**
     * Get all marks of classes for the given class level
     * @param level The class level
     * @return A JSON Object with as keys (the name of a class) / values (all students for this class)
     */
    public JSONObject getMarksForLevel(int level) {
        JSONObject classes = new JSONObject();

        for (String name_class : this.data.keySet()) {
            // If the name of a class contains the level given, we add this class
            if (name_class.contains(String.valueOf(level))) {
                classes.put(name_class, this.data.get(name_class));
            }
        }

        return classes;
    }

    /**
     * Join all the marks of a subject for each subject for a class where the name of this class given
     * @param name_class The name of a class
     * @return A JSON Object with as keys (a subject) / values (all marks of this subject)
     */
    public JSONObject unionMarksBySubjectForClass(String name_class) {
        Map<String, ArrayList<Double>> marks = new HashMap<>();

        // We go through all student for the class given
        this.data.getJSONArray(name_class).forEach(student -> {
            // We make all subject objects of a student readable
            JSONObject student_subjects = new JSONObject(new JSONObject(student.toString()).get("matieres").toString());

            // We go through all subjects of a student
            student_subjects.keySet().forEach(name_subject -> {
                // We make all marks objects of a subject readable
                JSONArray student_marks = new JSONArray(student_subjects.getJSONObject(name_subject).get("notes").toString());
                student_marks.forEach(a_mark -> {
                    // If the key (subject name) exists in the map, we add the mark in the Map with as key the subject name
                    // Else we create the subject in the Map then we add the mark
                    if (marks.containsKey(name_subject)) {
                        marks.get(name_subject).add(Double.valueOf(a_mark.toString()));
                    }else {
                        ArrayList<Double> array_marks = new ArrayList<>();
                        array_marks.add(Double.valueOf(a_mark.toString()));
                        marks.put(name_subject, array_marks);
                    }
                });
            });
        });

        return new JSONObject(marks);
    }

    /**
     * Join all the marks of an exam given of a subject given for a class where the name of this class given
     * @param name_class The name of a class
     * @param id_mark The id of an exam
     * @param name_subject The name of a subject
     * @return A JSON Object with as keys (a subject) / values (all marks of an exam of this subject)
     */
    public JSONObject unionMarksBySubjectForClassSpecialExam(String name_class, int id_mark, String name_subject) {
        Map<String, ArrayList<Double>> marks = new HashMap<>();

        // We go through all student for the class given
        this.data.getJSONArray(name_class).forEach(student -> {
            // We make all subject objects of a student readable
            JSONObject student_subjects = new JSONObject(new JSONObject(student.toString()).get("matieres").toString());

            // We check if the student has the given subject
            if (student_subjects.has(name_subject)) {
                // We make all marks objects of this subject readable
                JSONArray student_marks = new JSONArray(student_subjects.getJSONObject(name_subject).get("notes").toString());
                double student_mark;

                // Check if the student has the given exam
                if (student_marks.length() >= (id_mark+1)) {
                    student_mark = Double.parseDouble(student_marks.get(id_mark).toString());
                }else return;

                // If the key (subject name) exists in the map, we add the mark in the Map with as key the subject name
                // Else we create the subject in the Map then we add the mark
                if (marks.containsKey(name_subject)) {
                    marks.get(name_subject).add(student_mark);
                }else {
                    ArrayList<Double> array_marks = new ArrayList<>();
                    array_marks.add(student_mark);
                    marks.put(name_subject, array_marks);
                }
            }
        });

        return new JSONObject(marks);
    }

    /**
     * Calcul the median of a double sample
     * @param list The sample
     * @return (Double) The median
     */
    public static double calculateMedian(List<Double> list) {
        list.sort(Comparator.comparingDouble(value -> value));
        int middle = list.size()/2;
        if (list.size()%2 == 1) {
            return list.get(middle);
        } else {
            return (list.get(middle-1) + list.get(middle)) / 2.0;
        }
    }

    /**
     * Get all information of subjects for all the classes whose level is given
     * @param level The level of classes
     * @return A JSON Object with as keys (the name of a class) / values (all information for all subjects of this class)
     */
    public JSONObject getInfoSubjectsForLevel(int level) {
        return getInfoSubjectsForClasses(getMarksForLevel(level));
    }

    /**
     * Get all information of subjects for a given class
     * @param name_class The name of a class
     * @return A JSON Object with as keys (the name of a class) / values (all information for all subjects of this class)
     */
    public JSONObject getInfoSubjectsForClass(String name_class) {
        return getInfoSubjectsForClasses(getMarksForClass(name_class));
    }

    /**
     * Get all information of subjects for given classes
     * @param classes Map with all information of classes
     * @return A JSON Object with as keys (the name of a class) / values (all information for all subjects of this class)
     */
    public JSONObject getInfoSubjectsForClasses(JSONObject classes) {
        JSONObject info_subjects = new JSONObject();

        // We go through all given classes
        classes.keySet().forEach(name_class -> {
            // We join all the marks of subjects
            JSONObject marks_by_subjects = unionMarksBySubjectForClass(name_class);
            JSONObject a_class = new JSONObject();

            // We go through all subjects
            marks_by_subjects.keySet().forEach(name_subject -> {
                // We get all marks of a subject in a double list
                List<Double> marks = marks_by_subjects.getJSONArray(name_subject)
                        .toList()
                        .stream()
                        .map(element-> (double) element)
                        .collect(Collectors.toList());
                JSONObject a_subject = new JSONObject();
                // We calculate all information with this list
                a_subject.put("maximum", marks.stream().mapToDouble(value -> value).max().orElseThrow());
                a_subject.put("minimum", marks.stream().mapToDouble(value -> value).min().orElseThrow());
                a_subject.put("moyenne", marks.stream().mapToDouble(value -> value).average().orElseThrow());
                a_subject.put("médiane", calculateMedian(marks));
                a_class.put(name_subject, a_subject);
            });

            info_subjects.put(name_class, a_class);
        });

        return info_subjects;
    }

    /**
     * Update the averages of a student
     * @param a_student the student to update
     * @return the student with his new averages
     */
    private JSONObject updateAveragesStudent(JSONObject a_student) {
        List<Double> averages = new ArrayList<>();
        List<String> optional_subjects = getSubjects(true).stream().map(Subject::toString).collect(Collectors.toList());
        double bonus_point = 0.0;

        // We go through all the student's subjects
        for (String name_subject : a_student.getJSONObject("matieres").keySet()) {
            JSONObject object_subject = a_student.getJSONObject("matieres").getJSONObject(name_subject);

            // Get the average of the subject
            double average = object_subject.getJSONArray("notes").toList().stream().mapToDouble(value -> {
                if (value instanceof Integer) {
                    return (double) (int) value;
                } else if (value instanceof BigDecimal) {
                    return ((BigDecimal) value).doubleValue();
                }
                return (double) value;
            }).average().orElseThrow();

            // Update the subject average
            object_subject.put("moyenne", average);

            // Check if it's a main or optional subject to calculate the overall average
            if (!optional_subjects.contains(name_subject)) {
                averages.add(average);
            } else {
                // If it is a optional subject, we add 0.1 point per point above 10
                if (average > 10) {
                    bonus_point += ((int) average - 10) * Math.pow(10, -1);
                }
            }
        }

        // We calculate the overall average for the main subjects
        double overall_average = averages.stream().mapToDouble(value -> value).average().orElseThrow();
        // Then we add the bonus point (optional subject)
        overall_average += bonus_point;

        a_student.put("moyenne générale", overall_average);

        return a_student;
    }

    /**
     * Add student's mark for a given subject
     * @param name_class The name of the student's class
     * @param name_subject The name of a subject
     * @param last_name The last name of student
     * @param first_name The first name of student
     * @param mark The mark to add
     */
    public void addMarkToStudent(String name_class, String name_subject, String last_name, String first_name, String mark) {
        double double_mark = Double.parseDouble(mark);
        int cpt = 0;

        for (Object a_student : this.data.getJSONArray(name_class)) {
            // Make the student object readable
            JSONObject object_student = new JSONObject(a_student.toString());

            if (object_student.get("nom").equals(last_name) && object_student.get("prenom").equals(first_name)) {
                JSONObject object_subjects = object_student.getJSONObject("matieres");

                // If the student has the subject we add the new mark
                // else, we create the subject with the mark and add it
                if (object_subjects.has(name_subject)) {
                    object_subjects.getJSONObject(name_subject).getJSONArray("notes").put(double_mark);
                }else {
                    JSONObject new_subject = new JSONObject();
                    List<Double> new_list_marks = new ArrayList<>();
                    new_list_marks.add(double_mark);
                    new_subject.put("notes", new_list_marks);
                    new_subject.put("moyenne", new_list_marks);
                    object_subjects.put(name_subject, new_subject);
                }
                // Update the student information
                this.data.getJSONArray(name_class).put(cpt, updateAveragesStudent(object_student));

                // We write this JSON object to a file (data.json) and we close it
                try (FileWriter file = new FileWriter("./data.json")){
                    file.write(String.valueOf(this.data));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            cpt++;
        }
    }

    /**
     * Import new data to the json file
     * @param file_path The path of the import file
     */
    public void importData(String file_path) {
        String name_class;
        String name_subject;

        // We read the import file
        try (BufferedReader file_import = new BufferedReader(new FileReader(file_path))) {
            name_class = file_import.readLine();
            name_subject = file_import.readLine();

            String line;
            // We go through all student to add their mark
            while ((line = file_import.readLine()) != null) {
                String[] info_student = line.split(" ");
                addMarkToStudent(name_class, name_subject, info_student[0], info_student[1], info_student[2]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
