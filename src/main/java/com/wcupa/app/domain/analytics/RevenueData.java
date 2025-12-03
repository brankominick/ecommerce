package com.wcupa.app.domain.analytics;

import com.wcupa.app.domain.core.Order;

import java.util.List;
import java.util.ArrayList;

import java.time.LocalDate;

public class RevenueData {
    private final LocalDate start;
    private final LocalDate end;
    private final double totalRevenue;
    private final double totalTax;
    private final int totalOrders;

    public RevenueData(LocalDate start, LocalDate end, double totalRevenue, double totalTax, int totalOrders) {
        this.start = start;
        this.end = end;
        this.totalRevenue = totalRevenue;
        this.totalTax = totalTax;
        this.totalOrders = totalOrders;
    }
    
    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public int getTotalOrders() {
        return totalOrders;
    }
}
