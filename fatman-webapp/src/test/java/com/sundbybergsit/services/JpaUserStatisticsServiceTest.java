package com.sundbybergsit.services;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JpaUserStatisticsServiceTest {

    @Test
    public void calculateDiffInPercentages() throws Exception {
        JpaUserStatisticsService service = new JpaUserStatisticsService();
        int result = service.calculateDiffInPercentages(30.0F, 25.0F);
        assertThat(result, is(-16));
        result = service.calculateDiffInPercentages(43.3F, 59.9F);
        assertThat(result, is(38));
    }

    @Test(expected = IllegalArgumentException.class)
    public void totalDiffNotOkayWithNullUsername() throws Exception {
        JpaUserStatisticsService service = new JpaUserStatisticsService();
        service.totalFatDiff(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notOkayToCalculateDiffWithZeroStartPercentage() throws Exception {
        JpaUserStatisticsService service = new JpaUserStatisticsService();
        service.calculateDiffInPercentages(0F, 25.0F);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notOkayToCalculateDiffWithZeroEndPercentage() throws Exception {
        JpaUserStatisticsService service = new JpaUserStatisticsService();
        service.calculateDiffInPercentages(30.0F, 0F);
    }
}
