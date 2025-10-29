package com.example.healthassistantbackend.service;

import com.example.healthassistantbackend.entity.Content;
import com.example.healthassistantbackend.entity.User;
import com.example.healthassistantbackend.repository.ContentRepository;
import com.example.healthassistantbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.healthassistantbackend.dto.DeepseekDto;
import com.example.healthassistantbackend.dto.SerpApiDto;
import com.example.healthassistantbackend.entity.CalorieRecord;
import com.example.healthassistantbackend.entity.ExerciseRecord;
import com.example.healthassistantbackend.entity.SleepRecord;
import com.example.healthassistantbackend.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class ContentService {

    private static final Logger log = LoggerFactory.getLogger(ContentService.class);

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalorieRecordRepository calorieRecordRepository;

    @Autowired
    private ExerciseRecordRepository exerciseRecordRepository;

    @Autowired
    private SleepRecordRepository sleepRecordRepository;

    private final WebClient webClient;

    @Value("${serpapi.key}")
    private String serpApiKey;
    @Value("${deepseek.key}")
    private String deepseekApiKey;

    public ContentService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    // 获取所有内容
    public List<Content> getAllContent() {
        return contentRepository.findAll();
    }

    // 获取用户收藏的内容
    public Set<Content> getFavorites(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFavoriteContents();
    }

    // 添加收藏
    @Transactional
    public void addFavorite(Long contentId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Content content = contentRepository.findById(contentId).orElseThrow(() -> new RuntimeException("Content not found"));
        user.getFavoriteContents().add(content);
        userRepository.save(user); // 保存用户状态，JPA会自动更新中间表
    }

    // 移除收藏
    @Transactional
    public void removeFavorite(Long contentId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Content content = contentRepository.findById(contentId).orElseThrow(() -> new RuntimeException("Content not found"));
        user.getFavoriteContents().remove(content);
        userRepository.save(user);
    }

    public Optional<Content> discoverAndSaveNewContent(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        List<CalorieRecord> calorieRecords = calorieRecordRepository.findTop7ByUserOrderByRecordDateDesc(user);
        List<ExerciseRecord> exerciseRecords = exerciseRecordRepository.findTop7ByUserOrderByRecordDateDesc(user);
        List<SleepRecord> sleepRecords = sleepRecordRepository.findTop7ByUserOrderByRecordDateDesc(user);

        String prompt = buildPromptForSearchQuery(user, calorieRecords, exerciseRecords, sleepRecords);

        String searchQuery = getSearchQueryFromAi(prompt).block();
        if (searchQuery == null || searchQuery.isEmpty()) {
            return Optional.empty();
        }

        // 现在调用的是修正后的 searchGoogle 方法
        List<SerpApiDto.OrganicResult> searchResults = searchGoogle(searchQuery).block();
        if (searchResults == null || searchResults.isEmpty()) {
            return Optional.empty();
        }

        for (SerpApiDto.OrganicResult result : searchResults) {
            if (result.getLink() != null && !contentRepository.existsByContentUrl(result.getLink())) {
                Content newContent = new Content(
                        result.getTitle(),
                        result.getSnippet(),
                        "article",
                        "https://images.pexels.com/photos/1591447/pexels-photo-1591447.jpeg",
                        result.getLink(),
                        "网络发现," + searchQuery
                );
                Content savedContent = contentRepository.save(newContent);
                return Optional.of(savedContent);
            }
        }

        return Optional.empty();
    }

    // --- AI和搜索的辅助方法 ---

    private String buildPromptForSearchQuery(User user, List<CalorieRecord> c, List<ExerciseRecord> e, List<SleepRecord> s) {
        return "你是一个健康顾问。基于用户数据，分析其最需关注的健康问题，并返回一个最适合网络搜索的关键词，例如“如何改善睡眠质量”或“科学的减脂食谱”。用户信息：..." +
                " 近期数据摘要: 饮食记录数: " + c.size() + ", 运动记录数: " + e.size() + ", 睡眠记录数: " + s.size() +
                ". 请只返回一个最核心的搜索关键词，不要任何多余的解释。";
    }

    private Mono<String> getSearchQueryFromAi(String prompt) {
        DeepseekDto.Request request = new DeepseekDto.Request();
        request.setModel("deepseek-chat");
        request.setMessages(List.of(new DeepseekDto.Message("user", prompt)));

        return webClient.post()
                .uri("https://api.deepseek.com/chat/completions")
                .header("Authorization", "Bearer " + deepseekApiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(DeepseekDto.Response.class)
                .map(DeepseekDto.Response::getResponseText);
    }

    private Mono<List<SerpApiDto.OrganicResult>> searchGoogle(String query) {
        log.info("正在向 serpapi.com 发送 GET 请求, query: {}", query);

        return webClient.get() // <-- 修改1：使用 .get()
                .uri("https://serpapi.com/search", uriBuilder -> uriBuilder
                        .queryParam("q", query)
                        .queryParam("api_key", serpApiKey) // <-- 修改2：API Key 作为查询参数
                        .queryParam("engine", "google")   // <-- 修改3：根据文档添加必要参数
                        .queryParam("gl", "cn")           // 国家
                        .queryParam("hl", "zh-cn")        // 语言
                        .build())
                .retrieve()
                .bodyToMono(SerpApiDto.Response.class)
                .map(SerpApiDto.Response::getOrganicResults)
                .onErrorResume(e -> {
                    log.error("调用 serpapi.com 时出错", e);
                    return Mono.empty(); // 出错时返回空结果，防止整个流程崩溃
                });
    }
}