package org.csspec.auth.util;

import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;

public class HttpHeaderParser {
    private Map<String, List<String>> results;

    private HttpHeaders headers;

    HttpHeaderParser(HttpHeaders headers) {
        this.headers = headers;
    }

    public Map<String, List<String>> getResults() {
        return results;
    }


}
