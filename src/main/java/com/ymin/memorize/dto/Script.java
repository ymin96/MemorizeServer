package com.ymin.memorize.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Script {
    private int id;
    private int movie_id;
    private int start_second;
    private int end_second;
    private String caption;
    private String thumbnail;
}
