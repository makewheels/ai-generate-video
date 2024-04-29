package com.example.aigeneratevideo;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.aigeneratevideo.utils.SecretKeyUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class ImageService {
    public String submit(String prompt) {
        log.info("生成图片，提交提示词，" + prompt);
        JSONObject body = new JSONObject();
        body.put("prompt", prompt);
        String response = HttpUtil.createPost("https://api.open-proxy.cn/mj/submit/imagine")
                .bearerAuth(SecretKeyUtil.getSecretKey())
                .body(body.toJSONString())
                .execute().body();
        return JSONObject.parseObject(response).getString("result");
    }

    public boolean isTaskSuccess(String taskId) {
        String url = "https://api.open-proxy.cn/mj/task/" + taskId + "/fetch";
        log.info("查询任务状态, taskId = " + taskId);
        String response = HttpUtil.createGet(url)
                .bearerAuth(SecretKeyUtil.getSecretKey())
                .execute().body();
        return JSONObject.parseObject(response).getString("status").equals("SUCCESS");
    }

    public String getImageUrl(String taskId) {
        String url = "https://api.open-proxy.cn/mj/task/" + taskId + "/fetch";
        String response = HttpUtil.createGet(url)
                .bearerAuth(SecretKeyUtil.getSecretKey())
                .execute().body();
        return JSONObject.parseObject(response).getString("imageUrl");
    }

    public String upscale(String taskId) {
        JSONObject body = new JSONObject();
        body.put("action", "UPSCALE");
        body.put("index", 1);
        body.put("taskId", taskId);
        String response = HttpUtil.createPost("https://api.open-proxy.cn/mj/submit/change")
                .bearerAuth(SecretKeyUtil.getSecretKey())
                .body(body.toJSONString())
                .execute().body();
        return JSONObject.parseObject(response).getString("result");
    }

    public void promptToImageFile(String prompt, File imageFile) {
        String imageTaskId = submit(prompt);
        while (!isTaskSuccess(imageTaskId)) {
            ThreadUtil.sleep(3000);
        }

        String upscaleTaskId = upscale(imageTaskId);
        while (!isTaskSuccess(upscaleTaskId)) {
            ThreadUtil.sleep(3000);
        }

        String imageUrl = getImageUrl(upscaleTaskId);
        HttpUtil.downloadFile(imageUrl, imageFile);
    }
}
