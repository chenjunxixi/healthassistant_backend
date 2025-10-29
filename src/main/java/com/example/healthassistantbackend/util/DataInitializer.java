package com.example.healthassistantbackend.util;

import com.example.healthassistantbackend.entity.Content;
import com.example.healthassistantbackend.repository.ContentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ContentRepository contentRepository;

    // 使用构造函数注入Repository，更推荐
    public DataInitializer(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 检查数据库中是否已有内容，如果没有，才执行插入操作
        if (contentRepository.count() == 0) {
            System.out.println("数据库内容为空，正在插入样板文章...");

            Content tip1 = new Content("科学减脂：如何计算你的热量缺口？", "了解基础代谢和每日总消耗，是制定有效减脂计划的第一步。", "article", "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260", "https://www.zhihu.com/question/40496904", "减脂,饮食,科普");
            Content tip2 = new Content("15分钟HIIT燃脂训练", "高强度间歇训练（HIIT）是最高效的燃脂运动之一，适合没有太多时间运动的你。", "video", "https://images.pexels.com/photos/3768916/pexels-photo-3768916.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260", "https://www.bilibili.com/video/BV15L411H7qK", "减脂,运动,HIIT");
            Content tip3 = new Content("改善睡眠质量的7个小习惯", "睡得好，才能瘦得快。了解如何通过改变小习惯来提升你的睡眠质量。", "article", "https://images.pexels.com/photos/3771045/pexels-photo-3771045.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260", "https://www.zhihu.com/question/28025333", "助眠,健康习惯");
            Content tip4 = new Content("新手健身房力量训练计划", "不知道进健身房该练什么？这份详细的计划表带你入门。", "article", "https://images.pexels.com/photos/1954524/pexels-photo-1954524.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260", "https://www.zhihu.com/question/35485453", "增肌,运动,力量训练");
            Content tip5 = new Content("推广全民运动，健康是财富之本", "文章强调运动对提升个人健康、工作效率乃至国家竞争力的重要性，倡导将健走等简单运动融入日常生活，塑造全民运动文化。", "article", "https://images.pexels.com/photos/3757942/pexels-photo-3757942.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2", "https://www.mohw.gov.tw/cp-3204-21761-1.html", "全民运动,健康,健走,公共卫生,生活方式");
            Content tip6 = new Content("运动：定期身体活动的7 大好处", "本文详细介绍了定期进行身体活动的七大益处，包括控制体重、对抗疾病、改善情绪、增强精力、改善睡眠等。", "article", "https://images.pexels.com/photos/2294361/pexels-photo-2294361.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2", "https://www.mayoclinic.org/zh-hans/healthy-lifestyle/fitness/in-depth/exercise/art-20048389", "运动好处,身体活动,健康管理,疾病预防,精力提升");
            Content tip7 = new Content("专家给你一份精准健康饮食指南", "该指南提供了精准的日常饮食建议，强调食物多样化、三减（减油、减盐、减糖）、适量摄入坚果和限制饮酒，以促进健康。", "article", "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2", "https://www.gov.cn/fuwu/2018-01/30/content_5262302.htm", "健康饮食,饮食指南,营养,三减,平衡膳食");
            Content tip8 = new Content("怎么运动更健康？10招教你科学健身", "本文提供了10个科学健身的实用技巧，包括运动规划、热身、补水和合理饮食，帮助你更健康、更有效地进行锻炼。", "article", "https://images.pexels.com/photos/416809/pexels-photo-416809.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2", "https://cn.chinadaily.com.cn/a/202408/15/WS66bd4a6ca310054d254ece7d.html", "科学健身,运动,健康,健身技巧,运动损伤");
            Content tip9 = new Content("心理健康素养核心信息10条", "文章阐述了心理健康作为整体健康重要组成部分的核心信息，强调身心健康的相互影响，并指出适量运动对改善情绪有益。", "article", "https://images.pexels.com/photos/3822623/pexels-photo-3822623.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2", "https://www.cqstl.gov.cn/bm/qwsjkw_71116/zwgk_70831/jkkp/202112/t20211231_10265168.html", "心理健康,身心健康,情绪管理,焦虑,运动");

            // 使用 saveAll 批量保存
            contentRepository.saveAll(Arrays.asList(tip1, tip2, tip3, tip4, tip5, tip6, tip7, tip8, tip9));

            System.out.println("样板文章插入完成。共 " + contentRepository.count() + " 条。");
        } else {
            System.out.println("数据库中已存在内容，无需初始化。");
        }
    }
}