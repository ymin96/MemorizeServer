package com.ymin.memorize.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.movie.path}")
    private String path;

    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/movie/**")
                .addResourceLocations(path);
    }
}
