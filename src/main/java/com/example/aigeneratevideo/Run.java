package com.example.aigeneratevideo;

import com.alibaba.fastjson.JSONObject;

public class Run {
    public static void main(String[] args) {
        StoryService storyService = new StoryService();
        JSONObject storyObject = storyService.generateStory();
        System.out.println(storyObject);
    }
}
