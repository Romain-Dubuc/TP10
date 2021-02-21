package client;

import javafx.beans.property.SimpleStringProperty;

/**
 * Student is a class to put data in the table in the import tab
 *
 * @author Romain DUBUC
 * @version 1.0
 */
public class StudentTable {
    /**
     * The last name of a student
     */
    private final SimpleStringProperty last_names_tab;

    /**
     * The first name of a student
     */
    private final SimpleStringProperty first_names_tab;

    /**
     * The mark of a student
     */
    private final SimpleStringProperty marks_tab;

    /**
     * A row of the table in the import tab
     * @param last_names_tab The last name of a student
     * @param first_names_tab The first name of a student
     * @param marks_tab The mark of a student
     */
    public StudentTable(String last_names_tab, String first_names_tab, String marks_tab) {
        this.last_names_tab = new SimpleStringProperty(last_names_tab);
        this.first_names_tab = new SimpleStringProperty(first_names_tab);
        this.marks_tab = new SimpleStringProperty(marks_tab);
    }

    /**
     * Getter for the last name of a student
     * @return (String) the last name of a student
     */
    @SuppressWarnings("unused")
    public String getLastNamesTab() {
        return last_names_tab.get();
    }

    /**
     * Getter for the first name of a student
     * @return (String) the first name of a student
     */
    @SuppressWarnings("unused")
    public String getFirstNamesTab() {
        return first_names_tab.get();
    }

    /**
     * Getter for the mark of a student
     * @return (String) the mark of a student
     */
    @SuppressWarnings("unused")
    public String getMarksTab() {
        return marks_tab.get();
    }

    /**
     * Make the class displayable
     * @return the last name, the first name and the mark of a student
     */
    public String toString() {
        return getLastNamesTab() + " " + getFirstNamesTab() + " " + getMarksTab();
    }
}
