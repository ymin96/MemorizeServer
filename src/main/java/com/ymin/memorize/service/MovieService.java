package com.ymin.memorize.service;

import com.ymin.memorize.dto.Movie;
import com.ymin.memorize.mapper.CaptionMapper;
import com.ymin.memorize.mapper.MovieMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Repository
@Transactional
public class MovieService {

    @Autowired
    MovieMapper movieMapper;

    @Autowired
    CaptionService captionService;

    public List<Movie> findMovieByTitle(String title){
        ArrayList<Movie> movieArrayList = new ArrayList<>();
        movieArrayList.addAll(movieMapper.findMovieByE_title(title));
        movieArrayList.addAll(movieMapper.findMovieByK_title(title));

        for (Movie movie : movieArrayList) {
            int movie_id = movie.getId();
            movie.setCaptionList(captionService.getCaptionListByMovieId(movie_id));
        }

        return movieArrayList;
    }
}
