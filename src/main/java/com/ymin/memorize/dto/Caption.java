package com.ymin.memorize.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Caption {
    private int id;
    private int movie_id;
    private int start_second;
    private int end_second;
    private String caption;
}
