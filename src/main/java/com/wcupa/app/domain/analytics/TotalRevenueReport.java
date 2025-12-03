package com.wcupa.app.domain.analytics;

import java.time.LocalDateTime;

public class TotalRevenueReport extends ReportResult<RevenueData> {

    public TotalRevenueReport(RevenueData revenueData) {
        super(String.format("Total revenue report (%s - %s)",revenueData.getStart().toString(), revenueData.getEnd().toString()), revenueData);
    }
  
    
}
