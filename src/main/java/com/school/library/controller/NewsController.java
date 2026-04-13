package com.school.library.controller;

import com.school.library.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/news")
@CrossOrigin(origins = "http://localhost:3000")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping
    public List<Map<String, String>> getNews(@RequestParam(defaultValue = "nguon-goc") String category) {
        // Trả thẳng List về cho React
        return newsService.fetchNewsFromRSS(category);
    }
}