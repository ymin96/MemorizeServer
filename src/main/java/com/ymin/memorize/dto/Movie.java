package com.ymin.memorize.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Movie {

    private int id;
    private String k_title;
    private String e_title;
    private List<Script> scriptList;

    public Movie(){
        scriptList = new ArrayList<>();
    }

    public boolean equals(Object o){
        return ( id == ((Movie)o).getId());
    }
}
