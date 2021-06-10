package de.twisted.examshare.data.models;

import de.twisted.examshare.util.helper.TextUtil;

import java.util.HashMap;
import java.util.Map;

public class QueryData {

    private Map<String, String> data;

    public QueryData(String keyword, int dateFilter, int tasksOrder) {
        this.data = new HashMap<>();
        this.set("keyword", keyword);
        this.set("dateFilter", String.valueOf(dateFilter));
        this.set("tasksOrder", String.valueOf(tasksOrder));
    }

    public void set(String key, String value) {
        if (!value.isEmpty() && !value.equals("-1"))
            data.put(key, value);
    }

    public boolean hasKeyword() {
        return !TextUtil.isEmpty(data.get("keyword"));
    }

    public Map<String, String> getMap() {
        return this.data;
    }

}
