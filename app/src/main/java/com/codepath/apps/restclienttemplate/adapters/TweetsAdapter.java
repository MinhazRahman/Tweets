package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

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
        ImageView ivProfileImage;
        TextView tvName;
        TextView tvScreenName;
        TextView tvRelativeTimestamp;
        TextView tvBody;
        ImageView ivMediaPhoto;

        // itemView refers to each row layout (a set of views)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find each view of the row layout that itemView refers to
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvRelativeTimestamp = itemView.findViewById(R.id.tvRelativeTimestamp);
            tvBody = itemView.findViewById(R.id.tvBody);
            ivMediaPhoto = itemView.findViewById(R.id.ivMediaPhoto);
        }

        public void bindTweet(Tweet tweet) {
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

        }

        public void bindTweetWithExtendedEntities(Tweet tweet) {
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

            // Load the media image using Glide
            Glide.
                    with(context)
                    .load(tweet.getExtendedEntities().getMediaTweet().getMediaUrlHttps())
                    .transform(new CenterInside(), new RoundedCorners(30))
                    .into(ivMediaPhoto);
        }
    }


}
