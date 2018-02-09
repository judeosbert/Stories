package io.github.judeosbert.stories.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * Created by judeosbert on 2/9/18.
 */

public class DBHelperActivity extends SQLiteOpenHelper {
    private static  final String DATABASE_NAME = "savedStories.db";
    private static  final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE=
            "CREATE TABLE "+StoryContract.StoryEntry.TABLE_NAME + "("
                    +StoryContract.StoryEntry._ID+" INTEGER PRIMARY KEY,"
                    + StoryContract.StoryEntry.COLUMN_BODY+" TEXT NOT NULL,"
            + StoryContract.StoryEntry.COLUMN_AUTHOR+" TEXT NOT NULL,"
            + StoryContract.StoryEntry.COLUMN_PROMPT+" TEXT NOT NULL,"
            + StoryContract.StoryEntry.COLUMN_PERMALINK+" TEXT NOT NULL,"
            + StoryContract.StoryEntry.COLUMN_SAVETIME+" DATETIME DEFAULT_CURRENT_TIMESTAMP"
            +");";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS "+ StoryContract.StoryEntry.TABLE_NAME;

    public DBHelperActivity(Context context)
    {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
