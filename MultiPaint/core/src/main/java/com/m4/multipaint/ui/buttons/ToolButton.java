package com.m4.multipaint.ui.buttons;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.m4.multipaint.drawing.DrawingTool;
import com.m4.multipaint.drawing.User;

public class ToolButton extends TextButton
{
    public final DrawingTool drawingTool;

    public ToolButton(DrawingTool drawingTool, User user, Skin skin, ToolsGrid grid)
    {
        super(drawingTool.label, skin);
        this.drawingTool = drawingTool;
        ToolButton thisTool = this;
        this.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                user.setCurrentTool(drawingTool);
                grid.setSelectedButton(thisTool);
            }
        });
    }
}
