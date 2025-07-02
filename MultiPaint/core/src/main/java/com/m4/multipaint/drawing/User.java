package com.m4.multipaint.drawing;

import com.badlogic.gdx.graphics.Color;
import com.m4.multipaint.Constants;

public class User
{
    private final String id;
    private final Brush brush;
    private DrawingTool currentTool;

    public User(String id, Color color)
    {
        this.id = id;
        this.brush = new Brush(color);
        this.currentTool = DrawingTool.BRUSH;
    }

    public String getId()
    {
        return id;
    }

    public Brush getBrush()
    {
        return brush;
    }

    public Color getColor()
    {
        return this.brush.getColor();
    }

    public void setColor(Color color)
    {
        this.brush.setColor(color);
    }

    public int getBrushSize()
    {
        return this.brush.getSize();
    }

    public void reduceBrushSize()
    {
        this.brush.setSize(this.brush.getSize() - Constants.DELTA_BRUSH_SIZE);
    }

    public void incrementBrushSize()
    {
        this.brush.setSize(this.brush.getSize() + Constants.DELTA_BRUSH_SIZE);
    }

    public void setCurrentTool(DrawingTool drawingTool)
    {
        this.currentTool = drawingTool;
    }

    public DrawingTool getCurrentTool()
    {
        return this.currentTool;
    }

}
