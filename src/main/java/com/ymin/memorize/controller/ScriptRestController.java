package com.ymin.memorize.controller;

import com.ymin.memorize.dto.Movie;
import com.ymin.memorize.dto.Script;
import com.ymin.memorize.service.MovieService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
        last_page = (last_page == 0)? 1 : last_page;
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

    @GetMapping(value = "/script/stream/{id}")
    public void viewStream(@PathVariable("id")int script_id, HttpServletRequest request, HttpServletResponse response)throws UnsupportedEncodingException, IOException {
        Script script = movieService.getScriptById(script_id);
        Movie movie = movieService.getMovieByMovieId(script.getMovie_id());
        long range_start = 0;
        long range_end = 0;
        boolean is_part = false;

        //progressbar 에서 특정 위치를 클릭하거나 해서 임의 위치의 내용을 요청할 수 있으므로
        // 파일의 임의의 위치에서 읽어오기 위해 RandomAccessFile 클래스를 사용한다.
        // 해당 파일이 없을 경우 예외 발생
        File file = new File("D:/python-workspace/MovieScriptExtraction/Movie/Avengers Endgame/Avengers Endgame.webm");

        if( ! file.exists() )
            throw new FileNotFoundException();

        RandomAccessFile random_file = new RandomAccessFile(file, "r");
        //randomFile 을 클로즈 하기 위하여 try~finally 사용
        try {
            //동영상 파일 크기
            long movie_size = random_file.length();// 스트림 요청 범위, request의 헤더에서 range를 읽는다.
            //스트림 요청 범위, request의 헤더에서 range를 읽는다.
            String range = request.getHeader("range");
            //브라우저에 따라 range 형식이 다른데, 기본 형식은 "bytes={start}-{end}" 형식이다.
            //range가 null이거나, reqStart가  0이고 end가 없을 경우 전체 요청이다.
            //요청 범위를 구한다.
            if (range != null) {
                //처리의 편의를 위해 요청 range에 end 값이 없을 경우 넣어줌
                if (range.endsWith("-")) {
                    range = range + (movie_size - 1);
                }
                int idx = range.trim().indexOf("-");
                range_start = Long.parseLong(range.substring(6, idx));
                range_end = Long.parseLong(range.substring(idx + 1));
                if (range_start > 0)
                    is_part = true;
            } else {  //range가 null인 경우 동영상 전체 크기로 초기값을 넣어줌. 0부터 시작하므로 -1
                range_start = 0;
                range_end = movie_size - 1;
            }

            //전송 파일 크기
            long part_size = range_end - range_start + 1;
            response.reset();
            //전체 요청일 경우 200, 부분 요청일 경우 206을 반환상태 코드로 지정
            response.setStatus(is_part ? 206 : 200);
            //mime type 지정
            response.setContentType("video/webm");
            //전송 내용을 헤드에 넣어준다. 마지막에 파일 전체 크기를 넣는다.
            response.setHeader("Content-Range", "bytes " + range_start + "-" + range_end + "/" + range_end);
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", "" + part_size);
            OutputStream out = response.getOutputStream();
            //동영상 파일의 전송시작 위치 지정
            random_file.seek(range_start);
            //파일 전송...  java io는 1회 전송 byte수가 int로 지정됨
            //동영상 파일의 경우 int형으로는 처리 안되는 크기의 파일이 있으므로
            //8kb로 잘라서 파일의 크기가 크더라도 문제가 되지 않도록 구현
            int buffer_size = 8 * 1024;
            byte[] buf = new byte[buffer_size];
            do {
                int block = part_size > buffer_size ? buffer_size : (int) part_size;
                int len = random_file.read(buf, 0, block);
                out.write(buf, 0, len);
                part_size -= block;
            } while (part_size > 0);
        }catch (IOException e){
            //전송 중에 브라우저를 닫거나, 화면을 전환한 경우 종료해야 하므로 전송취소.
            // progressBar를 클릭한 경우에는 클릭한 위치값으로 재요청이 들어오므로 전송 취소.
        }
        finally {
            random_file.close();
        }
    }
}
