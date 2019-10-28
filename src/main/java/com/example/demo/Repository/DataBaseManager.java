package com.example.demo.Repository;

import com.example.demo.models.Movie;
import com.example.demo.models.Rating;
import com.example.demo.models.TmpLogin;
import com.example.demo.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Repository
public class DataBaseManager {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Statement stmt;
    private Connection connection;
    private PreparedStatement pstmt;

    private void getDBConnection() throws SQLException {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "Garne", "oJ7!5qsxBktlQtw9!qJX3I");
                stmt = connection.createStatement();
            } catch (ClassNotFoundException e) {
                log.info("com.mysql.jdba.Driver not found...");
            }
    }

    public void createNewMovieData(Movie movie) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "Garne", "oJ7!5qsxBktlQtw9!qJX3I");
            pstmt = connection.prepareStatement("INSERT INTO ap.movies (title, releaseYear, genre, director) VALUES (?, ?, ?, ?)");
            pstmt.setString(1, movie.getTitle());
            pstmt.setInt(2, movie.getReleaseYear());
            pstmt.setString(3, movie.getGenre());
            pstmt.setString(4, movie.getDirector());
            log.info("Data query sent: " + pstmt);
            pstmt.executeUpdate();

            //Getting Database Movie_id
            log.info("Getting newly created ID from database");
            pstmt = connection.prepareStatement("SELECT movie_id, title FROM ap.movies WHERE title LIKE ?");
            pstmt.setString(1, movie.getTitle());
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                int movie_id = rs.getInt("movie_id");
                String title = rs.getString("title");
                Movie tempMovie = new Movie (movie_id, title);
                sendRating(tempMovie, movie);
            }
            pstmt.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void sendRating(Movie tempMovie, Movie movie) {
        try {
            log.info("Setting initial rating");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "Garne", "oJ7!5qsxBktlQtw9!qJX3I");
            pstmt = connection.prepareStatement("INSERT INTO ap.movieratings (user_id, movie_id, ratings) VALUES (?, ?, ?)");
            pstmt.setInt(1, 1);
            pstmt.setInt(2, tempMovie.getMovie_id());
            pstmt.setDouble(3, movie.getRating());
            log.info(String.valueOf(pstmt));
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchDataBase(ArrayList<Movie> movieArrayList, String SearchKey) {
        try {
            log.info("search initiated");
            //View only bruger kan kun se ap.movies og ap.movieRatings
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "viewonly", "viewonly");
            pstmt = connection.prepareStatement("SELECT movies.movie_id, title, releaseYear, genre, director, FORMAT(AVG(ratings),2) AS rating FROM ap.movies " +
                    "JOIN ap.movieRatings ON movies.movie_id = movieratings.movie_id " +
                    "WHERE (title LIKE ? or releaseYear LIKE ? or genre LIKE ? or director LIKE ?) GROUP BY movie_id ");
            pstmt.setString(1, "%" + SearchKey + "%");
            pstmt.setString(2, "%" + SearchKey + "%");
            pstmt.setString(3, "%" + SearchKey + "%");
            pstmt.setString(4, "%" + SearchKey + "%");
            log.info(String.valueOf(pstmt));
            ResultSet rs = pstmt.executeQuery();
            insertIntoArrayList(movieArrayList, rs);
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertIntoArrayList(ArrayList<Movie> movieArrayList, ResultSet rs) throws SQLException {
        while (rs.next()){
            int movie_id = rs.getInt("movie_id");
            String title = rs.getString("title");
            int releaseYear = rs.getInt("releaseYear");
            String genre = rs.getString("genre");
            String director = rs.getString("director");
            double rating = rs.getDouble("rating");
            movieArrayList.add(new Movie(movie_id, title, releaseYear, genre, director, rating));
        }
        log.info("Arraylist created");
    }

    public void viewTable(ArrayList<Movie> movieArrayList) throws SQLException {
            stmt = null;
            String query = "SELECT movies.movie_id, title, releaseYear, genre, director, FORMAT(AVG(ratings),2) AS rating FROM ap.movies " +
                    "JOIN ap.movieRatings ON movies.movie_id = movieratings.movie_id GROUP BY movies.movie_id";
            try {
                getDBConnection();
                ResultSet rs = stmt.executeQuery(query);
                insertIntoArrayList(movieArrayList, rs);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
    }

    public void deleteMovie (int id){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "Garne", "oJ7!5qsxBktlQtw9!qJX3I");
            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM ap.movies WHERE movie_id = ?");
            pstmt.setInt(1, id);
            log.info(String.valueOf(pstmt));
            pstmt.executeUpdate();
            pstmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean validateLogin(TmpLogin loginAttempt) {
        boolean result = false; //TODO lav en success/failed for login
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "CsaE2PTRLxle#cYThl08br^6");
            PreparedStatement pstmt = connection.prepareStatement("SELECT userName, userPassword FROM ap.accounts WHERE userName = ?  AND userPassword = ?");
            pstmt.setString(1, loginAttempt.getTmpUsername());
            pstmt.setString(2, loginAttempt.getTmpPassword());
            log.info("Database called: " + pstmt);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                log.info("Login successfull");
            } else {
                log.info("Login failed");
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void createNewUser(User user) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "Garne", "oJ7!5qsxBktlQtw9!qJX3I");
            pstmt = connection.prepareStatement("INSERT INTO ap.accounts (userName, userPassword) VALUES (?, ?)");
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getUserPassword());
            log.info("Data query sent: " + pstmt);
            pstmt.executeUpdate();
            pstmt.close();
            log.info("User created");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void editDatabaseEntry(Movie tmpmovie) {
        try {
            log.info("Edit called");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "Garne", "oJ7!5qsxBktlQtw9!qJX3I");
            pstmt = connection.prepareStatement("UPDATE ap.movies SET title = ?, releaseYear = ?, genre = ?, director = ? " +
                    "WHERE movie_id = ?");
            pstmt.setString(1, tmpmovie.getTitle());
            pstmt.setInt(2, tmpmovie.getReleaseYear());
            pstmt.setString(3, tmpmovie.getGenre());
            pstmt.setString(4, tmpmovie.getDirector());
            pstmt.setInt(5, tmpmovie.getMovie_id());

            log.info(String.valueOf(pstmt));
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rateMovie(Rating rating) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "Garne", "oJ7!5qsxBktlQtw9!qJX3I");
            pstmt = connection.prepareStatement("INSERT INTO ap.movieratings (user_id, movie_id, ratings) VALUES (?, ?, ?)");
            pstmt.setInt(1, rating.getUser_id());
            pstmt.setInt(2, rating.getMovie_id());
            pstmt.setDouble(3, rating.getUserRating());
            log.info("Data query sent: " + pstmt);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
}