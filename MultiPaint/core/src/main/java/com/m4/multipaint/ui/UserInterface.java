package com.m4.multipaint.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.m4.multipaint.Constants;
import com.m4.multipaint.drawing.User;
import com.m4.multipaint.ui.buttons.*;

public class UserInterface extends Table implements Disposable
{
    private final Skin skin;
    private final User user;

    public UserInterface(User user)
    {
        this.skin = new Skin(Gdx.files.internal(Constants.UI_SKIN_FILE_NAME));
        this.user = user;
        setupUI();

        this.setWidth(Constants.SCREEN_RESOLUTION_W);
        this.setHeight(Constants.UI_HEIGHT); // o la altura que necesites
        this.setPosition(0, 0); // Abajo de la pantalla
    }

    private void setupUI()
    {
        final Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.fill();
        this.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture((pixmap)))));
        FullScreenButton fullScreenButton = new FullScreenButton(skin, this);

        IncreaseBrushButton increaseBrushButton = new IncreaseBrushButton(skin, user);

        DecreaseBrushButton decreaseBrushButton = new DecreaseBrushButton(skin, user);

        ToolsGrid toolsGrid = new ToolsGrid(user, skin);

        ColorsGrid colorsGrid = new ColorsGrid(user);

        this.add(fullScreenButton).width(Constants.BUTTON_WIDTH_BIG).height(Constants.BUTTON_HEIGHT_NORMAL).pad(Constants.BUTTON_PADDING);
        this.add(increaseBrushButton).width(Constants.BUTTON_WIDTH_NORMAL).height(Constants.BUTTON_HEIGHT_NORMAL).pad(Constants.BUTTON_PADDING);
        this.add(decreaseBrushButton).width(Constants.BUTTON_WIDTH_NORMAL).height(Constants.BUTTON_HEIGHT_NORMAL).pad(Constants.BUTTON_PADDING);

        toolsGrid.addToUI(this);

        colorsGrid.addToUI(this);
    }

    @Override
    public void dispose()
    {
        skin.dispose();
    }

    public void setFullScreenMode()
    {
        this.setWidth(Gdx.graphics.getWidth());
    }

    public void setWindowedMode()
    {
        this.setWidth(Constants.SCREEN_RESOLUTION_W);
    }
}
