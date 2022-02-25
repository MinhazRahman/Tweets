package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class VideoInfo {
    private Variants variants;

    // empty constructor needed by the Parceler library
    public VideoInfo() {
    }
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
