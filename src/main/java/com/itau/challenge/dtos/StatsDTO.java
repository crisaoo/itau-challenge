package com.itau.challenge.dtos;

import java.util.DoubleSummaryStatistics;

public record StatsDTO(long count, double sum, double avg, double min, double max) {
    public StatsDTO(DoubleSummaryStatistics stats) {
        this(stats.getCount(),
            stats.getSum(),
            stats.getAverage(),
            Double.isInfinite(stats.getMin())? 0.0 : stats.getMin(),
            Double.isInfinite(stats.getMax())? 0.0 : stats.getMax()
        );
    }
}
