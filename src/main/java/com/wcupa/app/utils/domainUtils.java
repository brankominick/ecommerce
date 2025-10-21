package com.wcupa.app.utils;

import java.util.Arrays;
import java.util.List;

public class domainUtils {
    
    public static List<String> splitStringToList(String input) {
        String[] arr = input.split(",");
        return Arrays.asList(arr);
    }
}
