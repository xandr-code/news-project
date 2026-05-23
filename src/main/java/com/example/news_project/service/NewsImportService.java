package com.example.news_project.service;

import com.example.news_project.config.KeywordExtract;
import com.example.news_project.config.NewsCategorizer;
import com.example.news_project.config.SummaryService;
import com.example.news_project.config.UrlNormalizer;
import com.example.news_project.entity.NewsSource;
import com.example.news_project.modelDto.ImportResult;
import com.example.news_project.repository.NewsArticleRepository;
import com.example.news_project.repository.NewsSourceRepository;

import java.util.ArrayList;
import java.util.List;

public class NewsImportService {
    public static final int maxFoundNews = 20;
    private final NewsSourceRepository sourceRepository;
    private final NewsArticleRepository articleRepository;
    private final ArticleParser parser;
    private final SummaryService summaryService;
    private final NewsCategorizer categorizer;
    private final KeywordExtract extract;
    private final UrlNormalizer urlNormalizer;

    public NewsImportService(NewsSourceRepository sourceRepository, NewsArticleRepository articleRepository, ArticleParser parser, SummaryService summaryService, NewsCategorizer categorizer, KeywordExtract extract, UrlNormalizer urlNormalizer) {
        this.sourceRepository = sourceRepository;
        this.articleRepository = articleRepository;
        this.parser = parser;
        this.summaryService = summaryService;
        this.categorizer = categorizer;
        this.extract = extract;
        this.urlNormalizer = urlNormalizer;
    }
    public void refreshAll() {
        List<ImportResult> results = new ArrayList<>();
        for (NewsSource source : sourceRepository.findAllByEnabledTrueOrderByNameAsc()) {

            results.add();

        }
    }
    public ImportResult refreshSource(NewsSource source) {
        try {

        }
    }
}
