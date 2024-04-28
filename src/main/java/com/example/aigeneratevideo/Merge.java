package com.example.aigeneratevideo;

import cn.hutool.core.io.FileUtil;

import java.io.File;

public class Merge {
    public void merge(File folder) {
        int size = FileUtil.loopFiles(folder).size() / 2;

    }
}
