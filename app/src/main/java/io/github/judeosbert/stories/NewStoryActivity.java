package io.github.judeosbert.stories;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.CompoundButton;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.judeosbert.stories.databinding.ActivityNewStoryBinding;

public class NewStoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    NetworkHelper networkHelper;
    ActivityNewStoryBinding newStoryBinding;
    private final String LOGTAG = "story-app-log-tag";
    private final int LOADERID = 200;
    private boolean isDarkThemeActive;
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
        JSONObject jsonObject;
        String writingPrompt,author,storyContent,permalink;
        try {
             jsonObject =  new JSONObject(data);
             writingPrompt = jsonObject.getString("title");
             author = jsonObject.getString("author");
             storyContent = jsonObject.getString("body");
             permalink = jsonObject.getString("permalink");
             newStoryBinding.scrollView2.scrollTo(0,0);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                newStoryBinding.writingPromptTextView.setText(Html.fromHtml(writingPrompt,Html.FROM_HTML_MODE_COMPACT));
            else
                newStoryBinding.writingPromptTextView.setText(Html.fromHtml(writingPrompt));
             newStoryBinding.authorTextView.setText(author);
             if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                newStoryBinding.storyTextView.setText(Html.fromHtml(storyContent,Html.FROM_HTML_MODE_COMPACT));
             else
                 newStoryBinding.storyTextView.setText(Html.fromHtml(storyContent));



        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
