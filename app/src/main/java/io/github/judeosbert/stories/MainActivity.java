package io.github.judeosbert.stories;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.github.judeosbert.stories.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        activityMainBinding.newStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newStoryIntent = new Intent(getApplicationContext(),NewStoryActivity.class);
                startActivity(newStoryIntent);
            }
        });


        activityMainBinding.savedStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newStoryIntent = new Intent(getApplicationContext(),SavedStoriesActivity.class);
                startActivity(newStoryIntent);
            }
        });

        activityMainBinding.settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newStoryIntent = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(newStoryIntent);

            }
        });


    }
}
