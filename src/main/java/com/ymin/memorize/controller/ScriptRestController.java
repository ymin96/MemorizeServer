package com.ymin.memorize.controller;

import com.ymin.memorize.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScriptRestController {

    @Autowired
    MovieService movieService;



}
