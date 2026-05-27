package com.example.news_project.console;

import com.example.news_project.entity.NewsArticle;
import com.example.news_project.service.*;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;
@Service
public class ConsoleInterface implements ApplicationRunner {
    private final Scanner scanner = new Scanner(System.in);
    AnalyticService analyticService;
    ArticleSearchService articleSearchService;
    SourceService sourceService;
    ExportService exportService;
    ConfigurableApplicationContext context;
    NewsImportService importService;

    public ConsoleInterface(AnalyticService analyticService, ArticleSearchService articleSearchService, SourceService sourceService, ExportService exportService, ConfigurableApplicationContext context, NewsImportService importService) {
        this.analyticService = analyticService;
        this.articleSearchService = articleSearchService;
        this.sourceService = sourceService;
        this.exportService = exportService;
        this.context = context;
        this.importService = importService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Boolean runner = true;
        while (runner == true) {
            printInfo();
            String command = scanner.nextLine();
            try {
                handle(command);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void printInfo() {
        System.out.println("Агрегатор новостей");
        System.out.println("Последние новости - 1");
        System.out.println("Поиск по ключевому слову - 2");
        System.out.println("Обновить новости - 3");
        System.out.println("Популярные темы - 4");
        System.out.println("Статистика по категориям - 5");
    }

    public void handle(String command) {
        if (command.equals("1")) {
            System.out.println(articleSearchService.latest());
            printArticles(articleSearchService.latest());
        } else if (command.equals("2")) {
            System.out.println("Вывод обновленных пользователей");
            importService.refreshAll();
            System.out.println();
        }
    }
    private void printArticles(List<NewsArticle> articles) {
        if (articles.isEmpty()) {
            System.out.println("Новостей пока нет");
            return;
        }
        for (NewsArticle article : articles) {
            System.out.println(article.getId() + article.getTitle() + article.getSummary());
        }
    }
}
