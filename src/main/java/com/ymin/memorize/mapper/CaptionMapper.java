package com.ymin.memorize.mapper;

import com.ymin.memorize.dto.Caption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CaptionMapper {

    @Select("SELECT * FROM caption WHERE movie_id = #{id}")
    List<Caption> getCaptionListByMovieId(@Param("id") int movie_id);
}
