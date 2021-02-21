package server;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Property is a class which allows to get the information in the properties file
 *
 * @author Romain DUBUC
 * @version 1.0
 */
public class Property {
    /**
     * The path of the properties file
     */
    private static final String PROPERTIES_FILE_PATH = "properties.json";

    /**
     * Constructor to not instantiate this class
     */
    private Property() {}

    /**
     * Get all data from properties file converted to a JSON Object
     * @return A JSON Object with the file data
     * @throws IOException IOException for the input stream
     */
    private static JSONObject getData() throws IOException{
        InputStream input_stream = new FileInputStream(PROPERTIES_FILE_PATH);

        // Convert the data file to a JSON Tokener
        JSONTokener tokener = new JSONTokener(input_stream);
        JSONObject object = new JSONObject(tokener);

        input_stream.close();

        return object;
    }

    /**
     * Get the value of a given property
     * @param property The name of a property
     * @return The value of the property
     */
    public static int getProperty(String property) {
        try {
            return getData().getInt(property);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Get all subjects of the properties file
     * @param optional If we get the main or the optional subjects
     * @return List of subject names
     *
     * @see Subject
     */
    public static List<Subject> getSubjects(boolean optional) {
        List<Subject> subjects = new ArrayList<>();

        try {
            JSONArray subjects_json = getData().getJSONArray(optional ? "optional_subjects" : "main_subjects");

            // We create Subject class for each subject
            for (int cpt = 0; cpt < subjects_json.length(); cpt++) {
                String name = subjects_json.getJSONObject(cpt).get("name").toString();
                int number_marks = (int) subjects_json.getJSONObject(cpt).get("number_marks");
                int begins_to = (int) subjects_json.getJSONObject(cpt).get("begins_to");
                subjects.add(new Subject(name, number_marks, begins_to, optional));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return subjects;
    }

    /**
     * Get property of a given subject
     * @param name_subject The name of a subject
     * @param property The name of a subject property
     * @return The property of a subject
     */
    public static String getSubjectInfo(String name_subject, String property) {
        try {
            JSONArray main_subjects_json = getData().getJSONArray("main_subjects");

            for (int cpt = 0; cpt < main_subjects_json.length(); cpt++) {
                if (main_subjects_json.getJSONObject(cpt).get("name").toString().equals(name_subject)) {
                    return main_subjects_json.getJSONObject(cpt).get(property).toString();
                }
            }

            JSONArray optional_subjects_json = getData().getJSONArray("optional_subjects");

            for (int cpt = 0; cpt < optional_subjects_json.length(); cpt++) {
                if (optional_subjects_json.getJSONObject(cpt).get("name").toString().equals(name_subject)) {
                    return optional_subjects_json.getJSONObject(cpt).get(property).toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
