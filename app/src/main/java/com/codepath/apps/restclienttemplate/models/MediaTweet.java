package com.codepath.apps.restclienttemplate.models;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MediaTweet {
    private long mediaId;
    private String mediaUrlHttps;
    private String expandedUrl;
    private String type;
    private VideoInfo videoInfo;

    public MediaTweet() {

    }

    public MediaTweet(JSONObject jsonObject) throws JSONException{
        mediaId = jsonObject.getLong("id");
        mediaUrlHttps = jsonObject.getString("media_url_https");
        expandedUrl = jsonObject.getString("expanded_url");
        type = jsonObject.getString("type");
    }

    public static MediaTweet getMediaFromJsonArray(JSONArray jsonArray) throws JSONException {
        MediaTweet mediaTweet = new MediaTweet();

        mediaTweet.mediaId = jsonArray.getJSONObject(0).getLong("id");
        mediaTweet.mediaUrlHttps = jsonArray.getJSONObject(0).getString("media_url_https");
        mediaTweet.expandedUrl = jsonArray.getJSONObject(0).getString("expanded_url");
        mediaTweet.type = jsonArray.getJSONObject(0).getString("type");

        if (jsonArray.getJSONObject(0).has("video_info")){
            mediaTweet.videoInfo = VideoInfo.getVideoInfoFromJson(jsonArray.getJSONObject(0).getJSONObject("video_info"));

        }

        return mediaTweet;
    }

    // get the List of MediaTweet from the json array
    @NonNull
    public static List<MediaTweet> getListOfMediaTweet(@NonNull JSONArray mediaJsonArray) throws JSONException {
        List<MediaTweet> movies = new ArrayList<>();

        for (int i=0; i<mediaJsonArray.length(); i++){
            movies.add(new MediaTweet(mediaJsonArray.getJSONObject(i)));
        }

        return movies;
    }

    public long getMediaId() {
        return mediaId;
    }

    public String getMediaUrlHttps() {
        return mediaUrlHttps;
    }

    public String getExpandedUrl() {
        return expandedUrl;
    }

    public String getType() {
        return type;
    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }
}
