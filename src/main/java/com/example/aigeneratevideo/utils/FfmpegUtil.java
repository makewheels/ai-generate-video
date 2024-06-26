package com.example.aigeneratevideo.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
public class FfmpegUtil {
    private static final String FFMPEG_PATH = "C:/mysofts/ffmpeg.exe";

    public static void runCmd(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 合并图片和音频
     */
    public static void mergeImageAndAudioFile(
            File imageFile, File audioFile, File outputFile) {
        String cmd = String.format(FFMPEG_PATH
                        + " -loop 1 -i \"%s\" -i \"%s\" -c:v libx264 -tune stillimage" +
                        " -c:a aac -b:a 192k -pix_fmt yuv420p -shortest \"%s\"",
                imageFile.getAbsolutePath(), audioFile.getAbsolutePath(), outputFile.getAbsolutePath());
        log.info("合并图片和音频: " + cmd);
        runCmd(cmd);
        ThreadUtil.sleep(5000);
    }

    /**
     * 合并多个视频
     */
    public static void mergeVideos(List<File> sourceVideoFiles, File outputFile) {
        StringBuilder stringBuilder = new StringBuilder();
        for (File sourceVideoFile : sourceVideoFiles) {
            stringBuilder.append("file '")
                    .append(sourceVideoFile.getAbsolutePath())
                    .append("'")
                    .append("\n");
        }
        File inventoryFile = new File(sourceVideoFiles.get(0).getParentFile(), "inventory.txt");
        log.info("生成合并视频的inventory文件: " + inventoryFile.getAbsolutePath());
        FileUtil.writeUtf8String(stringBuilder.toString(), inventoryFile);
        String cmd = String.format(FFMPEG_PATH
                        + " -f concat -safe 0 -i \"%s\" -c copy \"%s\"",
                inventoryFile.getAbsolutePath(), outputFile.getAbsolutePath());
        log.info("合并视频: " + cmd);
        runCmd(cmd);
        ThreadUtil.sleep(12000);
    }
}
