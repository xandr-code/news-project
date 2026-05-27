package com.example.news_project.service;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ContentCleaner {
    private static final List<String> noiseParser = new ArrayList<>();
    public ContentCleaner () {
        noiseParser.add("Реклама");
        noiseParser.add("Читайте также");
        noiseParser.add("Подпишитесь на");
    }
    public String clean(String rowText) {
        if (rowText == null && rowText.isEmpty()) {
            return "";
        } else {
            String text = Jsoup.parse(rowText).text();
            for (String pattern:noiseParser) {
                // rowText = text.replaceAll("(?ieu)")
            }

        }
    }
}
