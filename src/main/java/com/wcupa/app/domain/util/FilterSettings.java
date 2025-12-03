package com.wcupa.app.domain.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcupa.app.domain.core.Product;

public class FilterSettings {

    private static final Logger logger = LoggerFactory.getLogger(FilterSettings.class);


    public enum SortSetting {
        PRICE_ASC,
        PRICE_DESC,
        NAME_ASC,
        NAME_DESC,
        DATE_ASC,
        DATE_DESC,
        RATING_ASC,
        RATING_DESC
    }

    private String queryString;
    private List<String> category;
    private double minPrice;
    private double maxPrice;
    private double minRating;
    private SortSetting sortSetting;

    public FilterSettings(String query, List<String> category, double minPrice, double maxPrice, double minRating, SortSetting sortSetting) {
        this.queryString = query;
        this.category = category;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minRating = minRating;
        this.sortSetting = sortSetting;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String query) {
        this.queryString = query;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double min) {
        this.minPrice = min;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double max) {
        this.maxPrice = max;
    }

    public void setMinRating(double min) {
        this.minRating = min;
    }

    public double getMinRating() {
        return minRating;
    }

    public SortSetting getSortSetting() {
        return sortSetting;
    }

    public void setSortSetting(SortSetting setting) {
        this.sortSetting = setting;
    }

    public boolean isMatch(Product product) {
        boolean queryMatches = (product.getName().toLowerCase().contains(queryString) || product.getDescription().toLowerCase().contains(queryString));
        boolean categoryMatches = (category.isEmpty() || product.getTags().stream().anyMatch(category::contains));
        boolean priceMatches = (minPrice <= product.getPrice() && maxPrice >= product.getPrice());
        boolean ratingMatches = (minRating <= product.getAverageRating());
        return (queryMatches && categoryMatches && priceMatches && ratingMatches);
    }

    public String toString() {
        return String.format("Filter Settings:\nQuery String: %s\nCategory: %s\nMin Price: %s\nMax Price: %s\nMin Rating: %s\nSort Setting: %s\n", queryString, category.toString(), String.valueOf(minPrice), String.valueOf(maxPrice), String.valueOf(minRating), sortSetting.toString());
    }
}
