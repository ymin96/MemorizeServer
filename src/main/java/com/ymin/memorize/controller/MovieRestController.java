package com.ymin.memorize.controller;

import com.ymin.memorize.dto.Movie;
import com.ymin.memorize.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieRestController {

    @Autowired
    MovieService movieService;

    @RequestMapping(value = "/title/{title}", method = RequestMethod.GET)
    List<Movie> getMovieByTitle(@PathVariable String title){
        return movieService.findMovieByTitle(title);
    }

    @RequestMapping(value = "/caption/{word}", method = RequestMethod.GET)
    List<Movie> getMovieByCaption(@PathVariable String word){
        return movieService.getCaptionListByWord(word);
    }
}
