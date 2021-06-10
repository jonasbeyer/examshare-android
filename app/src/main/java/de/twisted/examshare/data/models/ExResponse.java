package de.twisted.examshare.data.models;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExResponse {

    private int code;
    private String message;
    private Map<String, Object> data;

    public ExResponse(ResponseType type) {
        this.code = type.getResponseCode();
    }

    public ResponseType getType() {
        for (ResponseType response : ResponseType.values()) {
            if (response.getResponseCode() == code)
                return response;
        }
        return code == 0 ? ResponseType.ERROR : ResponseType.UNKNOWN;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public boolean isDenied() {
        ResponseType type = getType();
        return type == ResponseType.ERROR || type == ResponseType.UNAUTHORIZED || type == ResponseType.BAD_VERSION;
    }

    public boolean isUnavailable(boolean enabled) {
        ResponseType type = getType();
        return enabled && (type == ResponseType.ERROR || type == ResponseType.BAD_VERSION);
    }

    public boolean isMissing(String key) {
        return data == null || !data.containsKey(key);
    }

    public int getInt(String key) {
        return isMissing(key) ? 0 : ((Double) data.get(key)).intValue();
    }

    public boolean getBoolean(String key) {
        return (Boolean) data.get(key);
    }

    public String getString(String key) {
        return (String) data.get(key);
    }

    public List getList(String key) {
        return (List) data.get(key);
    }

    public List getItems(Gson gson, Class classOfT) {
        List list = new ArrayList<>();
        List<LinkedTreeMap> itemList = getList("Items");
        for (LinkedTreeMap item : itemList)
            list.add(gson.fromJson(gson.toJsonTree(item), classOfT));

        return list;
    }

    public Map getMap(String key) {
        return (Map) data.get(key);
    }

    public double getDouble(String key) {
        return (Double) data.get(key);
    }

    public long getLong(String key) {
        return ((Double) data.get(key)).longValue();
    }

    public ExResponse putData(String key, Object value) {
        if (data == null)
            data = new HashMap<>();

        data.put(key, value);
        return this;
    }

    public enum ResponseType {
        UNKNOWN(-1),
        SUCCESS(200),
        ERROR(400),
        UNAUTHORIZED(401),
        PENDING_EMAIl(402),
        NOT_FOUND(404),
        BAD_VERSION(407);


        private int responseCode;

        ResponseType(int responseCode) {
            this.responseCode = responseCode;
        }

        public int getResponseCode() {
            return responseCode;
        }
    }

}
