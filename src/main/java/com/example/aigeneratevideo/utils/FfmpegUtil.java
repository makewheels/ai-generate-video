package com.example.aigeneratevideo.utils;

import java.io.File;

public class FfmpegUtil {
    private static final String FFMPEG_PATH = "C:/mysofts/ffmpeg.exe";

    public static String getMergeImageAndAudioFileCommand(
            File imageFile, File audioFile, File outputFile) {
        return String.format(FFMPEG_PATH
                        + " -loop 1 -i \"%s\" -i \"%s\" -c:v libx264 -tune stillimage" +
                        " -c:a aac -b:a 192k -pix_fmt yuv420p -shortest \"%s\"",
                imageFile.getAbsolutePath(), audioFile.getAbsolutePath(), outputFile.getAbsolutePath());
    }
}
