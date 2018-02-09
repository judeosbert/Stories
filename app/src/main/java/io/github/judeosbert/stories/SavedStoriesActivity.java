package io.github.judeosbert.stories;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import io.github.judeosbert.stories.data.DBHelperActivity;
import io.github.judeosbert.stories.data.StoryContract;
import io.github.judeosbert.stories.data.StoryManagerProvider;

public class SavedStoriesActivity extends AppCompatActivity implements SavedStoriesAdapter.SavedStoriesAdapterOnClickHandler {
    RecyclerView recyclerView;
    SavedStoriesAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_stories);
        recyclerView = findViewById(R.id.recyclerView);
        mAdapter = new SavedStoriesAdapter(this,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mAdapter);
        getLoadedData();


    }

    private void getLoadedData() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                StoryManagerProvider provider = new StoryManagerProvider();
                Cursor cursor = provider.query(StoryManagerProvider.CONTENT_URI,null,null,null, StoryContract.StoryEntry.COLUMN_SAVETIME+" DESC" );
                mAdapter.swapCursor(cursor);
            }
        };
        runnable.run();
    }


    @Override
    public void onClick(long id) {

    }
}
