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
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the tweet at passed in position
        Tweet tweet = tweets.get(position);

        // Bind the tweet with view holder
        holder.bindTweet(tweet);

    }

    @Override
    public int getItemCount() {
        return tweets.size();
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

        // itemView refers to each row layout (a set of views)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find each view of the row layout that itemView refers to
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvRelativeTimestamp = itemView.findViewById(R.id.tvRelativeTimestamp);
            tvBody = itemView.findViewById(R.id.tvBody);
        }

        public void bindTweet(Tweet tweet) {
            tvName.setText(tweet.getUser().getName());
            tvScreenName.setText(tweet.getUser().getScreenName());
            tvRelativeTimestamp.setText(tweet.getRelativeTimestamp());
            tvBody.setText(tweet.getBody());

            // Load the profile image using Glide
            Glide.
                    with(context)
                    .load(tweet.getUser().getProfileImageUrl())
                    .into(ivProfileImage);
        }
    }


}
