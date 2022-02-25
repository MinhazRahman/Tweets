package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class ExtendedEntities {
    private MediaTweet mediaTweet;

    public static ExtendedEntities fromJson(JSONObject jsonObject) throws JSONException {
        ExtendedEntities extendedEntities = new ExtendedEntities();
        extendedEntities.mediaTweet = MediaTweet.getMediaFromJsonArray(jsonObject.getJSONArray("media"));

        return extendedEntities;
    }

    public MediaTweet getMediaTweet() {
        return mediaTweet;
    }
}
