package com.example.aigeneratevideo;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.aigeneratevideo.bean.Story;
import com.example.aigeneratevideo.utils.SecretKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class StoryService {
    private String getExampleStory() {
        JSONObject story = new JSONObject();
        story.put("title", "奥巴马总统的奇幻梦境");

        JSONArray scenes = new JSONArray();

        JSONObject scene1 = new JSONObject();
        scene1.put("narrator", "奥巴马总统在白宫里经历了一场奇怪的梦境，变成了一只巨大的袋鼠。");
        scene1.put("prompt", "Giant kangaroo Obama experiences a strange dream in the White House, " +
                "surreal and fantastical. Anime style --ar 9:16");
        scenes.add(scene1);

        JSONObject scene2 = new JSONObject();
        scene2.put("narrator", "他必须完成三个任务才能解脱。他找到了隐藏在纪念碑和白宫地下室的宝物。");
        scene2.put("prompt", "Obama, the Kangaroo, must complete three tasks to be free. " +
                "He discovers treasures hidden in monuments and the White House basement." +
                " Anime style --ar 9:16");

        scenes.add(scene2);
        JSONObject scene3 = new JSONObject();
        scene3.put("narrator", "最后，他被告知这是一场考验。梦境消失后，他重新醒来，但他对人生有了新的认识。");
        scene3.put("prompt", "As the dream vanishes, he's informed it's a trial. Upon waking, " +
                "he gains a fresh perspective on life. Anime style --ar 9:16");
        scenes.add(scene3);

        story.put("scenes", scenes);
        return JSON.toJSONString(story, SerializerFeature.WriteSlashAsSpecial);
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

        systemMessage.put("content", "帮我生成一个小故事，主题是关于Barack Obama的故事。"
                + "里面有7个场景。你需要返回故事的标题，标题不要带Windows路径不能显示的特殊字符。"
                + "你需要以json形式返回，并且返回标准纯净的json，不要掺杂其它东西，因为我需要通过程序读取json。"
                + "故事需要吸引人，新颖，引人入胜，跌宕起伏。你不需要基于主题老的故事，你可以创造新的故事。"
                + "你只需要模仿返回的格式，不需要模仿故事内容，故事你可以生成一个更好的。"
                + "你要选一个画风，在每个图里都用相同的画风，你把画风放在每个提示词里就可以了。"
                + "我想要竖屏的图片，你把这个参数跟在每个提示词后面就可以了--ar 9:16。"
                + "\n在每个场景里，你需要给出旁白朗读文字和图片生成提示词。"
                + "旁白是中文，旁白朗读文字，注意朗读文字不要很长。"
                + "提示词需要是English，图片的提示词用于使用AI生成图片，可以多写一点。"
                + "\n给你个例子\n" + getExampleStory());

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
