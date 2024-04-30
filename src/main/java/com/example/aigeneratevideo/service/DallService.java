package com.example.aigeneratevideo.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.aigeneratevideo.utils.SecretKeyUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class DallService {
    public void promptToImageFile(String prompt, File imageFile) {
        JSONObject body = new JSONObject();
        body.put("model", "dall-e-3");
        body.put("prompt", prompt);
        body.put("size", "1024x1792");

        log.info("GPT-DALL接口请求体：" + body.toJSONString());
        String response = HttpUtil.createPost("https://api.open-proxy.cn/v1/images/generations")
                .bearerAuth(SecretKeyUtil.getSecretKey())
                .body(body.toJSONString())
                .execute().body();
        log.info("GPT-DALL接口返回：" + response);

        String imageUrl = JSONObject.parseObject(response).getJSONObject("data").getString("url");
        HttpUtil.downloadFile(imageUrl, imageFile);
    }
}
