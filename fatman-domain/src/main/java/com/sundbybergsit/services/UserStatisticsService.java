package com.sundbybergsit.services;

import java.util.Date;

public interface UserStatisticsService {

    int totalFatDiff(String username);

    int thisWeekFatDiff(String userId);

    int fatDiff(String userId, Date from, Date to);
}
