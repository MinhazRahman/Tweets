package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.activities.TweetDetailActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    private static final int TWEET_WITH_EXTENDED_ENTITIES = 1;
    private static final int TWEET_WITHOUT_EXTENDED_ENTITIES = 0;

    Context context;
    List<Tweet> tweets;

    // Pass in the context and the list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if (viewType == TWEET_WITHOUT_EXTENDED_ENTITIES){
            View tweetView1 = layoutInflater.inflate(R.layout.item_tweet, parent, false);
            viewHolder = new ViewHolder(tweetView1);
        }
        else {
            View tweetView2 = layoutInflater.inflate(R.layout.item_tweet_extended_entities, parent, false);
            viewHolder = new ViewHolder(tweetView2);
        }

        return viewHolder;
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the tweet at passed in position
        Tweet tweet = tweets.get(position);

        // bind the tweet data in the ViewHolder based on the presence of extended entities
        //String mediaType = tweet.getExtendedEntities().getMediaTweet().getType();
        if (holder.getItemViewType() == TWEET_WITHOUT_EXTENDED_ENTITIES){
            holder.bindTweet(tweet);
        }
        else {
            holder.bindTweetWithExtendedEntities(tweet);
        }
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (tweets.get(position).getExtendedEntities() == null){
            return TWEET_WITHOUT_EXTENDED_ENTITIES;
        }else {
            return TWEET_WITH_EXTENDED_ENTITIES;
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items:tweets
    public void addAll(List<Tweet> listTweet) {
        tweets.addAll(listTweet);
        notifyDataSetChanged();
    }

    // Define a ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout rlTweetContainer;
        RelativeLayout rlTweetExtendedEntityContainer;
        ImageView ivProfileImage;
        TextView tvName;
        TextView tvScreenName;
        TextView tvRelativeTimestamp;
        TextView tvBody;
        ImageView ivMediaPhoto;
        VideoView videoViewMedia;
        RelativeLayout rlVideoContainer;

        // Views for reply, retweet, like and share buttons
        TextView tvReply;
        TextView tvRetweet;
        TextView tvLike;
        TextView tvShare;

        // itemView refers to each row layout (a set of views)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find each view of the row layout that itemView refers to
            rlTweetContainer = itemView.findViewById(R.id.rlTweetContainer);
            rlTweetExtendedEntityContainer = itemView.findViewById(R.id.rlTweetExtendedEntityContainer);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvRelativeTimestamp = itemView.findViewById(R.id.tvRelativeTimestamp);
            tvBody = itemView.findViewById(R.id.tvBody);
            ivMediaPhoto = itemView.findViewById(R.id.ivMediaPhoto);
            videoViewMedia = itemView.findViewById(R.id.videoViewMedia);
            rlVideoContainer = itemView.findViewById(R.id.rlVideoContainer);

            tvReply = itemView.findViewById(R.id.tvReply);
            tvRetweet = itemView.findViewById(R.id.tvRetweet);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvShare = itemView.findViewById(R.id.tvShare);
        }

        public void bindTweet(Tweet tweet) {
            // Register click listener on whole row that does not contain extended entities (image, gif, video)
            rlTweetContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Navigate to the TweetDetailActivity on tap
                    Intent intent = new Intent(context, TweetDetailActivity.class);
                    // Wrap tweet object with Parcels.wrap()
                    intent.putExtra("tweet", Parcels.wrap(tweet));

                    // Launch the TweetDetailActivity
                    context.startActivity(intent);
                }
            });

            // Load the profile image using Glide
            Glide.
                    with(context)
                    .load(tweet.getUser().getProfileImageUrl())
                    .circleCrop()
                    .into(ivProfileImage);

            tvName.setText(tweet.getUser().getName());
            tvScreenName.setText(tweet.getUser().getScreenName());
            tvRelativeTimestamp.setText(tweet.getRelativeTimestamp());
            tvBody.setText(tweet.getBody());

            tvRetweet.setText(String.valueOf(tweet.getRetweetCount()));
            tvLike.setText(String.valueOf(tweet.getFavouriteCount()));

        }

        public void bindTweetWithExtendedEntities(Tweet tweet) {
            // Register click listener on whole row that contain extended entities (image, gif, video)
            rlTweetExtendedEntityContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Navigate to the TweetDetailActivity on tap
                    Intent intent = new Intent(context, TweetDetailActivity.class);
                    // Wrap tweet object with Parcels.wrap()
                    intent.putExtra("tweet", Parcels.wrap(tweet));

                    // Launch the TweetDetailActivity
                    context.startActivity(intent);
                }
            });

            // Load the profile image using Glide
            Glide.
                    with(context)
                    .load(tweet.getUser().getProfileImageUrl())
                    .circleCrop()
                    .into(ivProfileImage);

            tvName.setText(tweet.getUser().getName());
            tvScreenName.setText(tweet.getUser().getScreenName());
            tvRelativeTimestamp.setText(tweet.getRelativeTimestamp());
            tvBody.setText(tweet.getBody());

            tvRetweet.setText(String.valueOf(tweet.getRetweetCount()));
            tvLike.setText(String.valueOf(tweet.getFavouriteCount()));

            // Check if the media type is 'image', 'animated_gif' or 'video'
            // Bind Photo, GIF or Video
            String mediaType = tweet.getExtendedEntities().getMediaTweet().getType();

            if ( !mediaType.equals("video")){
                rlVideoContainer.setVisibility(View.GONE);
                loadImage(tweet);
            }else {
                ivMediaPhoto.setVisibility(View.GONE);
                loadVideo(tweet);
            }
        }

        private void loadVideo(Tweet tweet) {
            try {
                MediaController mediaController = new MediaController(context);

                mediaController.setAnchorView(videoViewMedia);

                // get the url of the video
                String url = tweet.
                        getExtendedEntities()
                        .getMediaTweet()
                        .getVideoInfo()
                        .getVariants()
                        .getUrl();

                Log.i("TweetsAdapter", "Video url: " + url);

                videoViewMedia.setMediaController(mediaController);
                videoViewMedia.setVideoPath(url);

                videoViewMedia.requestFocus();
                videoViewMedia.start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void loadImage(Tweet tweet) {
            // Load the media image using Glide
            Glide.
                    with(context)
                    .load(tweet.getExtendedEntities().getMediaTweet().getMediaUrlHttps())
                    .transform(new CenterInside(), new RoundedCorners(30))
                    .into(ivMediaPhoto);
        }
    }



}
