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
                "故事需要吸引人，新颖，引人入胜。你只需要模仿返回的格式，不需要模仿故事内容，故事你可以生成一个更好的。"
                + "\n\n\n我再来教你怎么生成提示词，你要选一个画风，在每个图里都用相同的画风" +
                "Midjourney是一种生成式AI工具，能够根据用户输入的提示词生成图片，类似于DALL-E。\n" +
                "提示词的主体部分由单词或短语组成，用来描述要生成的图片的主题。" +
                "包含很多形容词和名称的描述性更强的提示词会生成独特的图片，" +
                "相反，只包含基本的名词或形容词简短的提示词会生成平淡无奇的图片。" +
                "然而，由于Midjourney不理解语法和句子结构，所以超长的提示词未必会达到更好的效果。" +
                "在写提示词时，尽可能删掉不必要的词，因为更少的词意味着每个词的权重更高，" +
                "生成的图片的内容会更加符合主题。\n" +
                "比如“Illustrate for me a beautiful sunset over a serene ocean, " +
                "make the colors warm and soothing, and render it in an impressionistic" +
                " style with oil paints.”这样的提示词会包含很多Midjourney无法理解或无效的单词或词组。" +
                "比如“Illustrate for me”这个短语就是多余的，因为Midjourney本身的功能就是如此。" +
                "还有“make”，“render”这样的动词也是不必要的。Midjourney通常接受的是名称和形容词这样描述性的词。" +
                "因此，上面那段冗长的提示词可以简化为以下形式“warm soothing sunset over serene" +
                " ocean impressionistic oil paint”。\n" +
                "另外，在很多情况下，更具体的定位更强的词比那些通用的含义广泛的词效果更好。" +
                "比如，与其使用“small”这样比较泛的词，使用\"petite\", \"compact\", \"diminutive\", " +
                "\"tiny\"这样更精确的词会达到更好的效果。在写提示词时，要明确对你来说很关键的细节：\n" +
                "主题：人物、动物、地点、物体、事件等。\n" +
                "用例：logo、网页设计、室内设计、原型设计、产品设计等。\n" +
                "媒介：照片、绘画、插图、油画、点彩画、漫画、水彩画、素描、手稿画、拼贴画、版画、" +
                "雕塑、涂鸦、马赛克、挂毯、陶器等。\n" +
                "环境：室内、室外、城市、地铁站、火车站、工厂、城堡、酒庄、寺庙、教堂、森林" +
                "、草原、热带雨林、小岛、葡萄园、极地、沙漠、火山口、水下、洞穴、未来城、太空、" +
                "月球、空间站等。\n" +
                "灯光：柔和的、环境光、黄昏(Golden hour)、直射光、阴天、月光、霓虹灯、影视灯、" +
                "聚光灯、伦勃朗光、烛光、火光等。\n" +
                "颜色：鲜艳的、柔和的、明亮的、单色的、多彩的、黑白的、粉色的等等。\n" +
                "情绪：激情的、沉静的、喧闹的、不安的、愉悦的、忧郁的、热烈的、梦幻的、神秘的等等。\n" +
                "构图：肖像、三分法构图、头像、特写、鸟瞰、对称构图、引导线构图、对角线构图、" +
                "极简、剪影、全景等。\n" +
                "艺术家：梵高、毕加索、达利、保罗·塞尚、保罗·高更、达芬奇、波提切利、伦勃朗、" +
                "宫崎骏、张大千等。\n" +
                "画风：东方山水画、浮世绘、概念艺术、包豪斯、印象派、洛可可、野兽派、超现实主义、" +
                "长时间曝光等。\n" +
                "如果遇到需要分离的主题，还可以使用逗号，加号或者“and”。比如你想要创建灯(light)" +
                "和房子(house)的图像，如果不用逗号，加号或者“and”来分离，" +
                "那么当你把light house（灯塔）提交给Midjourney，那么它会生成一副灯塔的图像。\n" +
                "现在你已了解提示词该如何下，下面是20个提示词的例子，看了之后你会彻底掌握写" +
                "Midjourney提示词的技巧：\n" +
                "国家地理摄影风格的粉红色花朵中的白色奶牛\n" +
                "她摆出黑色和金色的姿势，具有醒目的树脂珠宝风格，城市衰败，边缘光，标志性，" +
                "异国情调，大胆，卡通式线条，精心设计\n" +
                "阿泰-盖兰和约翰-辛格-萨金特的《falkor》，无尽的故事\n" +
                "电影般的中世纪室内摄影，阿科斯-马杰的风格，美术。\n" +
                "抽象的未来主义的歇斯底里的日本时尚热潮，充满活力的抽象超现实摄影，" +
                "由埃德-埃姆施维勒和石罗正宗和比普尔拍摄\n" +
                "一个有中岛和木质地板的大房间，风格为深铜色和深米色，深灰色和深黑色，" +
                "乡村场景，avocadopunk，小屋核心，多种风格，深白色和灰色\n" +
                "一个女孩有五颜六色的眼妆和一个星形的发型，酸波风格，深棕色和深天蓝色，" +
                "苏米埃的灵感，超级英雄，babycore，Bec Winnel\n" +
                "荆棘骑士的女人在聚光灯下，渐变，矢量，光纤，北斋，旗帜传奇，光泽，Cel shaded\n" +
                "传统的斯拉夫民俗怪物，巴巴-雅加，乌尔杜拉克，阿尔贝托-塞维索风格的Upyr\n" +
                "一个穿着黑色皮草的少女涂着紫色的唇膏，单色方案的风格，深棕色和深黑色，城市腐烂，" +
                "大胆而优雅，艾米拉-梅德科娃，引人注目，双色调效果\n" +
                "1. 一幅受赛博朋克启发的全长插图，展示了一件未来主义的紧身衣。" +
                "这套衣服紧紧地包裹着穿着者的身体，散发着一种时尚和功能性的气息。" +
                "衣服的面料中嵌入了许多触觉传感器，它们被精心整合，以提供增强的触觉反馈和数据分析" +
                "。全息图层被投射到衣服的表面，产生变化的图案和颜色，使观众着迷。" +
                "在黑色背景的衬托下，这套衣服脱颖而出，唤起了一种神秘感和未来主义的优雅。" +
                "插图，数字艺术，将这一愿景带入生活，对细节的关注一丝不苟。"
                + "\n在每个场景里，你需要给出旁白朗读文字和图片生成提示词，提示词需要是English" +
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
