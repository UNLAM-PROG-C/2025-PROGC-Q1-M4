package com.m4.multipaint.ui.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.m4.multipaint.Constants;
import com.m4.multipaint.drawing.User;


public class ColorButton extends ImageButton
{
    public ColorButton(Color color, User user, ColorsGrid grid)
    {
        super(generateStyle(color));

        ColorButton thisButton = this;
        this.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                user.setColor(color);
                grid.setSelectedButton(thisButton);
            }
        });

        this.setTransform(true);
    }

    private static ImageButton.ImageButtonStyle generateStyle(Color color)
    {
        final Pixmap pixmap = new Pixmap(Constants.COLOR_BUTTON_SIZE, Constants.COLOR_BUTTON_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        final Texture texture = new Texture(pixmap);
        final Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));

        final ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = drawable;
        return style;
    }
}
