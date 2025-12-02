package com.pluralsight.Exercise6SakilaDataManager;
/**
 * Represents an Actor (entry within the Actor table of the Sakila Movie database).
 * Only 3 columns of the table have been included as fields (according to the
 * writeup for this exercise) for brevity.
 *
 * @author Ravi Spigner
 */
public class SakilaActor {
    private int id;
    private String first_name;
    private String last_name;
    public SakilaActor(int id, String first_name, String last_name) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
    }
    public int getId() {
        return id;
    }
    public String getFirst_name() {
        return first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
}
