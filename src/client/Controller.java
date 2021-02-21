package client;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static client.Client.getDataAPI;

/**
 * Controller is a class to make the client window more interactive
 *
 * @author Romain DUBUC
 * @version 1.0
 */
public class Controller{
    /**
     * The bar chart in the fifth window tab
     */
    @FXML
    private BarChart<String, Number> bar_chart;
    /**
     * The list of subjects in the fifth window tab
     */
    @FXML
    private ListView<String> subjects_list;
    /**
     * The list of subjects in the sixth window tab
     */
    @FXML
    private ListView<String> subjects_list_2;
    /**
     * The list of subjects in the seventh window tab
     */
    @FXML
    private ListView<String> subjects_list_3;
    /**
     * The list of subjects in the import tab
     */
    @FXML
    private ListView<String> subjects_list_4;
    /**
     * The list of exams for a subject in the sixth window tab
     */
    @FXML
    private ListView<String> exams_list;
    /**
     * The list of classes levels in the eighth window tab
     */
    @FXML
    private ListView<String> level_list;
    /**
     * The list of classes levels in the fourth window tab
     */
    @FXML
    private ListView<String> level_list_2;
    /**
     * The list of classes levels in the first window tab
     */
    @FXML
    private ListView<String> level_list_3;
    /**
     * The list of classes levels in the third window tab
     */
    @FXML
    private ListView<String> level_list_4;
    /**
     * The list of classes in the fourth window tab
     */
    @FXML
    private ListView<String> classes_list;
    /**
     * The list of classes in the first window tab
     */
    @FXML
    private ListView<String> classes_list_2;
    /**
     * The select menu of levels in the fifth window tab
     */
    @FXML
    private ChoiceBox<String> choice_level;
    /**
     * The select menu of levels in the sixth window tab
     */
    @FXML
    private ChoiceBox<String> choice_level_2;
    /**
     * The select menu of levels in the seventh window tab
     */
    @FXML
    private ChoiceBox<String> choice_level_3;
    /**
     * The line chart in the sixth window tab
     */
    @FXML
    private LineChart<Double, Double> line_chart;
    /**
     * The line chart in the seventh window tab
     */
    @FXML
    private LineChart<Double, Double> line_chart_2;
    /**
     * The line chart in the eighth window tab
     */
    @FXML
    private LineChart<Double, Double> line_chart_3;
    /**
     * The table in the fourth window tab
     */
    @FXML
    private TableView<InfoSubjects> table;
    /**
     * The subjects column of table in the fourth window tab
     */
    @FXML
    private TableColumn<InfoSubjects, String> subjects_tab;
    /**
     * The maximum marks column of the table in the fourth window tab
     */
    @FXML
    private TableColumn<InfoSubjects, String> max_tab;
    /**
     * The minimum marks column of the table in the fourth window tab
     */
    @FXML
    private TableColumn<InfoSubjects, String> min_tab;
    /**
     * The averages column of the table in the fourth window tab
     */
    @FXML
    private TableColumn<InfoSubjects, String> averages_tab;
    /**
     * The medians column of the table in the fourth window tab
     */
    @FXML
    private TableColumn<InfoSubjects, String> medians_tab;
    /**
     * The tabs of the window
     */
    @FXML
    private TabPane tab_pane;
    /**
     * The mark input in the import tab
     */
    @FXML
    private TextField mark_input;
    /**
     * The select menu of classes in the import tab
     */
    @FXML
    private ChoiceBox<String> choice_classes;
    /**
     * The select menu of classes in the import tab
     */
    @FXML
    private ChoiceBox<String> choice_classes_2;
    /**
     * The list of students in the import tab
     */
    @FXML
    private ListView<String> students_list;
    /**
     * The list of students in the import tab
     */
    @FXML
    private ListView<String> students_list_2;
    /**
     * The table in the import tab
     */
    @FXML
    private TableView<StudentTable> table_import;
    /**
     * The class table in the first tab
     */
    @FXML
    private TableView<ObservableList<String>> table_class;
    /**
     * The class table in the third tab
     */
    @FXML
    private TableView<ObservableList<String>> table_level;
    /**
     * The student table in the second tab
     */
    @FXML
    private TableView<ObservableList<String>> table_student;
    /**
     * The last names column of the table in the import tab
     */
    @FXML
    private TableColumn<StudentTable, String> last_names_tab;
    /**
     * The first names column of the table in the import tab
     */
    @FXML
    private TableColumn<StudentTable, String> first_names_tab;
    /**
     * The marks column of the table in the import tab
     */
    @FXML
    private TableColumn<StudentTable, String> marks_tab;
    /**
     * The names column of the table in the first tab
     */
    @FXML
    private TableColumn<ObservableList<String>, String> students_tab;
    /**
     * The names column of the table in the third tab
     */
    @FXML
    private TableColumn<ObservableList<String>, String> students_tab_2;
    /**
     * The classes column of the table in the third tab
     */
    @FXML
    private TableColumn<ObservableList<String>, String> classes_tab;
    /**
     * The subjects column of the table in the second tab
     */
    @FXML
    private TableColumn<ObservableList<String>, String> subjects_tab_2;
    /**
     * The marks column of the table in the second tab
     */
    @FXML
    private TableColumn<ObservableList<String>, String> marks_tab_2;


    /**
     * The logger to have window information
     */
    private static final Logger LOGGER = Logger.getLogger("log");

    /**
     * Initialize lists, choices and tables
     */
    @FXML
    private void initialize() {
        String[] level_classes = {"6eme", "5eme", "4eme", "3eme"};
        String[] classes = {"A", "B", "C", "D", "E", "F"};

        initListViewSubjects(subjects_list);
        initListViewSubjects(subjects_list_2);
        initListViewSubjects(subjects_list_3);
        initListViewSubjects(subjects_list_4);

        initTableSubjects(table_class, false);
        initTableSubjects(table_level, true);

        // Add levels information for choices and lists
        Arrays.stream(level_classes).forEach(level_class -> {
            choice_level_2.getItems().add(level_class);
            choice_level_3.getItems().add(level_class);
            choice_level.getItems().add(level_class);
            level_list.getItems().add(level_class);
            level_list_2.getItems().add(level_class);
            level_list_3.getItems().add(level_class);
            level_list_4.getItems().add(level_class);
        });

        // Add classes information for choices and lists
        Arrays.stream(classes).forEach(name_class -> {
            classes_list.getItems().add(name_class);
            classes_list_2.getItems().add(name_class);
            for (int level = 6; level >= 3; level--) {
                choice_classes.getItems().add(level + name_class);
                choice_classes_2.getItems().add(level + name_class);
            }
        });

        // Initialize table tabs
        subjects_tab.setCellValueFactory(new PropertyValueFactory<>("SubjectsTab"));
        max_tab.setCellValueFactory(new PropertyValueFactory<>("MaxTab"));
        min_tab.setCellValueFactory(new PropertyValueFactory<>("MinTab"));
        averages_tab.setCellValueFactory(new PropertyValueFactory<>("AveragesTab"));
        medians_tab.setCellValueFactory(new PropertyValueFactory<>("MediansTab"));

        last_names_tab.setCellValueFactory(new PropertyValueFactory<>("LastNamesTab"));
        first_names_tab.setCellValueFactory(new PropertyValueFactory<>("FirstNamesTab"));
        marks_tab.setCellValueFactory(new PropertyValueFactory<>("MarksTab"));

        // Initialize the column of the table in the first tab
        students_tab.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(0)));

        // Initialize the columns of the table in the third tab
        students_tab_2.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(0)));
        classes_tab.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(1)));

        // Initialize the columns of the table in the second tab
        subjects_tab_2.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(0)));
        marks_tab_2.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(1)));

        // Put a decimal regex for mark input in the import tab
        mark_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("")) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    mark_input.setText(oldValue);
                }else {
                    if (Double.parseDouble(newValue) > 20.0) {
                            mark_input.setText(oldValue);
                    }
                }
            }
        });
    }

    /**
     * Put main and optional subjects column to the given table
     * @param table Add columns to this table
     * @param has_classes_tab If the table has the tab "Classes"
     */
    public void initTableSubjects(TableView<ObservableList<String>> table, boolean has_classes_tab) {
        try {
            List<String> subjects = new ArrayList<>();

            // Get information from the server to get the main subjects
            JSONArray main_subjects = (JSONArray) getDataAPI("getSubjects", false);
            main_subjects.forEach(subject -> subjects.add(subject.toString()));

            // Get information from the server to get the optional subjects
            JSONArray optional_subjects = (JSONArray) getDataAPI("getSubjects", true);
            optional_subjects.forEach(subject -> subjects.add(subject.toString()));

            // Add every subjects to the table with a id
            for (int cpt = 0; cpt < subjects.size(); cpt++) {
                final int finalIdx = has_classes_tab ? cpt+2 : cpt+1;
                TableColumn<ObservableList<String>, String> table_column = new TableColumn<>(subjects.get(cpt));
                table_column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));
                table.getColumns().add(table_column);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Put main and optional subjects to the list from the server information
     * @param list_view The list used to put subjects
     */
    public void initListViewSubjects(ListView<String> list_view) {
        try {
            // Get information from the server to get the main subjects
            JSONArray main_subjects = (JSONArray) getDataAPI("getSubjects", false);
            main_subjects.forEach(subject -> list_view.getItems().add(subject.toString()));

            // Get information from the server to get the optional subjects
            JSONArray optional_subjects = (JSONArray) getDataAPI("getSubjects", true);
            optional_subjects.forEach(subject -> list_view.getItems().add(subject.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Put information to chart or list related to selected tab
     */
    public void clickedSubject() {
        switch (tab_pane.getSelectionModel().getSelectedIndex()) {
            // Make Bar chart with level and subject selected in the fifth tab
            case 4 -> setDataBarChart(6 - choice_level.getSelectionModel().getSelectedIndex(), subjects_list.getSelectionModel().getSelectedItem());
            // Put subject exams with the selected subject in the sixth tab
            case 5 -> setExamsForSubject(subjects_list_2.getSelectionModel().getSelectedItem());
            // Make Line chart with level and subject selected in the seventh tab
            case 6 -> setDataLineChart(6 - choice_level_3.getSelectionModel().getSelectedIndex(), subjects_list_3.getSelectionModel().getSelectedItem());
            default -> LOGGER.log(Level.WARNING, "Wrong tab");
        }
    }

    /**
     * Make a gaussian for the given line chart with the given data
     * @param chart The chart to add the gaussian
     * @param list The data to use to make the gaussian
     * @param title Title of the gaussian
     */
    private void makeGaussian(LineChart<Double, Double> chart, List<Object> list, String title) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName(title);

        // Cast the Object list to a Double list
        double[] list_double = list.stream().mapToDouble(value -> {
            if (value instanceof Integer) {
                return (double) (int) value;
            }else if (value instanceof BigDecimal) {
                return ((BigDecimal) value).doubleValue();
            }
            return (double) value;
        }).toArray();

        // Calculate the average and the standard deviation of the Double list
        double average = Arrays.stream(list_double).average().orElseThrow();
        double standard_deviation = new StandardDeviation().evaluate(list_double);
        // Calculate the normal distribution of the Double list if the standard deviation is not equal to 0
        if (standard_deviation != 0) {
            NormalDistribution normal = new NormalDistribution(average, standard_deviation);

            // 99% of Double list values
            for (double cpt = average-3.29*standard_deviation; cpt < average+3.29*standard_deviation; cpt+=0.2) {
                series.getData().add(new XYChart.Data<>(cpt, normal.density(cpt)));
            }

            // Add the series to the given chart
            chart.getData().add(series);
        }
    }

    /**
     * Set data for the Line chart in the eighth tab
     * This chart shows the distribution of the overall average for the subject of a level classes given
     * @param level The level of classes to show
     */
    private void setDataLineChartOverallAverage(int level) {
        try {
            // Clear the chart
            line_chart_3.getData().clear();

            // Get all information for all the subjects of the classes from a given level from the server
            JSONObject data = (JSONObject) getDataAPI("getInfoSubjectsForLevel", level);

            // We go through all given classes
            for (String name_class : data.keySet()) {
                List<Double> averages = new ArrayList<>();
                JSONObject subjects = data.getJSONObject(name_class);

                // We get average information for all subjects
                for (String name_subject : subjects.keySet()) {
                    JSONObject a_subject = subjects.getJSONObject(name_subject);
                    averages.add(a_subject.getDouble("moyenne"));
                }
                // We create and put a gaussian with all averages of a class
                makeGaussian(line_chart_3, Arrays.asList(averages.toArray()), name_class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set data for the Line chart in the sixth tab
     * This chart show the distribution of marks for a given exam of a level classes given
     * @param level The level of classes
     * @param name_subject The name of subject
     * @param id_mark The id of a subject's exam
     */
    private void setDataLineChartExam(int level, String name_subject, int id_mark) {
        // Check if the user has selected an exam
        if (id_mark == -1) {
            return;
        }
        // Clear the chart
        line_chart.getData().clear();
        try {
            // Get the name of classes by a given level
            JSONArray name_classes = (JSONArray) getDataAPI("getClassesNameByLevel", level);

            // We go through all classes
            name_classes.forEach(name_class -> {
                try {
                    // Get all marks by subject of the class for a given exam
                    JSONObject subjects_marks = (JSONObject) getDataAPI("unionMarksBySubjectForClassSpecialExam", name_class, id_mark, name_subject);
                    // If the class has the subject, we create and put a gaussian on the chart
                    if (subjects_marks.has(name_subject)) {
                        makeGaussian(line_chart, subjects_marks.getJSONArray(name_subject).toList(), name_class.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set data for the Line chart in the seventh tab
     * This chart show the distribution of marks for a given subject of a level classes given
     * @param level The level of classes
     * @param name_subject The name of subject
     */
    private void setDataLineChart(int level, String name_subject) {
        // Clear the chart
        line_chart_2.getData().clear();
        try {
            // Get the name of classes by a given level
            JSONArray name_classes = (JSONArray) getDataAPI("getClassesNameByLevel", level);

            // We go through all classes
            name_classes.forEach(name_class -> {
                try {
                    // Get all marks by subject of the class
                    JSONObject marks = (JSONObject) getDataAPI("unionMarksBySubjectForClass", name_class);
                    // If the class has the subject, we create and put a gaussian on the chart
                    if (marks.has(name_subject)) {
                        makeGaussian(line_chart_2, marks.getJSONArray(name_subject).toList(), name_class.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all exams for a given subject and put them in the exams list
     * @param name_subject The name of subject
     */
    private void setExamsForSubject(String name_subject) {
        try {
            // Clear the exams list
            exams_list.getItems().clear();
            // Get the number of exams for a subject
            int number_exams = ((JSONArray) getDataAPI("getSubjectInfo", name_subject, "number_marks")).getInt(0);

            // Add every exams of this subject
            for (int cpt = 1; cpt <= number_exams; cpt++) {
                exams_list.getItems().add("Épreuve " +  cpt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set data for the Bar chart in the fifth tab
     * This chart compares the average of a subject for all classes of a level
     * @param level The level of classes
     * @param name_subject The name of subject
     */
    private void setDataBarChart(int level, String name_subject) {
        try {
            // Clear the bar chart
            bar_chart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            // Get all subjects information for all classes of a level
            JSONObject data = (JSONObject) getDataAPI("getInfoSubjectsForLevel", level);

            // We go through all classes
            data.keySet().forEach(name_class -> {
                // If the class has the given subject, we calculate and add the average to the chart
                if (data.getJSONObject(name_class).has(name_subject)) {
                    JSONObject subject = data.getJSONObject(name_class).getJSONObject(name_subject);
                    double average = subject.getDouble("moyenne");
                    series.getData().add(new XYChart.Data<>(name_class, average));
                }
            });

            bar_chart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute when user chooses an exam in the exams list in the sixth tab
     * Put information in the chart by level, subject and the exam selected
     */
    public void clickedExam() {
        setDataLineChartExam(6 - choice_level_2.getSelectionModel().getSelectedIndex(), subjects_list_2.getSelectionModel().getSelectedItem(), exams_list.getSelectionModel().getSelectedIndex());
    }

    /**
     * Get and set data to the table in the fourth tab
     * The table shows subjects information for a class
     * @param level The level of a class
     * @param a_class The name of a class
     */
    public void setDataTable(int level, String a_class) {
        // Check if the name is not null
        if (a_class == null) {
            return;
        }
        try {
            // Clear the table
            table.getItems().clear();

            // Get all subjects information for a class
            JSONObject data_class = (JSONObject) getDataAPI("getInfoSubjectsForClass", level + a_class);
            ArrayList<InfoSubjects> info_subjects_table = new ArrayList<>();

            data_class.keySet().forEach(name_class -> {
                // Make the classes subjects readable
                JSONObject data_subjects = data_class.getJSONObject(name_class);

                // We go through all subjects
                data_subjects.keySet().forEach(name_subject -> {
                    JSONObject data_subject = data_subjects.getJSONObject(name_subject);
                    // Add subject's data on the table
                    info_subjects_table.add(new InfoSubjects(
                            name_subject,
                            data_subject.get("maximum").toString(),
                            data_subject.get("minimum").toString(),
                            data_subject.get("moyenne").toString(),
                            data_subject.get("médiane").toString()
                    ));
                });
            });

            ObservableList<InfoSubjects> data_models = FXCollections.observableArrayList(info_subjects_table);
            table.setItems(data_models);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute when user chooses a class in the classes list
     * Put information in the table by the level and the class selected
     */
    public void clickedClasses() {
        if (tab_pane.getSelectionModel().getSelectedIndex() == 0) {
            setDataTableClass(6 - level_list_3.getSelectionModel().getSelectedIndex(), classes_list_2.getSelectionModel().getSelectedItem());
        } else if ((tab_pane.getSelectionModel().getSelectedIndex() == 3)){
            setDataTable(6 - level_list_2.getSelectionModel().getSelectedIndex(), classes_list.getSelectionModel().getSelectedItem());
        }else LOGGER.log(Level.WARNING, "Wrong tab");
    }

    /**
     * Execute when user chooses a level in select menus
     */
    public void clickedLevel() {
        switch (tab_pane.getSelectionModel().getSelectedIndex()) {
            // Make Bar chart with level and subject selected in the fifth tab
            case 4 -> setDataBarChart(6 - choice_level.getSelectionModel().getSelectedIndex(), subjects_list.getSelectionModel().getSelectedItem());
            // Make Line chart with level, subject and exam selected in the sixth tab
            case 5 -> setDataLineChartExam(6 - choice_level_2.getSelectionModel().getSelectedIndex(), subjects_list_2.getSelectionModel().getSelectedItem(), exams_list.getSelectionModel().getSelectedIndex());
            // Make Line chart with level and subject selected in the seventh tab
            case 6 -> setDataLineChart(6 - choice_level_3.getSelectionModel().getSelectedIndex(), subjects_list_3.getSelectionModel().getSelectedItem());
            default -> LOGGER.log(Level.WARNING, "Wrong tab");
        }
    }

    /**
     * Set marks for the selected class
     * @param level The level of the class
     * @param name_class The name of the class
     */
    public void setDataTableClass(int level, String name_class) {
        // Check if the name is not null
        if (name_class == null) {
            return;
        }
        try {
            // Clear the class table
            table_class.getItems().clear();

            // Get all marks for the class
            JSONObject marks = (JSONObject) getDataAPI("getMarksForClass", level + name_class);

            marks.getJSONArray(level + name_class).forEach(a_student -> {
                // Make the student object readable
                JSONObject object_student = new JSONObject(a_student.toString());
                JSONObject object_subjects = object_student.getJSONObject("matieres");

                ObservableList<String> row = FXCollections.observableArrayList();

                // Put the student name in the "Noms" column
                row.add(0, object_student.get("nom") + " " + object_student.get("prenom"));

                // Put marks for every subjects columns
                for (int column = 1; column < table_class.getColumns().size(); column++) {
                    String name_column = table_class.getColumns().get(column).getText();

                    // Check if the student has the subject
                    if (object_subjects.has(name_column)) {
                        row.add(column, object_subjects.getJSONObject(name_column).getJSONArray("notes").toString());
                    }else row.add(column, "");
                }

                // Put the row in the table
                table_class.getItems().add(row);
            });
        } catch (ServerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set marks for the classes of the given level
     * @param level The level of the classes
     */
    public void setDataTableLevel(int level) {
        try {
            // Clear the level table
            table_level.getItems().clear();

            // Get all marks for the classes
            JSONObject classes_marks = (JSONObject) getDataAPI("getMarksForLevel", level);

            // We go through all students of all classes
            classes_marks.keySet().forEach(name_class -> classes_marks.getJSONArray(name_class).forEach(a_student -> {
                JSONObject object_student = new JSONObject(a_student.toString());
                JSONObject object_subjects = object_student.getJSONObject("matieres");
                ObservableList<String> row = FXCollections.observableArrayList();

                // We put the name and the class of the student in the table
                row.add(0, object_student.get("nom") + " " + object_student.get("prenom"));
                row.add(1, name_class);

                // Put marks for every subjects columns
                for (int column = 2; column < table_level.getColumns().size(); column++) {
                    String name_column = table_level.getColumns().get(column).getText();

                    // Check if the student has the subject
                    if (object_subjects.has(name_column)) {
                        row.add(column, object_subjects.getJSONObject(name_column).getJSONArray("notes").toString());
                    }else row.add(column, "");
                }

                // Put the row in the table
                table_level.getItems().add(row);
            }));
        } catch (ServerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set marks for the selected student
     * @param last_name The last name of the student
     * @param first_name The first name of the student
     */
    public void setDataTableStudent(String last_name, String first_name) {
        try {
            // Clear the student table
            table_student.getItems().clear();

            // Get all marks for the student
            JSONObject student_marks = (JSONObject) getDataAPI("getMarksForStudent", last_name, first_name);
            JSONObject student_subjects = student_marks.getJSONObject("matieres");

            // Put marks for every subjects in the table
            student_subjects.keySet().forEach(name_subject -> {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(0, name_subject);
                row.add(1, student_subjects.getJSONObject(name_subject).getJSONArray("notes").toString());
                table_student.getItems().add(row);
            });
        } catch (ServerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute when user chooses a level in the levels lists
     */
    public void clickedLevels() {
        switch (tab_pane.getSelectionModel().getSelectedIndex()) {
            // Add data in the class table with level, class selected in the first tab
            case 0 -> setDataTableClass(6 - level_list_3.getSelectionModel().getSelectedIndex(), classes_list_2.getSelectionModel().getSelectedItem());
            // Add data in the table with level selected in the third tab
            case 2 -> setDataTableLevel(6 - level_list_4.getSelectionModel().getSelectedIndex());
            // Add data in the table with level, class selected in the fourth tab
            case 3 -> setDataTable(6 - level_list_2.getSelectionModel().getSelectedIndex(), classes_list.getSelectionModel().getSelectedItem());
            // Make Line chart with level selected in the eighth tab
            case 7 -> setDataLineChartOverallAverage(6 - level_list.getSelectionModel().getSelectedIndex());
            default -> LOGGER.log(Level.WARNING, "Wrong tab");

        }
    }

    /**
     * Execute when user chooses a class
     */
    public void clickedAllClasses() {
        try {
            // Add every students name in the students list in the selected tab
            if (tab_pane.getSelectionModel().getSelectedIndex() == 1) {
                // Clear the list
                students_list_2.getItems().clear();

                // Get all students name for the selected class in the second tab
                JSONArray students = (JSONArray) getDataAPI("getStudentsForClass", choice_classes_2.getSelectionModel().getSelectedItem());
                students.forEach(name_student -> students_list_2.getItems().add(name_student.toString()));
            }else {
                // Clear the list
                students_list.getItems().clear();
                resetImportTable();

                // Get all students name for the selected class in the import tab
                JSONArray students = (JSONArray) getDataAPI("getStudentsForClass", choice_classes.getSelectionModel().getSelectedItem());
                students.forEach(name_student -> students_list.getItems().add(name_student.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute when user click to the button to add a student to import
     */
    public void addStudent() {
        // Get all selected information
        String student_selected = students_list.getSelectionModel().getSelectedItem();
        String mark = mark_input.getText();

        if (student_selected != null && mark != null) {
            String[] name_student = student_selected.split(" ");
            // Add student with his mark in the table
            table_import.getItems().add(new StudentTable(name_student[0], name_student[1], mark));
        }
    }

    /**
     * Execute when user click to the button to reset the import table
     */
    public void resetImportTable() {
        // Clear the table
        table_import.getItems().clear();
    }

    /**
     * Execute when user click to the button to send table's information to the server
     */
    public void sendStudentsMarks() {
        if (!table_import.getItems().isEmpty()) {
            String import_file_path = "./import.txt";
            try(FileWriter import_file = new FileWriter(import_file_path)) {
                // Write the class and the subject in the file
                import_file.write(choice_classes.getSelectionModel().getSelectedItem() + "\n");
                import_file.write(subjects_list_4.getSelectionModel().getSelectedItem());

                // We go through all students to write them in the file
                table_import.getItems().forEach(student_table -> {
                    try {
                        import_file.write("\n");
                        import_file.write(student_table.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                getDataAPI("importData", import_file_path);
                resetImportTable();
            } catch (ServerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Execute when user choose a student for the second tab
     */
    public void clickedStudent() {
        String[] name_student = students_list_2.getSelectionModel().getSelectedItem().split(" ");
        setDataTableStudent(name_student[0], name_student[1]);
    }
}
