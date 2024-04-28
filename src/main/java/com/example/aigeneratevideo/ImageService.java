package com.example.aigeneratevideo;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.aigeneratevideo.utils.SecretKeyUtil;

public class ImageService {
    public String submit(String prompt) {
        JSONObject body = new JSONObject();
        body.put("prompt", prompt);
        String response = HttpUtil.createPost("https://api.open-proxy.cn/mj/submit/imagine")
                .bearerAuth(SecretKeyUtil.getSecretKey())
                .body(body.toJSONString())
                .execute().body();
        return JSONObject.parseObject(response).getString("result");
    }

    public boolean queryTask(String taskId) {
        String url = "https://api.open-proxy.cn/mj/task/" + taskId + "/fetch";
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
}
