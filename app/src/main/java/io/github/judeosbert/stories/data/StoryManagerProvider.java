package io.github.judeosbert.stories.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.URI;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * Created by judeosbert on 2/9/18.
 */

public class StoryManagerProvider extends ContentProvider {

    public static  final String AUTHORITY_URI = "io.github.judeosbert.stories";
    public static  final Uri BASE_URI = Uri.parse("content://"+AUTHORITY_URI);
    public static  final String STORIES = "stories";
    public static  final String ONESTORY = STORIES+"/#";
    private final String LOGTAG = "LOGTAG";

    private static final  int STORIES_MATCH_ID = 100;
    private static final int ONESTORY_MATCH_ID = 101;
    private DBHelperActivity dbHelper;
    public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(STORIES).build();
    private static SQLiteDatabase sqLiteDatabase;
    private static Context sContext;
    private static final UriMatcher sUriMatcher  = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY_URI,"stories",STORIES_MATCH_ID);
        sUriMatcher.addURI(AUTHORITY_URI,"stories"+"/#",ONESTORY_MATCH_ID);
    }


    @Override
    public boolean onCreate() {

        dbHelper = new DBHelperActivity(getContext());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        if(sqLiteDatabase ==  null)
        {
            Log.d(LOGTAG,"NULL DATABASE");
        }
        else
        {
            Log.d(LOGTAG,"NOT NULL DATABASE");
        }
        sContext = getContext();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor returnCursor = null;
        switch (sUriMatcher.match(uri))
        {
            case STORIES_MATCH_ID:
                returnCursor = sqLiteDatabase.query(true,StoryContract.StoryEntry.TABLE_NAME,null,null,null,null,null,s1,null);


                break;
            default:
                break;
        }
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Log.d(LOGTAG,"Inside insert with URI" +uri );// +" and cvalues"+contentValues.toString());
        Log.d(LOGTAG, String.valueOf(sUriMatcher.match(uri)));
        long id = 0;
//        dbHelper = new DBHelperActivity(getContext());
//        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri = null;
        switch(sUriMatcher.match(uri))
        {
            case STORIES_MATCH_ID:
                Log.d(LOGTAG,"Inside switch case 100");

                id = sqLiteDatabase.insert(StoryContract.StoryEntry.TABLE_NAME,null,contentValues);
                if(id > 0)
                {
                    Log.d(LOGTAG,"Dat inserted with id" +id);
                    returnUri = ContentUris.withAppendedId(CONTENT_URI,id);
                }

                break;

            default:
                Log.d(LOGTAG,"Unknown URI handler");
                Log.e(LOGTAG,"Unknown URI handler");
        }
        sContext.getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


}
