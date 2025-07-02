package com.m4.multipaint.drawing;


import com.badlogic.gdx.graphics.Color;

public class DrawAction
{
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;
    private final Color color;
    private final int size;


    public DrawAction(Color color, int size, int startX, int startY, int endX, int endY)
    {
        this.size = size;
        this.color = color;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public void apply(Canvas canvas)
    {
        canvas.drawLine(color, size, startX, startY, endX, endY);
    }

    @Override
    public String toString()
    {
        return String.format("%s|%s|%s|%s|%s|%s", this.color, this.size, startX, startY, endX, endY);
    }
}
