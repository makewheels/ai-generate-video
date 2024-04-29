package com.example.aigeneratevideo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.aigeneratevideo.utils.SecretKeyUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;

@Slf4j
public class AudioService {
    public void textToAudio(String input, File audioFile) {
        log.info("文字转音频tts，输入 = " + input);
        JSONObject body = new JSONObject();
        body.put("model", "tts-1-hd");
        body.put("input", input);
        body.put("voice", "nova");
        body.put("response_format", "aac");

        HttpResponse httpResponse = null;
        int retryCount = 3;
        int status = -1;

        while (retryCount > 0) {
            httpResponse = HttpUtil.createPost("https://api.open-proxy.cn/v1/audio/speech")
                    .bearerAuth(SecretKeyUtil.getSecretKey())
                    .body(body.toJSONString())
                    .execute();
            status = httpResponse.getStatus();
            if (status == 200) {
                break;
            } else {
                retryCount--;
            }
        }

        if (status == 200) {
            InputStream inputStream = httpResponse.bodyStream();
            IoUtil.copy(inputStream, FileUtil.getOutputStream(audioFile));
        } else {
            log.error("请求失败，重试次数已用完，状态码 = " + status);
        }
    }
}
