package com.codepath.apps.restclienttemplate.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter;
import com.codepath.apps.restclienttemplate.application.TwitterApp;
import com.codepath.apps.restclienttemplate.client.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.utils.SpacesItemDecoration;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 20;
    public static String TAG = "TimelineActivity";

    TwitterClient twitterClient;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter tweetsAdapter;
    RecyclerView.ItemDecoration itemDecoration;
    SpacesItemDecoration spacesItemDecoration;
    SwipeRefreshLayout swipeContainer;
    EndlessRecyclerViewScrollListener scrollListener;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Find the toolbar view inside the activity layout
        // Set windowActionBar to false in the theme to use a Toolbar instead
        toolbar = findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        twitterClient = TwitterApp.getRestClient(this);

        // Find the views
        swipeContainer = findViewById(R.id.swipeContainer);
        rvTweets = findViewById(R.id.rvTweets);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "Fetching new data.");
                // code to refresh the list here
                populateHomeTimeline();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Initialize the list of tweets and adapter
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(this, tweets);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        // Setup the RecyclerView: layout manager and the adapter
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(tweetsAdapter);

        // decorate each item using decorators attached to the recyclerview
        itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        // attach a decorator to  the recyclerview for adding consistent spacing around items displayed
        spacesItemDecoration = new SpacesItemDecoration(16);

        rvTweets.addItemDecoration(itemDecoration);
        rvTweets.addItemDecoration(spacesItemDecoration);

        // Define the scrollListener by passing the linearLayoutManager
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG,"onLoadMore" + page);
                loadMoreTweets();
            }
        };

        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        populateHomeTimeline();

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Returning Data Result to Timeline Activity
    // Use an ActivityResultLauncher, which launches an Activity and expects a Result to come back.
    ActivityResultLauncher<Intent> composeActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // If the user comes back to this activity from EditActivity
                    // with no error or cancellation
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Get the data from the Intent (tweet)
                        Intent data = result.getData();
                        // Unwrap the Parcel object
                        assert data != null;
                        Tweet tweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet"));

                        // Update the recycler view with the tweet
                        // Modify the data source of tweets
                        tweets.add(0,tweet);
                        // Update the adapter/notify the adapter
                        tweetsAdapter.notifyItemInserted(0);
                        // scroll to the first tweet at position 0, top of the recycler view
                        rvTweets.smoothScrollToPosition(0);
                    }
                }
            });

    // Handle clicks on the Menu item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle presses on the action bar items
        if (item.getItemId() == R.id.compose){
            // compose icon has been selected
            Toast.makeText(this,  "Compose", Toast.LENGTH_SHORT).show();

            // Navigate to the compose activity
            Intent intent = new Intent(this, ComposeActivity.class);
            composeActivityResultLauncher.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    // this is where we will make another API call to get the next page of tweets
    // and add the objects to our current list of tweets
    private void loadMoreTweets() {
        // Send an API request to retrieve appropriate paginated data
        twitterClient.getNextPageOfTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess." + json.toString());
                // Deserialize and construct new model objects from the API response
                JSONArray jsonArray = json.jsonArray;
                try {
                    // Get the list of tweets
                    List<Tweet> tweetList = Tweet.fromJsonArray(jsonArray);
                    // Append the new data objects to the existing set of items inside the array of items
                    // Also notify the adapter of the new items made with `notifyItemRangeInserted()`
                   tweetsAdapter.addAll(tweetList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure on loadMoreTweets." + response,throwable);
            }
        }, tweets.get(tweets.size() - 1).getId()); // pass the id of the last tweet.
    }

    private void populateHomeTimeline() {
        twitterClient.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess." + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    // CLEAR OUT old items before appending in the new ones
                    tweetsAdapter.clear();
                    // ...the data has come back, add new items to the adapter...
                    tweetsAdapter.addAll(Tweet.fromJsonArray(jsonArray));
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    Log.i(TAG, "Json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure." + response,throwable);
            }
        });
    }
}