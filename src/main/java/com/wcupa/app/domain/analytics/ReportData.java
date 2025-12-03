package com.wcupa.app.domain.analytics;

import java.time.LocalDateTime;

public interface ReportData<T> {
    String getReportTitle();

    LocalDateTime getGeneratedTime();

    T getData();
}
