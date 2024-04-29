package com.example.aigeneratevideo;

import lombok.Data;

@Data
public class Scene {
    private String narrator;
    private String prompt;
    private String imageFilePath;
    private String audioFilePath;
    private String videoFilePath;
}
