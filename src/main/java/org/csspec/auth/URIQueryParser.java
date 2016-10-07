package org.csspec.auth;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class URIQueryParser {
    public static Map<String, List<String>> parse(String queries) throws Exception {
        if (queries.length() < 1)
            throw new RuntimeException("queries length cannot be zero");

        Map<String, List<String>> result = new HashMap<>();
        for (String query : queries.split("&")) {
            String pair[] = query.split("=");

            String key = URLDecoder.decode(pair[0], "UTF-8");
            String value = "";
            if (pair.length > 0) {
                value = URLDecoder.decode(pair[1], "UTF-8");
            }

            List<String> values = result.get(key);
            if (values == null) {
                values = new ArrayList<>();
                result.put(key, values);
            }
            values.add(value);
        }

        return result;
    }
}
