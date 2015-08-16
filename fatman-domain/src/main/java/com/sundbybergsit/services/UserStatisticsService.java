package com.sundbybergsit.services;

public interface UserStatisticsService {

    int totalFatDiff(String username);

    int thisWeekFatDiff(String userId);

    int thisMonthFatDiff(String userId);
}
