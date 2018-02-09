package io.github.judeosbert.stories.data;

import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Created by judeosbert on 2/9/18.
 */

public final class StoryContract {

    private StoryContract()
    {
        //Just to prevent class initialization
    }

    public static class StoryEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "saved_stories";
        public static  final String COLUMN_PROMPT = "writing_prompt";
        public static  final String COLUMN_AUTHOR = "story_author";
        public static final String COLUMN_BODY = "story_content";
        public static final String COLUMN_PERMALINK = "story_permalink";
        public static  final String COLUMN_SAVETIME = "story_save_time";

    }
}
