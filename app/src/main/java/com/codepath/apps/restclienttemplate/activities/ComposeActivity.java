package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.codepath.apps.restclienttemplate.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ComposeActivity extends AppCompatActivity {
    public static final int MAX_TWEET_LENGTH = 140;

    EditText editTextCompose;
    Button btnTweet;
    ImageView ivCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // Find the views inside the activity layout
        editTextCompose = findViewById(R.id.editTextCompose);
        btnTweet = findViewById(R.id.btnTweet);
        ivCancel = findViewById(R.id.ivCancel);

        // Set the click listener on the button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        // Set the click listener on the button
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}