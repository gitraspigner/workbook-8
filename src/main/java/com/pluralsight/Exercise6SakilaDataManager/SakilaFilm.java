package com.pluralsight.Exercise6SakilaDataManager;
/**
 * Represents a Film (entry within the Film table of the Sakila Movie database).
 * Only 4 columns of the table have been included as fields (according to the
 * writeup for this exercise) for brevity.
 *
 * @author Ravi Spigner
 */
public class SakilaFilm {
    private int film_id;
    private String title;
    private String release_year;
    private String length;
    public SakilaFilm(int film_id, String title, String release_year, String length) {
        this.film_id = film_id;
        this.title = title;
        this.release_year = release_year;
        this.length = length;
    }
    @Override
    public String toString() {
        return "id: " + film_id +
                ", title: " + title +
                ", release year: " + release_year +
                ", length: " + length;
    }
    public int getFilm_id() {
        return film_id;
    }
    public void setFilm_id(int film_id) {
        this.film_id = film_id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getRelease_year() {
        return release_year;
    }
    public void setRelease_year(String release_year) {
        this.release_year = release_year;
    }
    public String getLength() {
        return length;
    }
    public void setLength(String length) {
        this.length = length;
    }
}
