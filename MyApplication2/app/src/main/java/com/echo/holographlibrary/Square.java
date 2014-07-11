package com.echo.holographlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Анастасия on 28.04.14.
 */
public class Square extends View{
    private ArrayList<PieSlice> slices = new ArrayList<PieSlice>();

    private int x;
    private int y;
    public Square(Context context) {
        super(context);
    }
    public Square(Context context, AttributeSet attrs) {
    super(context, attrs);
}
    public void addSlice(PieSlice slice) {
        this.slices.add(slice);
        postInvalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        x = getWidth()/4;
        y = 40;
        Paint paint = new Paint();
        for (PieSlice slice : slices){
            paint.setColor(slice.getColor());
            canvas.drawCircle(x, y, 25, paint);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setTextSize(50);
            canvas.drawText(slice.getTitle(), x+80, y+10, paint);
            y+=70;
        }

    }
}
