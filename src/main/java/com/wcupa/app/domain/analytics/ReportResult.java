package com.wcupa.app.domain.analytics;

import java.time.LocalDateTime;

public abstract class ReportResult<T> implements ReportData<T> {
    protected final String title;
    protected final LocalDateTime generatedTime;
    protected final T data;

    ReportResult(String title, T data) {
        this.title = title;
        this.generatedTime = LocalDateTime.now();
        this.data = data;
    }

    @Override
    public String getReportTitle() {
        return title;
    }

    @Override
    public LocalDateTime getGeneratedTime() {
        return generatedTime;
    }

    @Override
    public T getData() {
        return data;
    }
}
