package com.streakup.app.ui.addhabit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;

public class ColorAdapter extends BaseAdapter {
    private final Context context;
    private final String[] colors;
    private final OnColorClickListener listener;

    public interface OnColorClickListener {
        void onColorClick(String color);
    }

    public ColorAdapter(Context ctx, String[] colors, OnColorClickListener l) {
        this.context = ctx;
        this.colors = colors;
        this.listener = l;
    }

    @Override public int getCount() { return colors.length; }
    @Override public Object getItem(int i) { return colors[i]; }
    @Override public long getItemId(int i) { return i; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = new View(context);
        int size = (int) (40 * context.getResources().getDisplayMetrics().density);
        v.setLayoutParams(new GridView.LayoutParams(size, size));
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.OVAL);
        gd.setColor(Color.parseColor(colors[position]));
        v.setBackground(gd);
        v.setOnClickListener(view -> listener.onColorClick(colors[position]));
        return v;
    }
}
