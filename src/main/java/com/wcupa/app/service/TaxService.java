package com.wcupa.app.service;

public class TaxService {

    public static double calculateTax(String shippingAddress, double subtotal) {
        String state = parseState(shippingAddress);
        double rate = getRateForState(state);

        return subtotal * rate;
    }

    private static String parseState(String shippingAddress) {
        // "Name\nStreet\nCity, State, Zip"
        return shippingAddress.split(",")[1].strip();
    }

    private static double getRateForState(String state) {
        return switch (state) {
            case "AL" -> 0.0400;
            case "AK" -> 0.0000;
            case "AZ" -> 0.0560;
            case "AR" -> 0.0650;
            case "CA" -> 0.0725;
            case "CO" -> 0.0290;
            case "CT" -> 0.0635;
            case "DE" -> 0.0000;
            case "FL" -> 0.0600;
            case "GA" -> 0.0400;
            case "HI" -> 0.0400;
            case "ID" -> 0.0600;
            case "IL" -> 0.0625;
            case "IN" -> 0.0700;
            case "IA" -> 0.0600;
            case "KS" -> 0.0650;
            case "KY" -> 0.0600;
            case "LA" -> 0.0500;
            case "ME" -> 0.0550;
            case "MD" -> 0.0600;
            case "MA" -> 0.0625;
            case "MI" -> 0.0600;
            case "MN" -> 0.0688;
            case "MS" -> 0.0700;
            case "MO" -> 0.0423;
            case "MT" -> 0.0000;
            case "NE" -> 0.0550;
            case "NV" -> 0.0460;
            case "NH" -> 0.0000;
            case "NJ" -> 0.0663;
            case "NM" -> 0.0513;
            case "NY" -> 0.0400;
            case "NC" -> 0.0475;
            case "ND" -> 0.0500;
            case "OH" -> 0.0575;
            case "OK" -> 0.0450;
            case "OR" -> 0.0000;
            case "PA" -> 0.0600;
            case "RI" -> 0.0700;
            case "SC" -> 0.0600;
            case "SD" -> 0.0450;
            case "TN" -> 0.0700;
            case "TX" -> 0.0625;
            case "UT" -> 0.0470;
            case "VT" -> 0.0600;
            case "VA" -> 0.0430;
            case "WA" -> 0.0650;
            case "WV" -> 0.0600;
            case "WI" -> 0.0500;
            case "WY" -> 0.0400;

            default -> 0.05;  // ~national average
        };

    }
}