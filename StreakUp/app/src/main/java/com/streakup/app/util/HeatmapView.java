package com.streakup.app.util;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import java.time.LocalDate;
import java.util.*;

/**
 * GitHub-style contribution heatmap showing the last 16 weeks.
 * Each cell is a small square colored by whether the habit was done on that day.
 */
public class HeatmapView extends View {
    private static final int WEEKS = 16;
    private static final int DAYS = 7;

    private final Paint cellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Set<Long> completedDays = new HashSet<>();
    private int accentColor = 0xFF6C63FF;

    private int cellSize;
    private int cellGap = 4;

    public HeatmapView(Context context) { super(context); init(); }
    public HeatmapView(Context context, AttributeSet attrs) { super(context, attrs); init(); }
    public HeatmapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        textPaint.setTextSize(22f);
        textPaint.setColor(0xFF888888);
    }

    public void setData(List<Long> days, int color) {
        completedDays = new HashSet<>(days);
        accentColor = color;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        cellSize = (w - (WEEKS + 1) * cellGap) / WEEKS;
        int h = DAYS * (cellSize + cellGap) + cellGap + 24; // 24px for day labels
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (cellSize <= 0) return;

        LocalDate today = LocalDate.now();
        // Start from 16 weeks ago, aligned to Sunday
        LocalDate startDate = today.minusWeeks(WEEKS - 1);
        // Align to start of week (Monday)
        startDate = startDate.minusDays(startDate.getDayOfWeek().getValue() - 1);

        int alpha = Color.alpha(accentColor);
        int r = Color.red(accentColor);
        int g = Color.green(accentColor);
        int b = Color.blue(accentColor);

        for (int week = 0; week < WEEKS; week++) {
            for (int day = 0; day < DAYS; day++) {
                LocalDate date = startDate.plusWeeks(week).plusDays(day);
                if (date.isAfter(today)) continue;

                long epochDay = date.toEpochDay();
                boolean done = completedDays.contains(epochDay);

                int left = week * (cellSize + cellGap) + cellGap;
                int top = day * (cellSize + cellGap) + cellGap;

                if (done) {
                    cellPaint.setColor(accentColor);
                    cellPaint.setAlpha(220);
                } else {
                    // Light gray empty cell
                    cellPaint.setColor(0xFFE8E8E8);
                    cellPaint.setAlpha(255);
                }

                RectF rect = new RectF(left, top, left + cellSize, top + cellSize);
                canvas.drawRoundRect(rect, 3f, 3f, cellPaint);
            }
        }
    }
}
