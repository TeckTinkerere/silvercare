package com.silvercare.controller;

import com.silvercare.model.Season;
import com.silvercare.service.SeasonService;
import com.silvercare.util.ApiResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/season")
public class SeasonController {

    @Autowired
    private SeasonService seasonService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCurrentSeason() {
        Season season = seasonService.getCurrentSeason();
        return ApiResponseBuilder.success(season);
    }
}
