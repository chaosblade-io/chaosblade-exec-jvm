package com.alibaba.chaosblade.exec.plugin.http;

/**
 * @author pengpj
 */
public class UrlUtils {

    private static final String QUERY_SYMBOL = "?";

    /**
     * Get the URL and exclude the query parameters
     */
    public static String getUrlExcludeQueryParameters(String path) {
        if (path.contains(QUERY_SYMBOL)) {
            return path.substring(0, path.indexOf(QUERY_SYMBOL));
        }
        return path;
    }

    private UrlUtils() {
    }
}
