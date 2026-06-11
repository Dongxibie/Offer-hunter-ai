package com.offerhunter.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JSON提取工具类 - 从AI响应中提取JSON内容
 * 支持：纯JSON、markdown代码块包裹的JSON、含多余文本的JSON
 */
public class JsonExtractorUtil {

    private static final Pattern JSON_BLOCK_PATTERN = Pattern.compile("```(?:json)?\\s*\\n?(\\{[\\s\\S]*?})\\s*\\n?```");
    private static final Pattern JSON_PATTERN = Pattern.compile("\\{[^{}]*(?:\\{[^{}]*}[^{}]*)*}");

    /**
     * 从AI响应文本中提取JSON字符串
     *
     * @param response AI返回的原始文本
     * @return 提取到的JSON字符串
     * @throws IllegalArgumentException 无法提取有效JSON时抛出
     */
    public static String extractJson(String response) {
        if (response == null || response.isBlank()) {
            throw new IllegalArgumentException("AI响应内容为空");
        }

        // 1. 优先匹配markdown代码块中的JSON
        Matcher blockMatcher = JSON_BLOCK_PATTERN.matcher(response);
        if (blockMatcher.find()) {
            return blockMatcher.group(1).trim();
        }

        // 2. 尝试用括号配对方式提取最外层JSON对象
        String result = extractByBraceMatching(response);
        if (result != null) {
            return result;
        }

        // 3. 正则兜底
        Matcher jsonMatcher = JSON_PATTERN.matcher(response);
        if (jsonMatcher.find()) {
            return jsonMatcher.group();
        }

        // 4. 最后尝试直接解析（可能整个响应就是JSON）
        String trimmed = response.trim();
        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            return trimmed;
        }

        throw new IllegalArgumentException("无法从AI响应中提取JSON: " + response.substring(0, Math.min(100, response.length())));
    }

    /**
     * 通过括号配对提取最外层完整的JSON对象
     */
    private static String extractByBraceMatching(String text) {
        int start = text.indexOf('{');
        if (start == -1) return null;

        int depth = 0;
        boolean inString = false;
        boolean escaped = false;

        for (int i = start; i < text.length(); i++) {
            char c = text.charAt(i);

            if (escaped) {
                escaped = false;
                continue;
            }

            if (c == '\\' && inString) {
                escaped = true;
                continue;
            }

            if (c == '"') {
                inString = !inString;
                continue;
            }

            if (inString) continue;

            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    return text.substring(start, i + 1);
                }
            }
        }

        return null;
    }
}
