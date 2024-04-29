package com.example.aigeneratevideo;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.aigeneratevideo.utils.SecretKeyUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StoryService {
    private String getExampleStory() {
        return "{\"title\":\"冒险的启程\",\"scenes\":[{\"narrator\":\"图书馆的一角，" +
                "一个古老的地图卷轴轻轻展开，尘封的秘密苏醒。\",\"prompt\":\"" +
                "An ancient map scroll being unrolled in a dusty library corner.\"}," +
                "{\"narrator\":\"小镇的出口处，一位年轻的探险者调整背包，准备踏上未知的旅途。\"," +
                "\"prompt\":\"A young adventurer adjusting his backpack at the town's exit," +
                " ready for a journey.\"}]}";
    }

    private JSONObject getBody() {
        JSONObject body = new JSONObject();
        body.put("model", "gpt-4-1106-preview");

        JSONObject response_format = new JSONObject();
        response_format.put("type", "json_object");
        body.put("response_format", response_format);

        JSONArray messages = new JSONArray();
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");

        systemMessage.put("content", "帮我生成一个小故事，里面有6个场景，以json形式返回，你需要返回故事的标题。" +
                "故事需要吸引人，引人入胜。你只需要模仿返回的格式，不需要模仿故事内容，故事你可以生成一个更好的。" +
                "\n在每个场景里，你需要给出旁白朗读文字和图片生成提示词。" +
                "\n旁白朗读文字，注意朗读文字不要很长。图片的提示词用于使用AI生成图片。" +
                "\n给你个例子\n" + getExampleStory());

        messages.add(systemMessage);
        body.put("messages", messages);

        return body;
    }

    public Story generateStory() {
        log.info("生成故事，开始调用GPT接口");
        String response = HttpUtil.createPost("https://api.open-proxy.cn/v1/chat/completions")
                .bearerAuth(SecretKeyUtil.getSecretKey())
                .body(getBody().toJSONString())
                .execute().body();
        String story = JSONObject.parseObject(response)
                .getJSONArray("choices").getJSONObject(0)
                .getJSONObject("message").getString("content");
        log.info("生成得到的故事：" + story);
        return JSONObject.parseObject(story, Story.class);
    }

}
