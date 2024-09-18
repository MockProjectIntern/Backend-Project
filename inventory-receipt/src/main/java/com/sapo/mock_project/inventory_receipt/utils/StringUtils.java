package com.sapo.mock_project.inventory_receipt.utils;

public class StringUtils {
    public static String snakeCaseToCamelCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        // Tách chuỗi thành các từ dựa trên dấu gạch dưới
        String[] words = str.split("_");

        // Tạo một StringBuilder để gộp các từ đã chuyển đổi
        StringBuilder camelCaseString = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (words[i].length() > 0) {
                // Chữ cái đầu tiên của mỗi từ cần viết hoa
                camelCaseString.append(Character.toUpperCase(words[i].charAt(0)))
                        .append(words[i].substring(1).toLowerCase());
            }
        }

        return camelCaseString.toString();
    }

    public static String snakeCaseToEqualCamelCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        // Tách chuỗi thành các từ dựa trên dấu gạch dưới
        String[] words = str.split("_");

        // Tạo một StringBuilder để gộp các từ đã chuyển đổi
        StringBuilder camelCaseString = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (i == 0) {
                // Chữ cái đầu tiên của từ đầu tiên không cần viết hoa
                camelCaseString.append(words[i].toLowerCase());
                continue;
            }
            if (words[i].length() > 0) {
                // Chữ cái đầu tiên của mỗi từ cần viết hoa
                camelCaseString.append(Character.toUpperCase(words[i].charAt(0)))
                        .append(words[i].substring(1).toLowerCase());
            }
        }

        return camelCaseString.toString();
    }
}
