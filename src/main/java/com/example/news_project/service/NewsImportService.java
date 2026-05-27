package com.example.news_project.service;

import com.example.news_project.config.KeywordExtract;
import com.example.news_project.config.NewsCategorizer;
import com.example.news_project.config.SummaryService;
import com.example.news_project.config.UrlNormalizer;
import com.example.news_project.entity.NewsArticle;
import com.example.news_project.entity.NewsSource;
import com.example.news_project.modelDto.ImportResult;
import com.example.news_project.modelDto.NewsCandidate;
import com.example.news_project.repository.NewsArticleRepository;
import com.example.news_project.repository.NewsSourceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class NewsImportService {
    public static final int maxFoundNews = 20;
    private final NewsSourceRepository sourceRepository;
    private final NewsArticleRepository articleRepository;
    private final ArticleParser parser;
    private final SummaryService summaryService;
    private final NewsCategorizer categorizer;
    private final KeywordExtract keywordExtract;
    private final UrlNormalizer urlNormalizer;

    public NewsImportService(NewsSourceRepository sourceRepository, NewsArticleRepository articleRepository, ArticleParser parser, SummaryService summaryService, NewsCategorizer categorizer, KeywordExtract extract, KeywordExtract keywordExtract, UrlNormalizer urlNormalizer) {
        this.sourceRepository = sourceRepository;
        this.articleRepository = articleRepository;
        this.parser = parser;
        this.summaryService = summaryService;
        this.categorizer = categorizer;
        this.keywordExtract = keywordExtract;
        this.urlNormalizer = urlNormalizer;
    }
    public List<ImportResult> refreshAll() {
        List<ImportResult> results = new ArrayList<>();
        for (NewsSource source : sourceRepository.findAllByEnabledTrueOrderByNameAsc()) {
            results.add(refreshSource(source));
            System.out.println(source);
        }
        return results;
    }
    public ImportResult refreshSource(NewsSource source) {
        try {
            List<NewsCandidate> candidates = parser.fetch(source)
                    .stream()
                    .limit(maxFoundNews)
                    .toList();
            int added = 0;
            int duplicates = 0;
            for (NewsCandidate candidate : candidates) {
                String canonicalUrl = urlNormalizer.normalize(candidate.getUrl());
                if (isDuplicate(candidate.getTitle(), canonicalUrl, source)) {
                    duplicates++;
                    continue;
                }
               // String cleanedContent = contentCleaner.clean(firstNotBlank(candidate.content(), candidate.description()));
                //String cleanedDescription = contentCleaner.clean(candidate.description());
                NewsArticle article = new NewsArticle(candidate.getTitle(), canonicalUrl, source);
                article.setMediaUrl(candidate.getMediaUrl());
                //article.setContent(cleanedContent);
                article.setSummary(summaryService.summarize(firstNotBlank(candidate.getDescription(), candidate.getContent())));
                article.setCategory(categorizer.detect(candidate.getTitle(), candidate.getContent()));
                article.setKeywords(String.join(", ", keywordExtract.extract(candidate.getTitle() + " " + candidate.getContent(), 8)));
                article.setPublishedAt(candidate.getPublishedAt());
                article.setPopularity(article.getKeywords().isBlank() ? 0 : article.getKeywords().split(",").length);
                articleRepository.save(article);
                added++;
            }
            return new ImportResult(source.getName(), candidates.size(), added, duplicates, "OK");
        } catch (Exception exception) {
            return new ImportResult(source.getName(), 0, 0, 0, exception.getMessage());
        }
    }
    private boolean isDuplicate(String title, String canonicalUrl, NewsSource source) {
        return articleRepository.existsByCanonicalUrl(canonicalUrl)
                || articleRepository.existsByTitleIgnoreCaseAndSource_NameIgnoreCase(title, source.getName());
    }

    private String firstNotBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }
}
