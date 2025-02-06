package io.vitech.flights.tracker.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaginationConfig {

    @Value("${vitech.tracker.pagination.default-page-size}")
    private int defaultPageSize;

    @Value("${vitech.tracker.pagination.max-page-size}")
    private int maxPageSize;

    public int getDefaultPageSize() {
        return defaultPageSize;
    }

    public int getMaxPageSize() {
        return maxPageSize;
    }
}
