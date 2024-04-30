package com.example.aigeneratevideo.bean;

import lombok.Data;

@Data
public class Scene {
    private String narrator;
    private String prompt;
    private String imageFilePath;
    private String audioFilePath;
    private String videoFilePath;
}
