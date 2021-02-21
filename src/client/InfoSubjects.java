package client;

import javafx.beans.property.SimpleStringProperty;

/**
 * DataChart is a class to put data in the table in the fourth tab
 *
 * @author Romain DUBUC
 * @version 1.0
 */
public class InfoSubjects {
    /**
     * The name of a subject
     */
    private final SimpleStringProperty subjects_tab;

    /**
     * The maximum mark of a subject
     */
    private final SimpleStringProperty max_tab;

    /**
     * The minimum mark of a subject
     */
    private final SimpleStringProperty min_tab;

    /**
     * The average of a subject
     */
    private final SimpleStringProperty averages_tab;

    /**
     * The median of a subject
     */
    private final SimpleStringProperty medians_tab;

    /**
     * A row of the table in the fourth tab
     * @param subjects_tab Name of a subject
     * @param max_tab Maximum mark of a subject
     * @param min_tab Minimum mark of a subject
     * @param averages_tab Average of a subject
     * @param medians_tab Median of a subject
     */
    public InfoSubjects(String subjects_tab, String max_tab, String min_tab, String averages_tab, String medians_tab) {
        this.subjects_tab = new SimpleStringProperty(subjects_tab);
        this.max_tab = new SimpleStringProperty(max_tab);
        this.min_tab = new SimpleStringProperty(min_tab);
        this.averages_tab = new SimpleStringProperty(averages_tab);
        this.medians_tab = new SimpleStringProperty(medians_tab);
    }

    /**
     * Getter for the name of a subject
     * @return (String) the name of a subject
     */
    @SuppressWarnings("unused")
    public String getSubjectsTab() {
        return subjects_tab.get();
    }

    /**
     * Getter for the maximum mark of a subject
     * @return (String) the maximum mark of a subject
     */
    @SuppressWarnings("unused")
    public String getMaxTab() {
        return max_tab.get();
    }

    /**
     * Getter for the minimum mark of a subject
     * @return (String) the minimum mark of a subject
     */
    @SuppressWarnings("unused")
    public String getMinTab() {
        return min_tab.get();
    }

    /**
     * Getter for the average of a subject
     * @return (String) the average of a subject
     */
    @SuppressWarnings("unused")
    public String getAveragesTab() {
        return averages_tab.get();
    }

    /**
     * Getter for the median of a subject
     * @return (String) the median of a subject
     */
    @SuppressWarnings("unused")
    public String getMediansTab() {
        return medians_tab.get();
    }
}
