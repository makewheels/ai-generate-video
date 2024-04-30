package com.example.aigeneratevideo.bean;

import lombok.Data;

import java.util.List;

@Data
public class Story {
    private String title;
    private List<Scene> scenes;
    private String resultFilePath;
}
