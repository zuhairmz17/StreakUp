package com.streakup.app.ui.addhabit;

import android.content.Context;
import android.view.*;
import android.widget.*;
import com.streakup.app.R;

public class EmojiAdapter extends BaseAdapter {
    private final Context context;
    private final String[] emojis;
    private final OnEmojiClickListener listener;

    public interface OnEmojiClickListener {
        void onEmojiClick(String emoji);
    }

    public EmojiAdapter(Context ctx, String[] emojis, OnEmojiClickListener l) {
        this.context = ctx;
        this.emojis = emojis;
        this.listener = l;
    }

    @Override public int getCount() { return emojis.length; }
    @Override public Object getItem(int i) { return emojis[i]; }
    @Override public long getItemId(int i) { return i; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = new TextView(context);
        tv.setText(emojis[position]);
        tv.setTextSize(24);
        tv.setGravity(android.view.Gravity.CENTER);
        tv.setPadding(8, 8, 8, 8);
        tv.setOnClickListener(v -> listener.onEmojiClick(emojis[position]));
        return tv;
    }
}
