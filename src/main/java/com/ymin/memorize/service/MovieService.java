package com.ymin.memorize.service;

import com.ymin.memorize.dto.Script;
import com.ymin.memorize.dto.Movie;
import com.ymin.memorize.mapper.ScriptMapper;
import com.ymin.memorize.mapper.MovieMapper;
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
    ScriptMapper scriptMapper;

    // 제목으로 영화 검색 후 리스트 반환
    public List<Movie> findMovieByTitle(String title){
        ArrayList<Movie> movieArrayList = new ArrayList<>();
        movieArrayList.addAll(movieMapper.findMovieByE_title(title)); //영어 제목으로 검색
        movieArrayList.addAll(movieMapper.findMovieByK_title(title)); //한글 제목으로 검색

        // 해당 영화의 스크립트 검색
        for (Movie movie : movieArrayList) {
            int movie_id = movie.getId();
            movie.setScriptList(scriptMapper.getScriptListByMovieId(movie_id));
        }

        return movieArrayList;
    }

    // Movie_ID 로 영화 검색
    public Movie getMovieByMovieId(int id){
        return movieMapper.getMovieByMovieId(id);
    }

    // Movie_id 로 스크립트 검색하여 반환
    public List<Script> getScriptListByMovieId(int movie_id){
        return scriptMapper.getScriptListByMovieId(movie_id);
    }

    // 단어로 자막 검색하여 해당 영화와 자막 리스트 반환
    public List<Movie> getScriptListByWord(String word){
        ArrayList<Script> scriptList = (ArrayList<Script>) scriptMapper.getScriptListByWord(word);
        ArrayList<Movie> movieList = new ArrayList<>();

        // 자막을 체크하여 해당 자막의 영화 리스트에 넣어준다
        for(Script script : scriptList){
            Movie movie = movieMapper.getMovieByMovieId(script.getMovie_id());
            // movieList에 이미 해당 영화가 존재한다면 그냥 추가
            if(movieList.contains(movie)){
                int index = movieList.indexOf(movie);
                movieList.get(index).getScriptList().add(script);
            }
            else{
                movie.getScriptList().add(script);
                movieList.add(movie);
            }
        }

        return movieList;
    }

    // 제목으로 검색하여 영화 개수 반환
    public int getMovieCount(String title){
        return title == null ? movieMapper.getMovieCount() : movieMapper.getMovieCountByTitle(title);
    }

    // 영화 제목과 limit(컨텐츠 개수), offset(페이지) 를 받아 영화 리스트를 반환
    public List<Movie> getMovieList(String title, int limit, int offset){
        return title == null ? movieMapper.getMovieListPage(limit,offset) : movieMapper.getMovieListPageByTitle(title, limit, offset);
    }

    // 영화 번호, 포함될 단어, limit, offset 을 받아 자막 리스트를 반환
    public List<Script> getScriptList(int movie_id, String word, int limit, int offset){
        return word == null? scriptMapper.getScriptListPage(movie_id, limit, offset) : scriptMapper.getScriptListPageByWord(movie_id, word, limit, offset);
    }

    public int getScriptCount(int movie_id, String word){
        return word == null ? scriptMapper.getScriptCount(movie_id) : scriptMapper.getScriptCountByWord(movie_id, word);
    }

    public Script getScriptById(int id){
        return scriptMapper.getScriptById(id);
    }
}
