package com.m4.multipaint.drawing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.m4.multipaint.Constants;

public class Brush
{
    private int size;
    private Color color;

    public Brush(Color color)
    {
        this.size = Constants.DEFAULT_BRUSH_SIZE;
        this.color = color;
    }

    public void apply(Pixmap pixmap, int x, int y)
    {
        pixmap.fillCircle(x, y, size);
    }

    public int getSize()
    {
        return size;
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public void setSize(int size)
    {
        this.size = Math.max(Constants.MIN_BRUSH_SIZE, Math.min(size, Constants.MAX_BRUSH_SIZE));
    }
}
