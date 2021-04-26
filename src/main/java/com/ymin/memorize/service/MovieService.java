package com.ymin.memorize.service;

import com.ymin.memorize.dto.Caption;
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
    CaptionMapper captionMapper;

    public List<Movie> findMovieByTitle(String title){
        ArrayList<Movie> movieArrayList = new ArrayList<>();
        movieArrayList.addAll(movieMapper.findMovieByE_title(title));
        movieArrayList.addAll(movieMapper.findMovieByK_title(title));

        for (Movie movie : movieArrayList) {
            int movie_id = movie.getId();
            movie.setCaptionList(captionMapper.getCaptionListByMovieId(movie_id));
        }

        return movieArrayList;
    }

    public Movie getMovieByMovieId(int id){
        return movieMapper.getMovieByMovieId(id);
    }

    public List<Caption> getCaptionListByMovieId(int movie_id){
        return captionMapper.getCaptionListByMovieId(movie_id);
    }

    public List<Movie> getCaptionListByWord(String word){
        ArrayList<Caption> captionList = (ArrayList<Caption>) captionMapper.getCaptionListByWord(word);
        ArrayList<Movie> movieList = new ArrayList<>();

        for(Caption caption : captionList){
            Movie movie = movieMapper.getMovieByMovieId(caption.getMovie_id());
            if(movieList.contains(movie)){
                int index = movieList.indexOf(movie);
                movieList.get(index).getCaptionList().add(caption);
            }
            else{
                movie.getCaptionList().add(caption);
                movieList.add(movie);
            }
        }

        return movieList;
    }
}
