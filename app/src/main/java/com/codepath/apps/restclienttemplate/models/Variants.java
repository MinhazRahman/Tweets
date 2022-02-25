package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Variants {
    private long bitrate;
    private String contentType;
    private String url;

    public static Variants getVariantsFromJsonArray(JSONArray jsonArray) throws JSONException {
        Variants variants = new Variants();

        JSONObject variantsJson = jsonArray.getJSONObject(0);

        if (variantsJson.has("bitrate")){
            variants.bitrate = variantsJson.getLong("bitrate");
        }
        variants.contentType = variantsJson.getString("content_type");
        variants.url = variantsJson.getString("url");

        return variants;
    }

    public long getBitrate() {
        return bitrate;
    }

    public String getContentType() {
        return contentType;
    }

    public String getUrl() {
        return url;
    }
}
