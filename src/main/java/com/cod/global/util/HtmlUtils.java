package com.cod.global.util;

public class HtmlUtils {
    public static String convertLineBreaksToHtml(String text) {
        if (text == null) {
            return "";
        }
        // Convert line breaks to <br> tags
        return text.replaceAll("\r\n|\r|\n", "<br>");
    }
}
