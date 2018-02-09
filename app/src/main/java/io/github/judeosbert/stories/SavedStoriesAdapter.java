package io.github.judeosbert.stories;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.judeosbert.stories.data.StoryContract;

/**
 * Created by judeosbert on 2/9/18.
 */

public class SavedStoriesAdapter extends RecyclerView.Adapter<SavedStoriesAdapter.SaveStoriesAdapterViewHolder> {

    private  SavedStoriesAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;
    private  Context mContext;
    public interface SavedStoriesAdapterOnClickHandler{
        void onClick(long id);
    }

    public SavedStoriesAdapter(@NonNull Context context,SavedStoriesAdapterOnClickHandler clickHandler)
    {
        mContext = context;
        mClickHandler = clickHandler;

    }

    @Override
    public SaveStoriesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.saved_stories_layout,parent,false);
        view.setFocusable(true);
        return new SaveStoriesAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(SaveStoriesAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.authorTextView.setText(mCursor.getString(mCursor.getColumnIndex(StoryContract.StoryEntry.COLUMN_AUTHOR)));
        holder.storySnapTextView.setText(mCursor.getString(mCursor.getColumnIndex(StoryContract.StoryEntry.COLUMN_BODY)));
        holder.timestampTextView.setText(mCursor.getString(mCursor.getColumnIndex(StoryContract.StoryEntry.COLUMN_SAVETIME)));

        holder.authorTextView.setText("random123");

    }

    @Override
    public int getItemCount() {

        if(mCursor == null)
        {
            return 0;
        }
        return mCursor.getCount();
    }
    void swapCursor(Cursor cursor)
    {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public class SaveStoriesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView storySnapTextView,authorTextView,timestampTextView;
        public SaveStoriesAdapterViewHolder(View itemView) {
            super(itemView);
            storySnapTextView = itemView.findViewById(R.id.story_snap_text_view);
            authorTextView = itemView.findViewById(R.id.author_text_view);
            timestampTextView = itemView.findViewById(R.id.timestamp_text_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long id = (long) view.getTag();
            mClickHandler.onClick(id);

        }
    }
}
