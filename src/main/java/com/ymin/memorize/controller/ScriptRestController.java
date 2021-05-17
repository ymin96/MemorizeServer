package com.ymin.memorize.controller;

import com.ymin.memorize.dto.Script;
import com.ymin.memorize.service.MovieService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ScriptRestController {

    @Autowired
    MovieService movieService;

    @Value("${file.movie.path}")
    String path;

    @RequestMapping(value = "/scripts/{movie_id}", method = RequestMethod.GET)
    Map<String, Object> getScripts(
            @PathVariable int movie_id,
            @RequestParam(value = "limit", defaultValue = "16") int limit,
            @RequestParam(value = "offset", defaultValue = "1") int offset,
            @RequestParam(value = "word", required = false) String word) {
        Map<String, Object> response_json = new HashMap<>();

        int script_count = movieService.getScriptCount(movie_id, word);
        int last_page =(int)Math.ceil((double) script_count/ limit);

        offset = (offset < 0) ? 0 : (offset > last_page)? last_page - 1 : offset - 1;
        offset *= limit;
        List<Script> script_list = movieService.getScriptList( movie_id, word, limit, offset);

        response_json.put("cur_page", offset + 1);
        response_json.put("script_list", script_list);
        response_json.put("last_page", last_page);
        return response_json;
    }

    @GetMapping(value = "/script/thumbnail/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> thumbnailSearch(@PathVariable("id")int script_id) throws IOException{
        Script script = movieService.getScriptById(script_id);
        InputStream imageStream = new FileInputStream(path + script.getThumbnail());
        byte[] imageByteArray  = imageStream.readAllBytes();
        imageStream.close();
        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
    }
}
