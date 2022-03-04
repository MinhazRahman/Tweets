package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {

    RelativeLayout rlVideoContainer;
    ImageView ivBackArrow;
    ImageView ivProfileImage;
    TextView tvUserName;
    TextView tvScreenName;
    TextView tvRelativeTimestamp;
    TextView tvBody;
    ImageView ivTweetImage;
    VideoView videoViewPlayer;
    ImageView iconPlay;

    // Views for reply, retweet, like and share buttons
    TextView tvReply;
    TextView tvRetweet;
    TextView tvLike;
    TextView tvShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        // Find the views
        rlVideoContainer = findViewById(R.id.rlVideoContainer);
        ivBackArrow = findViewById(R.id.ivBackToTimeline);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvUserName = findViewById(R.id.tvUserName);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvRelativeTimestamp = findViewById(R.id.tvRelativeTimestamp);
        tvBody = findViewById(R.id.tvBody);
        ivTweetImage = findViewById(R.id.ivTweetImage);
        videoViewPlayer = findViewById(R.id.videoViewPlayer);
        iconPlay = findViewById(R.id.iconPlay);

        tvReply = findViewById(R.id.tvReply);
        tvRetweet = findViewById(R.id.tvRetweet);
        tvLike = findViewById(R.id.tvLike);
        tvShare = findViewById(R.id.tvShare);

        // Unwrap the Parcel object
        Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        // check if the Tweet has Extended Entities object or not
        // If the Tweet does not have Extended Entities then set the values only for common entities
        // Otherwise, set the values for both common entities and extended entities (such as image, gif, and video)
        if (tweet.getExtendedEntities() == null){
            setCommonEntities(tweet);

            // set the visibility for views that contain extended entities
            rlVideoContainer.setVisibility(View.GONE);
            ivTweetImage.setVisibility(View.GONE);

        }else { // Tweet with Extended Entities object
            // Check if the media type is 'image', 'animated_gif' or 'video'
            String mediaType = tweet.getExtendedEntities().getMediaTweet().getType();
            if (!mediaType.equals("video")){
                rlVideoContainer.setVisibility(View.GONE);

                // Bind common entities
                setCommonEntities(tweet);

                // And then load the embedded image
                loadImage(tweet);
            }
            else {
                // In case of 'video' set the visibility GONE for both (for now)
                // Will work on it later
                ivTweetImage.setVisibility(View.GONE);
                rlVideoContainer.setVisibility(View.GONE);

                // Bind common entities
                setCommonEntities(tweet);

            }
        }

        // When clicked on the Back Arrow go back to the TimelineActivity
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // closes the activity
            }
        });
    }

    private void setCommonEntities(Tweet tweet) {
        // Extract the properties of the tweet object
        // Load the profile image using Glide
        Glide.
                with(this)
                .load(tweet.getUser().getProfileImageUrl())
                .circleCrop()
                .into(ivProfileImage);

        tvUserName.setText(tweet.getUser().getName());
        tvScreenName.setText(tweet.getUser().getScreenName());
        tvRelativeTimestamp.setText(tweet.getRelativeTimestamp());
        tvBody.setText(tweet.getBody());

        tvRetweet.setText(String.valueOf(tweet.getRetweetCount()));
        tvLike.setText(String.valueOf(tweet.getFavouriteCount()));
    }

    private void loadImage(Tweet tweet) {
        // Load the media image using Glide
        Glide.
                with(this)
                .load(tweet.getExtendedEntities().getMediaTweet().getMediaUrlHttps())
                .transform(new CenterInside(), new RoundedCorners(30))
                .into(ivTweetImage);
    }
}