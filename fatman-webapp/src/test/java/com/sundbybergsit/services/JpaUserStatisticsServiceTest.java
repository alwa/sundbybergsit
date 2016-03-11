package com.sundbybergsit.services;

import org.junit.gen5.api.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.gen5.api.Assertions.expectThrows;

class JpaUserStatisticsServiceTest {

    @Test
    public void calculateDiffInPercentages() throws Exception {
        JpaUserStatisticsService service = new JpaUserStatisticsService();
        int result = service.calculateDiffInPercentages(30.0F, 25.0F);
        assertThat(result, is(-16));
        result = service.calculateDiffInPercentages(43.3F, 59.9F);
        assertThat(result, is(38));
    }

    @Test
    public void fatDiffNotOkayWithNullUsername() throws Exception {
        JpaUserStatisticsService service = new JpaUserStatisticsService();
        Date from = new Date();
        Date to = new Date();

        expectThrows(IllegalArgumentException.class, () -> {
            service.fatDiff(null, from, to);
        });
    }

    @Test
    public void totalDiffNotOkayWithNullUsername() throws Exception {
        JpaUserStatisticsService service = new JpaUserStatisticsService();

        expectThrows(IllegalArgumentException.class, () -> {
            service.totalFatDiff(null);
        });
    }

    @Test
    public void notOkayToCalculateDiffWithZeroStartPercentage() throws Exception {
        JpaUserStatisticsService service = new JpaUserStatisticsService();

        expectThrows(IllegalArgumentException.class, () -> {
            service.calculateDiffInPercentages(0F, 25.0F);
        });
    }

    @Test
    public void notOkayToCalculateDiffWithZeroEndPercentage() throws Exception {
        JpaUserStatisticsService service = new JpaUserStatisticsService();

        expectThrows(IllegalArgumentException.class, () -> {
            service.calculateDiffInPercentages(30.0F, 0F);
        });
    }
}
