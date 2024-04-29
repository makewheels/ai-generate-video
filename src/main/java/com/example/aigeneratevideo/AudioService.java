package com.example.aigeneratevideo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.aigeneratevideo.utils.SecretKeyUtil;

import java.io.File;
import java.io.InputStream;

public class AudioService {
    public void textToAudio(String input, File audioFile) {
        JSONObject body = new JSONObject();
        body.put("model", "tts-1-hd");
        body.put("input", input);
        body.put("voice", "nova");
        body.put("response_format", "aac");

        InputStream inputStream = HttpUtil.createPost("https://api.open-proxy.cn/v1/audio/speech")
                .bearerAuth(SecretKeyUtil.getSecretKey())
                .body(body.toJSONString())
                .execute().bodyStream();
        IoUtil.copy(inputStream, FileUtil.getOutputStream(audioFile));
    }
}
