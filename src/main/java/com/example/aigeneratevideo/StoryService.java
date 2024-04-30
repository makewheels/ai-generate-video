package com.example.aigeneratevideo;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.aigeneratevideo.utils.SecretKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class StoryService {
    private String getExampleStory() {
        JSONObject story = new JSONObject();
        story.put("title", "冒险的启程");

        JSONArray scenes = new JSONArray();

        JSONObject scene1 = new JSONObject();
        scene1.put("narrator", "图书馆的一角，一个古老的地图卷轴轻轻展开，尘封的秘密苏醒。 --ar 9:16");
        scene1.put("prompt", "An ancient map scroll being unrolled in a dusty library corner.");
        scenes.add(scene1);

        JSONObject scene2 = new JSONObject();
        scene2.put("narrator", "小镇的出口处，一位年轻的探险者调整背包，准备踏上未知的旅途。");
        scene2.put("prompt", "A young adventurer adjusting his backpack at the town's exit, ready for a journey. --ar 9:16");
        scenes.add(scene2);

        story.put("scenes", scenes);
        return JSON.toJSONString(story, SerializerFeature.WriteSlashAsSpecial);
    }

    public static void main(String[] args) {
        StoryService storyService = new StoryService();
        System.out.println(storyService.getExampleStory());
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

        systemMessage.put("content", "帮我生成一个小故事，主题是关于Grand Theft Auto里的人物故事。" +
                "里面有7个场景。你需要返回故事的标题，标题不要带Windows路径不能显示的特殊字符。" +
                "你需要以json形式返回，并且返回标准纯净的json，不要掺杂其它东西，因为我需要通过程序读取json。" +
                "故事需要吸引人，新颖，引人入胜。你只需要模仿返回的格式，不需要模仿故事内容，故事你可以生成一个更好的。" +
                "\n在每个场景里，你需要给出旁白朗读文字和图片生成提示词。" +
                "\n旁白朗读文字，注意朗读文字不要很长。图片的提示词用于使用AI生成图片。" +
                "\n给你个例子\n" + getExampleStory());

        messages.add(systemMessage);
        body.put("messages", messages);

        return body;
    }

    public Story generateStory() {
        JSONObject body = getBody();
        log.info("GPT接口请求体：" + body.toJSONString());
        String response = HttpUtil.createPost("https://api.open-proxy.cn/v1/chat/completions")
                .bearerAuth(SecretKeyUtil.getSecretKey())
                .body(body.toJSONString())
                .execute().body();
        log.info("GPT接口返回：" + response);

        String storyJson = JSONObject.parseObject(response)
                .getJSONArray("choices").getJSONObject(0)
                .getJSONObject("message").getString("content");
        storyJson = StringUtils.removeStart(storyJson, "```json");
        storyJson = StringUtils.removeEnd(storyJson, "```");

        log.info("生成得到的故事：" + storyJson);
        return JSONObject.parseObject(storyJson, Story.class);
    }

}
