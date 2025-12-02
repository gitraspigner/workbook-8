package com.pluralsight.Exercise6SakilaDataManager;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * Handles opening & closing the database connection, searching it for information,
 * and returning the information to the main application class (SakilaApplication)
 * for the Sakila Movies database.
 *
 * @author Ravi Spigner
 */
public class SakilaDataManager {
    private DataSource dataSource;
    public SakilaDataManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public List<SakilaActor> getActorsByLastName(String lastName)
            throws SQLException {
        List<SakilaActor> actors = new ArrayList<>();
        //Perform Query To retrieve Actors
        String query = "SELECT actor_id, first_name, last_name FROM actor WHERE last_name = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);) {
                statement.setString(1, lastName);
                try (ResultSet results = statement.executeQuery();) {
                    //Process Results
                    while (results.next()) {
                        int idResult = results.getInt("actor_id");
                        String firstNameResult = results.getString("first_name");
                        String lastNameResult = results.getString("last_name");
                        actors.add(new SakilaActor(idResult, firstNameResult, lastNameResult));
                    }
                }
            }
        //Return Results
        return actors;
    }
    public List<SakilaFilm> getFilmsByActorId(int actorId)
            throws SQLException {
        List<SakilaFilm> films = new ArrayList<>();
        //Perform Query To retrieve Films
        String query = "SELECT f.film_id, f.title, f.release_year, f.length FROM actor a " +
                "JOIN film_actor fa ON a.actor_id = fa.actor_id " +
                "JOIN film f ON fa.film_id = f.film_id WHERE a.actor_id = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);) {
                statement.setInt(1, actorId);
                try (ResultSet results = statement.executeQuery();) {
                    //Process Results
                    while (results.next()) {
                        int idResult = results.getInt("film_id");
                        String titleResult = results.getString("title");
                        String releaseYearResult = results.getString(
                                "release_year").substring(0, 4);
                        String lengthResult = results.getString("length");
                        films.add(new SakilaFilm(idResult, titleResult, releaseYearResult,
                                lengthResult));
                    }
                }
            }
        //Return Results
        return films;
    }
}
