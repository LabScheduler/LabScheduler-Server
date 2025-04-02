package com.example.labschedulerserver.utils;

public class ConvertFromJsonToTypeVariable {
    public static String convert(String json) {
        if (!json.contains("_")) {
            return json;
        }
        String[] path = json.split("_");
        StringBuilder result = new StringBuilder();
        result.append(path[0]);
        for (int i = 1; i < path.length; i++) {
            Character head = Character.toUpperCase(path[i].charAt(0));
            result.append(head).append(path[i].substring(1));
        }
        return result.toString();
    }
}
