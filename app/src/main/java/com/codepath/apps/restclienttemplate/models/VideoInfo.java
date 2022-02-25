package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoInfo {
    private Variants variants;

    public static VideoInfo getVideoInfoFromJson(JSONObject jsonObject) throws JSONException {
        VideoInfo videoInfo = new VideoInfo();

        if (jsonObject.has("variants")){
            videoInfo.variants = Variants.getVariantsFromJsonArray(jsonObject.getJSONArray("variants"));
        }

        return videoInfo;
    }

    public Variants getVariants() {
        return variants;
    }
}
