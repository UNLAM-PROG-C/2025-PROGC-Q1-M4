package com.m4.multipaint.ui.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.m4.multipaint.Constants;
import com.m4.multipaint.drawing.User;
import com.m4.multipaint.ui.UserInterface;

import java.util.ArrayList;
import java.util.List;

public class ColorsGrid
{

    List<ColorButton> buttons;

    public ColorsGrid(User user)
    {
        ColorButton defaultButton = new ColorButton(Color.BLACK, user, this);

        this.buttons = new ArrayList<>();
        this.buttons.add(defaultButton);
        this.buttons.add(new ColorButton(Color.BLUE, user, this));
        this.buttons.add(new ColorButton(Color.RED, user, this));
        this.buttons.add(new ColorButton(Color.YELLOW, user, this));
        this.buttons.add(new ColorButton(Color.GREEN, user, this));
        this.buttons.add(new ColorButton(Color.WHITE, user, this));

        setSelectedButton(defaultButton);
    }

    public void setSelectedButton(ColorButton selectedColor)
    {
        for (ColorButton button : this.buttons)
        {
            button.clearActions();
            if (button == selectedColor)
            {
                button.addAction(Actions.scaleTo(Constants.BUTTON_SELECTED_SCALE, Constants.BUTTON_SELECTED_SCALE, Constants.BUTTON_SELECTED_ANIMATION_DURATION));
            } else
            {
                button.addAction(Actions.scaleTo(1f, 1f, Constants.BUTTON_SELECTED_ANIMATION_DURATION));
            }
        }
    }

    public void addToUI(UserInterface ui)
    {
        for (ColorButton button : this.buttons)
        {
            ui.add(button).minSize(Constants.COLOR_BUTTON_SIZE).pad(Constants.BUTTON_PADDING);
            button.setOrigin(button.getWidth() / 2, button.getHeight() / 2);
        }
    }
}
