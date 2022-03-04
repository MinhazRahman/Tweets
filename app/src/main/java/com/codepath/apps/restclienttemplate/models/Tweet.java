package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.utils.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {
    private long id;
    private String body;
    private String createdAt;
    private User user;
    private ExtendedEntities extendedEntities;
    private long retweetCount;
    private long favouriteCount;

    // empty constructor needed by the Parceler library
    public Tweet() {
    }

    // Create Tweet object from json object
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.id = jsonObject.getLong("id");
        tweet.body = jsonObject.getString("full_text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        
        if (jsonObject.has("extended_entities")){
            tweet.extendedEntities = ExtendedEntities.fromJson(jsonObject.getJSONObject("extended_entities"));
        }
        tweet.retweetCount = jsonObject.getLong("retweet_count");
        tweet.favouriteCount = jsonObject.getLong("favorite_count");

        return tweet;
    }

    // Get normal tweet (not the extended tweet) from json.
    public static Tweet getTextOnlyTweetFromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.id = jsonObject.getLong("id");
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        return tweet;
    }

    // Create the list of Tweets from the json array
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();

        for (int i=0; i<jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }

        return tweets;
    }

    public long getId(){
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public ExtendedEntities getExtendedEntities() {
        return extendedEntities;
    }

    public String getRelativeTimestamp(){
        return ". " + TimeFormatter.getTimeDifference(createdAt);
    }

    public long getRetweetCount() {
        return retweetCount;
    }

    public long getFavouriteCount() {
        return favouriteCount;
    }
}
