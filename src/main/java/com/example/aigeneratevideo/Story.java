package com.example.aigeneratevideo;

import lombok.Data;

import java.util.List;

@Data
public class Story {
    private String title;
    private List<Scene> scenes;
}
