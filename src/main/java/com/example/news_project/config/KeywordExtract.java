package com.example.news_project.config;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class KeywordExtract {

    private static final Set<String> STOP_WORDS = Set.of(
            "который", "которая", "которые", "потому", "после", "перед", "также", "более", "менее",
            "сегодня", "заявил", "сообщил", "однако", "около", "через", "чтобы", "были", "была",
            "will", "with", "that", "this", "from", "have", "about", "after", "before"
    );
    public List<String> extract(String text, int limit) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return Arrays.stream(text.toLowerCase(Locale.ROOT).split("[^a-zа-яё0-9]+"))
                .map(String::trim)
                .filter(word -> word.length() > 4)
                .filter(word -> !STOP_WORDS.contains(word))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()).thenComparing(Map.Entry.comparingByKey()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }
}
