package com.m4.multipaint.ui.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.m4.multipaint.Constants;
import com.m4.multipaint.ui.UserInterface;

public class FullScreenButton extends TextButton
{
    private boolean isFullScreen = false;

    public FullScreenButton(Skin skin, UserInterface userInterface)
    {
        super("Fullscreen", skin);
        this.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if (!isFullScreen)
                {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    userInterface.setFullScreenMode();
                    isFullScreen = true;
                    setText("Windowed");
                } else
                {
                    Gdx.graphics.setWindowedMode(Constants.SCREEN_RESOLUTION_W, Constants.SCREEN_RESOLUTION_H);
                    userInterface.setWindowedMode();
                    isFullScreen = false;
                    setText("Fullscreen");
                }
            }
        });
    }
}
