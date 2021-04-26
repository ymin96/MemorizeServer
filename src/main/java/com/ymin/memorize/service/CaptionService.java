package com.ymin.memorize.service;


import com.ymin.memorize.dto.Caption;
import com.ymin.memorize.mapper.CaptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Repository
@Transactional
public class CaptionService {

    @Autowired
    CaptionMapper captionMapper;

    public List<Caption> getCaptionListByMovieId(int movie_id){
        return captionMapper.getCaptionListByMovieId(movie_id);
    }

}
