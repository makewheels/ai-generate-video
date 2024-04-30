package com.example.aigeneratevideo.utils;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.example.aigeneratevideo.bean.Story;

import java.io.File;

public class StoryUtil {
    public static void save(Story story, File configFile) {
        FileUtil.writeUtf8String(JSON.toJSONString(story, true), configFile);
    }

    public static Story load(File configFile) {
        return JSON.parseObject(FileUtil.readUtf8String(configFile), Story.class);
    }
}
