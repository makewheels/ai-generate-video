package com.example.aigeneratevideo;

import java.io.File;
import java.util.List;

public class Run {
    public static void main(String[] args) {
        File baseFolder = new File("D:\\2024年4月16日093520");
        StoryService storyService = new StoryService();
        Story story = storyService.generateStory();
        File folder = new File(baseFolder, story.getTitle());
        List<Scene> scenes = story.getScenes();
        for (Scene scene : scenes) {

        }
    }
}
