package com.ymin.memorize.mapper;

import com.ymin.memorize.dto.Script;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ScriptMapper {

    @Select("SELECT * " +
            "FROM caption " +
            "WHERE movie_id = #{id}")
    List<Script> getScriptListByMovieId(@Param("id") int movie_id);

    @Select("SELECT * " +
            "FROM caption " +
            "WHERE caption LIKE CONCAT('%',#{word},'%')")
    List<Script> getScriptListByWord(@Param("word") String word);

    @Select("SELECT * " +
            "FROM caption " +
            "WHERE movie_id = #{id} " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<Script> getScriptListPage(@Param("id") int movie_id, @Param("limit") int limit,@Param("offset") int offset);

    @Select("SELECT * " +
            "FROM caption " +
            "WHERE movie_id = #{id} AND caption LIKE CONCAT('%',#{word},'%') " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<Script> getScriptListPageByWord(@Param("id") int movie_id,@Param("word")String word, @Param("limit") int limit,@Param("offset") int offset);

    @Select("SELECT count(*) " +
            "FROM caption " +
            "WHERE movie_id = #{movie_id}")
    int getScriptCount( @Param("movie_id") int movie_id);

    @Select("SELECT count(*) " +
            "FROM caption " +
            "WHERE movie_id = #{movie_id} AND caption LIKE CONCAT('%',#{word},'%')")
    int getScriptCountByWord(@Param("movie_id")int movie_id, @Param("word") String word);


    @Select("SELECT * " +
            "FROM caption " +
            "WHERE id = #{id}")
    Script getScriptById(@Param("id")int id);
}
