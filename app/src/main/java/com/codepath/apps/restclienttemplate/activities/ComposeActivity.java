package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.application.TwitterApp;
import com.codepath.apps.restclienttemplate.client.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 140;

    EditText editTextCompose;
    Button btnTweet;
    TextView tvCancel;
    TwitterClient twitterClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // Initialize the TwitterClient
        twitterClient = TwitterApp.getRestClient(this);

        // Find the views inside the activity layout
        editTextCompose = findViewById(R.id.editTextCompose);
        btnTweet = findViewById(R.id.btnTweet);
        tvCancel = findViewById(R.id.tvCancel);

        // Set the click listener on the button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the tweet content that we will publish
                String tweetContent = editTextCompose.getText().toString();

                if (tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Sorry! your tweet can not be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tweetContent.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this, "Sorry! your tweet is too long.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Make an API call to the Twitter to publish the tweet
                twitterClient.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish tweet");

                        try {
                            // Get the published tweet
                            Tweet tweet = Tweet.getTextOnlyTweetFromJson(json.jsonObject);

                            Log.i(TAG, "Published tweet: " + tweet.getBody());

                            // Prepare data intent
                            Intent intent = new Intent();
                            // Pass relevant data back as a result
                            // Wrap tweet object with Parcels.wrap()
                            intent.putExtra("tweet", Parcels.wrap(tweet));

                            // Activity finished ok, return the data
                            // set result code and bundle data for response
                            setResult(RESULT_OK, intent);

                            //closes the activity, pass data to parent
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet" + response, throwable);
                    }
                });
            }
        });

        // Set the click listener on the button
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}