package com.example.aigeneratevideo;

import com.example.aigeneratevideo.utils.FfmpegUtil;
import com.example.aigeneratevideo.utils.StoryUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Run {
    StoryService storyService = new StoryService();
    ImageService imageService = new ImageService();
    AudioService audioService = new AudioService();

    private void handleImage(Story story, File imageFolder) {
        List<Scene> scenes = story.getScenes();
        for (int i = 0; i < scenes.size(); i++) {
            Scene scene = scenes.get(i);
            File imageFile = new File(imageFolder, i + ".png");
            scene.setImageFilePath(imageFile.getAbsolutePath());
            imageService.promptToImageFile(scene.getPrompt(), imageFile);
        }
    }

    private void handleAudio(Story story, File audioFolder) {
        List<Scene> scenes = story.getScenes();
        for (int i = 0; i < scenes.size(); i++) {
            Scene scene = scenes.get(i);
            File audioFile = new File(audioFolder, i + ".aac");
            scene.setAudioFilePath(audioFile.getAbsolutePath());
            audioService.textToAudio(scene.getNarrator(), audioFile);
        }
    }

    private void mergeImageAndAudio(File configFile, File storyFolder) {
        Story story = StoryUtil.load(configFile);
        // 生成小节视频
        File sectionVideoFolder = new File(storyFolder, "section-video");
        List<Scene> scenes = story.getScenes();
        for (int i = 0; i < scenes.size(); i++) {
            Scene scene = scenes.get(i);
            File sectionVideo = new File(sectionVideoFolder, i + ".mp4");
            scene.setVideoFilePath(sectionVideo.getAbsolutePath());
            FfmpegUtil.mergeImageAndAudioFile(
                    new File(scene.getImageFilePath()),
                    new File(scene.getAudioFilePath()),
                    sectionVideo);
        }

        // 把小节视频合并
        List<File> sectionVideoFiles = new ArrayList<>();
        for (Scene scene : scenes) {
            sectionVideoFiles.add(new File(scene.getVideoFilePath()));
        }
        File resultFile = new File(storyFolder, story.getTitle() + ".mp4");
        FfmpegUtil.mergeVideos(sectionVideoFiles, resultFile);
    }

    private void run() {
//        File baseFolder = new File("D:\\2024年4月16日093520\\stories");
//        Story story = storyService.generateStory();
//        File storyFolder = new File(baseFolder, story.getTitle());
//        File imageFolder = new File(storyFolder, "image");
//        File audioFolder = new File(storyFolder, "audio");
//
//        handleImage(story, imageFolder);
//        handleAudio(story, audioFolder);
//
//        File configFile = new File(storyFolder, "config.json");
//        StoryUtil.save(story, configFile);

        File configFile = new File("D:\\2024年4月16日093520\\stories\\遗失的光芒\\config.json");
        File storyFolder = new File("D:\\2024年4月16日093520\\stories\\遗失的光芒");
        mergeImageAndAudio(configFile, storyFolder);
//        StoryUtil.save(story, configFile);
    }


    public static void main(String[] args) {
        new Run().run();
    }

}
