package com.example.demo.Controller;

import com.example.demo.Repository.DataBaseManager;
import com.example.demo.Service.*;
import com.example.demo.models.Movie;
import com.example.demo.models.Rating;
import com.example.demo.models.TmpLogin;
import com.example.demo.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;

@Controller
public class HomeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private boolean result = false;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    //TODO thymeleaf fragment og HTTP session

    //@Autowired
    //UserServiceImpl userService;
    @Autowired
    DataBaseManager DBM;

    @GetMapping ("/")
    public String index(Model model) throws SQLException {
        movieArrayList.clear();
        model.addAttribute("tmpLogin", new TmpLogin());
        DBM.viewTable(movieArrayList);
        model.addAttribute("movies", movieArrayList);
        return "index";
    }

    @PostMapping("/login")
    public String login (Model model, HttpSession session, TmpLogin loginAttempt) throws ClassNotFoundException {
        model.addAttribute("loginAttempt", new TmpLogin());
        log.info("'" + loginAttempt.getTmpUsername() + "' tried to log in");
        DBM.validateLogin(loginAttempt);
        if (result == true){ //virker ikke
            User user = (User) session.getAttribute("user");
            log.info("User session started...");
        }
        return "redirect:/";
    }

    @GetMapping ("/edit")
    public String edit (Model model) {
        model.addAttribute("tmpLogin", new TmpLogin());
        model.addAttribute("tmpmovie", new Movie());
        //model.addAttribute("movie", Movie); til th:object
        return "edit";
    }

    @PostMapping ("/edit")
    public String edit (@ModelAttribute Movie tmpmovie) {
        DBM.editDatabaseEntry(tmpmovie);
        return "redirect:/";
    }

    @GetMapping ("/rate")
    public String rate (Model model) {
        model.addAttribute("tmpLogin", new TmpLogin());
        model.addAttribute("ratingO", new Rating());
        return "rate";
    }

    @PostMapping ("/rate")
    public String rate (@ModelAttribute Rating rating){
        if (rating.getUserRating() <= 10 && rating.getUserRating() >= 0) {
            DBM.rateMovie(rating);
        }
        return "redirect:/";
    }

    @GetMapping ("/create")
    public String create (Model model){
        model.addAttribute("tmpLogin", new TmpLogin());
        model.addAttribute("movie", new Movie());
        return "create";
    }

    @PostMapping ("/create")
    public String create(@ModelAttribute Movie movie, Model model) {
        DBM.createNewMovieData(movie);
        return "redirect:/";
    }

    @PostMapping ("/search")
    public String search (String searchKey, Model model) {
        movieArrayList.clear();
        model.addAttribute("tmpLogin", new TmpLogin());
        DBM.searchDataBase(movieArrayList, searchKey);
        model.addAttribute("movies", movieArrayList);
        return "search";
    }

    @GetMapping ("/delete/{movie_id}")
    public String delete (@PathVariable ("movie_id") int movie_id, Model model) {
        log.info("Delete initiated");
        model.addAttribute("tmpLogin", new TmpLogin());
        DBM.deleteMovie(movie_id);
        return "redirect:/";
    }

    @GetMapping ("/createUser")
    public String createUser (Model model){
        model.addAttribute("tmpLogin", new TmpLogin());
        model.addAttribute("user", new User());
        return "createUser";    }

    @PostMapping ("/createUser")
    public String createUser (@ModelAttribute User user, Model model) {
        DBM.createNewUser(user);
        return "redirect:/";
    }

    @GetMapping ("/details/{movie_id}")
    public String details (@PathVariable ("movie_id") int movie_id, Model model){
        model.addAttribute("tmpLogin", new TmpLogin());
        model.addAttribute("movie", movieArrayList.get(movie_id-1));
        return ("details");
    }
}
