package io.github.judeosbert.stories;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.judeosbert.stories.data.DBHelperActivity;
import io.github.judeosbert.stories.data.StoryContract;
import io.github.judeosbert.stories.data.StoryManagerProvider;
import io.github.judeosbert.stories.databinding.ActivityNewStoryBinding;

public class NewStoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    NetworkHelper networkHelper;
    ActivityNewStoryBinding newStoryBinding;
    private final String LOGTAG = "story-app-log-tag";
    private final String REQUESTFAILED = "request-failed";
    private final int LOADERID = 200;
    private boolean isDarkThemeActive;
    private String storyPermalink;
    private class storyDetails{
        public String getPrompt() {
            return prompt;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        public String getPermalink() {
            return permalink;
        }

        String prompt;
        String author;
        String content;
        String permalink;

        public storyDetails(String prompt,String author,String content,String permalink)
        {
            this.prompt = prompt;
            this.author = author;
            this.content = content;
            this.permalink = permalink;
        }
    }
    private String storyAppTagLine = "Stories : Read stories in your free time.";
    private storyDetails details;
    StoryManagerProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newStoryBinding = DataBindingUtil.setContentView(this,R.layout.activity_new_story);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isDarkThemeActive = sharedPreferences.getBoolean("appTheme",false);
        final LoaderManager.LoaderCallbacks<String> callbacks = this;
        getSupportLoaderManager().initLoader(LOADERID,null,callbacks);
        newStoryBinding.includeView.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportLoaderManager().restartLoader(200,null,callbacks);
            }
        });
        if(isDarkThemeActive)
        {
            switchToDarkTheme();
        }
        else
        {
            switchToLightTheme();
        }


        newStoryBinding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                    isDarkThemeActive = !isDarkThemeActive;
                    if(isDarkThemeActive)
                        switchToDarkTheme();
                    else
                        switchToLightTheme();

            }
        });
        newStoryBinding.includeView.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                String textToShare = (storyPermalink == null || storyPermalink.length() == 0)?storyAppTagLine:storyPermalink;
                shareIntent.putExtra(Intent.EXTRA_TEXT,textToShare);
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        });

        newStoryBinding.includeView.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(details != null) {
                    ContentValues storyValues = new ContentValues();
                    storyValues.put(StoryContract.StoryEntry.COLUMN_AUTHOR, details.getAuthor());
                    storyValues.put(StoryContract.StoryEntry.COLUMN_PROMPT, details.getPrompt());
                    storyValues.put(StoryContract.StoryEntry.COLUMN_BODY, details.getContent());
                    storyValues.put(StoryContract.StoryEntry.COLUMN_PERMALINK, details.getPermalink());

                    Uri newURI = provider.insert(StoryManagerProvider.CONTENT_URI,storyValues);
                    if(newURI != null )
                    {
                        Toast.makeText(getApplicationContext(),"Your story has been saved",Toast.LENGTH_LONG).show();
                        newStoryBinding.includeView.saveButton.setEnabled(false);
                    }

                    DBHelperActivity dbHelper = new DBHelperActivity(getApplicationContext());
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.insert(StoryContract.StoryEntry.TABLE_NAME,null,storyValues);
                }
                else
                    return;

            }
        });

    }

    private void switchToLightTheme() {
        newStoryBinding.constraintAppLayout.setBackgroundColor(getResources().getColor(R.color.Lbackground));
        newStoryBinding.writingPromptTextView.setTextColor(getResources().getColor(R.color.LPromptColor));

        newStoryBinding.authorTextView.setTextColor(getResources().getColor(R.color.LtextColor));
        newStoryBinding.storyTextView.setTextColor(getResources().getColor(R.color.LtextColor));
        isDarkThemeActive = false;


    }

    private void switchToDarkTheme() {
        newStoryBinding.constraintAppLayout.setBackgroundColor(getResources().getColor(R.color.Dbackground));
        newStoryBinding.writingPromptTextView.setTextColor(getResources().getColor(R.color.DtextColor));
        newStoryBinding.authorTextView.setTextColor(getResources().getColor(R.color.DtextColor));
        newStoryBinding.storyTextView.setTextColor(getResources().getColor(R.color.DtextColor));
        isDarkThemeActive = true;

    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        newStoryBinding.storyLoadingProgressBar.setVisibility(View.VISIBLE);
        return new NetworkHelper(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        newStoryBinding.storyLoadingProgressBar.setVisibility(View.INVISIBLE);
        if(data.equals(REQUESTFAILED))
        {
            Toast.makeText(this,"There was an error.Please try again by using the next button",Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject jsonObject;
        String writingPrompt,author,storyContent,permalink;
        try {
             jsonObject =  new JSONObject(data);
             writingPrompt = jsonObject.getString("title");
             author = jsonObject.getString("author");
             storyContent = jsonObject.getString("body");
             storyPermalink = "I liked this story. Try reading "+jsonObject.getString("permalink");
             newStoryBinding.scrollView2.scrollTo(0,0);
            provider = new StoryManagerProvider();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                newStoryBinding.writingPromptTextView.setText(Html.fromHtml(writingPrompt,Html.FROM_HTML_MODE_COMPACT));
            else
                newStoryBinding.writingPromptTextView.setText(Html.fromHtml(writingPrompt));
             newStoryBinding.authorTextView.setText(author);
             if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                newStoryBinding .storyTextView.setText(Html.fromHtml(storyContent,Html.FROM_HTML_MODE_COMPACT));
             else
                 newStoryBinding.storyTextView.setText(Html.fromHtml(storyContent));
            details = new storyDetails(writingPrompt,author,storyContent,storyPermalink);
            newStoryBinding.includeView.saveButton.setEnabled(true);



        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
