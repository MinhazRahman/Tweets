package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.application.TwitterApp;
import com.codepath.apps.restclienttemplate.client.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 15;

    TextInputLayout textInputLayoutCompose;
    TextInputEditText textInputEditTextCompose;
    Button btnTweet;
    TextView tvCancel;
    TwitterClient twitterClient;
    ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // Initialize the TwitterClient
        twitterClient = TwitterApp.getRestClient(this);

        // Find the views inside the activity layout
        textInputLayoutCompose = findViewById(R.id.textInputLayoutCompose);
        textInputEditTextCompose = findViewById(R.id.editTextCompose);
        btnTweet = findViewById(R.id.btnTweet);
        tvCancel = findViewById(R.id.tvCancel);
        ivProfileImage = findViewById(R.id.ivProfileImage);

        // Set the click listener on the button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the tweet content that we will publish
                String tweetContent = textInputEditTextCompose.getText().toString();

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

        // Set add text listener on the input edit text
        // Handle an event as the text in the view is being changed.
        // Display a character count for the twitter client as the user types their tweet
        textInputEditTextCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputLayoutCompose.setHint(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() == 0) {
                    textInputEditTextCompose.setHint(null);
                    textInputLayoutCompose.setHint("What's happening?");
                }else if((MAX_TWEET_LENGTH - editable.length()) >= 0) {
                    textInputLayoutCompose.setHint((MAX_TWEET_LENGTH - editable.length())+" character left");
                }
                else {
                    textInputLayoutCompose.setHint((MAX_TWEET_LENGTH - editable.length())+"");
                }
            }

        });

    }

    // Get logged in user credentials
    private void getLoggedInUser(){

        twitterClient.getVerifiedUserCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess to get user credentials");
                try {
                    User loggedInUser = User.fromJson(json.jsonObject);
                    Log.i(TAG, "Uer name: " + loggedInUser.getName());
                    Log.i(TAG, "Uer Screen name: " + loggedInUser.getScreenName());
                    Log.i(TAG, "Profile image url: " + loggedInUser.getProfileImageUrl());
                    setProfileImage(loggedInUser);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure to get user credentials" + response, throwable);
            }
        });
    }

    // Set profile image for the logged in user
    private void setProfileImage(User user){
        // Load the profile image using Glide
        Glide.
                with(this)
                .load(user.getProfileImageUrl())
                .circleCrop()
                .into(ivProfileImage);
    }

    // onResume method is called after onCreate method
    // After creating all the views we want to populate them with data that we will get from the api call
    @Override
    protected void onResume() {
        super.onResume();

        // set focus on textInputEditTextCompose
        textInputEditTextCompose.setFocusableInTouchMode(true);
        textInputEditTextCompose.requestFocus();
        textInputLayoutCompose.setHint(null);
        textInputEditTextCompose.setHint("What's happening?");

        // get logged in user
        getLoggedInUser();
    }
}