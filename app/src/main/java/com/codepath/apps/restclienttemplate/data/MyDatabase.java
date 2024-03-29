package com.codepath.apps.restclienttemplate.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.codepath.apps.restclienttemplate.models.SampleModel;
import com.codepath.apps.restclienttemplate.models.SampleModelDao;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.data.TweetDAO;

@Database(entities={SampleModel.class, Tweet.class, User.class}, version=1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract SampleModelDao sampleModelDao();
    public abstract TweetDAO tweetDao();

    // Database name to be used
    public static final String NAME = "MyDataBase";
}
