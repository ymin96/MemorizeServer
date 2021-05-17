package com.ymin.memorize.controller;

import com.ymin.memorize.dto.Movie;
import com.ymin.memorize.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MovieRestController {

    @Autowired
    MovieService movieService;

    // 영화 제목 으로 검색 후 리턴
    @RequestMapping(value = "/title/{title}", method = RequestMethod.GET)
    List<Movie> getMovieByTitle(@PathVariable String title) {
        return movieService.findMovieByTitle(title);
    }

    //자막 내용 으로 검색 후 리턴
    @RequestMapping(value = "/caption/{word}", method = RequestMethod.GET)
    List<Movie> getMovieByCaption(@PathVariable String word) {
        return movieService.getScriptListByWord(word);
    }

    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    Map<String, Object> getMovies(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "limit", defaultValue = "16") int limit,
            @RequestParam(value = "offset" , defaultValue = "1") int offset) {
        Map<String, Object> response_json = new HashMap<>();

        int movie_count = movieService.getMovieCount(title);
        int last_page = (int) Math.ceil((double) movie_count / limit);

        offset = (offset < 0) ? 0 : (offset > last_page)? last_page - 1 : offset - 1;
        offset *= limit;
        List<Movie> movie_list = movieService.getMovieList(title, limit, offset);

        response_json.put("cur_page", offset);
        response_json.put("movie_list", movie_list);
        response_json.put("last_page", last_page);
        return response_json;
    }
}
