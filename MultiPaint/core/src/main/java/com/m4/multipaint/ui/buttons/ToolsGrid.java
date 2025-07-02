package com.m4.multipaint.ui.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.m4.multipaint.Constants;
import com.m4.multipaint.drawing.DrawingTool;
import com.m4.multipaint.drawing.User;
import com.m4.multipaint.ui.UserInterface;

public class ToolsGrid
{

    private final ToolButton brushButton;
    private final ToolButton lineButton;
    private final ToolButton rectangleButton;
    private final ToolButton circleButton;

    public ToolsGrid(User user, Skin skin)
    {

        this.brushButton = new ToolButton(DrawingTool.BRUSH, user, skin, this);
        this.lineButton = new ToolButton(DrawingTool.LINE, user, skin, this);
        this.rectangleButton = new ToolButton(DrawingTool.RECTANGLE, user, skin, this);
        this.circleButton = new ToolButton(DrawingTool.CIRCLE, user, skin, this);

        this.setSelectedButton(brushButton);


    }

    public void setSelectedButton(ToolButton selectedButton)
    {
        brushButton.setColor(brushButton == selectedButton ? Color.LIGHT_GRAY : Color.WHITE);
        lineButton.setColor(lineButton == selectedButton ? Color.LIGHT_GRAY : Color.WHITE);
        rectangleButton.setColor(rectangleButton == selectedButton ? Color.LIGHT_GRAY : Color.WHITE);
        circleButton.setColor(circleButton == selectedButton ? Color.LIGHT_GRAY : Color.WHITE);
    }

    public void addToUI(UserInterface UI)
    {
        UI.add(brushButton).width(Constants.BUTTON_WIDTH_NORMAL).height(Constants.BUTTON_HEIGHT_NORMAL).pad(Constants.BUTTON_PADDING);
        UI.add(lineButton).width(Constants.BUTTON_WIDTH_NORMAL).height(Constants.BUTTON_HEIGHT_NORMAL).pad(Constants.BUTTON_PADDING);
        UI.add(rectangleButton).width(Constants.BUTTON_WIDTH_NORMAL).height(Constants.BUTTON_HEIGHT_NORMAL).pad(Constants.BUTTON_PADDING);
        UI.add(circleButton).width(Constants.BUTTON_WIDTH_NORMAL).height(Constants.BUTTON_HEIGHT_NORMAL).pad(Constants.BUTTON_PADDING);
    }
}
