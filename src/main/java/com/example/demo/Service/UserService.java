package com.example.demo.Service;

import com.example.demo.models.Movie;

public interface UserService {
    void save(Movie movies);
    void update(Movie movies);
    void delete(int id);
    void findID(int movie_id);
}
