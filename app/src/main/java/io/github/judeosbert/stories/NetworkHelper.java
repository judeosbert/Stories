package io.github.judeosbert.stories;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by judeosbert on 2/8/18.
 */

public class NetworkHelper extends AsyncTaskLoader<String> {
    private final String SERVERIP = "http://172.16.3.165:3000/story";
    private final String REQUESTFAILED = "request-failed";
    private Context mContext;
    OkHttpClient okHttpClient = new OkHttpClient();

    public NetworkHelper(Context context) {
        super(context);
        mContext = context;
    }

    public String getJSONStory() throws IOException {
        Request request = new Request.Builder()
                .url(SERVERIP)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
//            if (response.isSuccessful())
            return response.body().string();
//                Log.d("reader-app",response.body().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


            return REQUESTFAILED;


    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public String loadInBackground() {
        String JSONBody = "test-text";

        try {
            JSONBody = getJSONStory();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return JSONBody;
    }

    @Override
    public void deliverResult(String data) {
        super.deliverResult(data);
    }
}

