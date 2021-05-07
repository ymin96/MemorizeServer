package com.ymin.memorize.mapper;

import com.ymin.memorize.dto.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MovieMapper {

    @Select("SELECT * " +
            "FROM movie " +
            "WHERE k_title LIKE CONCAT('%',#{title},'%')")
    List<Movie> findMovieByK_title(@Param("title") String title);

    @Select("SELECT * " +
            "FROM movie " +
            "WHERE e_title LIKE CONCAT('%',#{title},'%')")
    List<Movie> findMovieByE_title(@Param("title") String title);

    @Select("SELECT * " +
            "FROM movie WHERE id = #{id}")
    Movie getMovieByMovieId(@Param("id") int id);

    @Select("SELECT count(*) " +
            "FROM movie")
    int getMovieCount();

    @Select("SELECT count(*) " +
            "FROM movie " +
            "WHERE  k_title LIKE CONCAT('%',#{title},'%') OR  e_title LIKE CONCAT('%',#{title},'%')")
    int getMovieCountByTitle(@Param("title")String title);

    @Select("SELECT * " +
            "FROM movie " +
            "WHERE k_title LIKE CONCAT('%',#{title},'%') OR  e_title LIKE CONCAT('%',#{title},'%') " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<Movie> getMovieListPageByTitle(@Param("title")String title,@Param("limit") int limit,@Param("offset") int offset);

    @Select("SELECT * " +
            "FROM movie " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<Movie> getMovieListPage(@Param("limit") int limit,@Param("offset") int offset);
}
