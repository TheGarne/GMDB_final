package com.example.demo.models;

public class Movie {
    private int movie_id;
    private String title;
    private int releaseYear;
    private String genre;
    private String director;
    private double rating = 0.0; //

    public Movie(String title) {
        this.title = title;
    }
    public Movie(int movie_id) {
        this.movie_id = movie_id;
    }
    public Movie(String title, int releaseYear, String genre, String director) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.director = director;
    }
    public Movie(int movie_id, String title, int releaseYear, String genre, String director, double rating) {
        this.movie_id = movie_id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.director = director;
        this.rating = rating;
    }
    public Movie(String title, int releaseYear, String genre, String director, double rating) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.director = director;
        this.rating = rating;
    }
    public Movie() {

    }
    public Movie(int movie_id, String title) {
        this.title = title;
        this.movie_id = movie_id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
