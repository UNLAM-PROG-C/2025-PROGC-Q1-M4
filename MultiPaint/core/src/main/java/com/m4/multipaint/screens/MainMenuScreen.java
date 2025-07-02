package com.m4.multipaint.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.m4.multipaint.Constants;
import com.m4.multipaint.MultiPaint;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.m4.multipaint.networking.ServerConnection;


public class MainMenuScreen implements Screen
{

    final MultiPaint game;
    private final Stage stage;
    private final Skin skin;

    public MainMenuScreen(MultiPaint game)
    {

        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal(Constants.UI_SKIN_FILE_NAME));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label ipLabel = new Label("IP", skin);
        TextField ipField = new TextField(Constants.DEFAULT_IP, skin);

        Label portLabel = new Label("Puerto", skin);
        TextField portField = new TextField(Constants.DEFAULT_PORT, skin);

        Label userNameLabel = new Label("Nombre", skin);
        TextField userNameField = new TextField(Constants.DEFAULT_USER_NAME, skin);

        TextButton startButton = new TextButton("Empieza a dibujar", skin);
        startButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                String ip = ipField.getText();
                String portText = portField.getText();
                String userName = userNameField.getText();

                if (!isValidIp(ip))
                {
                    showErrorDialog("La direccion IP no es válida.");
                    return;
                }

                if (!isValidPort(portText))
                {
                    showErrorDialog("El puerto debe ser un numero entre 1 y 65535.");
                    return;
                }

                int port = Integer.parseInt(portText);

                new Thread(() ->
                {
                    try
                    {
                        startButton.setDisabled(true);
                        startButton.setText("Conectando");
                        ServerConnection testConnection = new ServerConnection(userName, ip, port);
                        testConnection.connect();
                        String response = testConnection.readNextMessage();
                        if (!response.equals("OK"))
                        {
                            Gdx.app.postRunnable(() ->
                            {
                                showErrorDialog(response);
                                startButton.setDisabled(false);
                                startButton.setText("Empieza a dibujar");
                            });
                        } else if (testConnection.isConnected())
                        {
                            Gdx.app.postRunnable(() ->
                            {
                                game.setScreen(new PaintScreen(game, userName, testConnection));
                                dispose();
                            });
                        } else
                        {
                            Gdx.app.postRunnable(() ->
                            {
                                showErrorDialog("No se pudo conectar al servidor, revise los datos de conexion.");
                                startButton.setDisabled(false);
                                startButton.setText("Empieza a dibujar");
                            });
                        }
                    } catch (Exception e)
                    {
                        Gdx.app.postRunnable(() ->
                        {
                            showErrorDialog("No se pudo conectar al servidor, revise los datos de conexion.");
                            startButton.setDisabled(false);
                            startButton.setText("Empieza a dibujar");
                        });
                    }
                }).start();
            }
        });

        table.add(ipLabel);
        table.add(ipField).width(200).pad(10).row();

        table.add(portLabel);
        table.add(portField).width(200).pad(10).row();

        table.add(userNameLabel);
        table.add(userNameField).width(200).pad(10).row();

        table.add(startButton).colspan(2).center();
    }


    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.font.getData().setScale(1.5f);

        GlyphLayout layout1 = new GlyphLayout(game.font, "Bienvenido a MultiPaint!!!");
        GlyphLayout layout2 = new GlyphLayout(game.font, "Completa los campos para iniciar");

        float x1 = (game.viewport.getWorldWidth() - layout1.width) / 2;
        float x2 = (game.viewport.getWorldWidth() - layout2.width) / 2;

        float baseY = (game.viewport.getWorldHeight() * 0.75f);
        float spacing = layout1.height * 2;

        game.batch.begin();
        game.font.draw(game.batch, "Bienvenido a MultiPaint!!!", x1, baseY);
        game.font.draw(game.batch, "Completa los campos para iniciar", x2, baseY - spacing);
        game.batch.end();

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height)
    {
        game.viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        stage.dispose();
        skin.dispose();
    }

    private boolean isValidIp(String ip)
    {
        //https://stackoverflow.com/questions/5284147/validating-ipv4-addresses-with-regexp
        return ip.matches(Constants.IP_VALIDATION_REGEX);
    }

    private boolean isValidPort(String portText)
    {
        try
        {
            int port = Integer.parseInt(portText);
            return port >= Constants.MIN_PORT && port <= Constants.MAX_PORT;
        } catch (NumberFormatException e)
        {
            return false;
        }
    }

    private void showErrorDialog(String message)
    {
        Dialog dialog = new Dialog("Error", skin);
        dialog.text(message);
        dialog.button("OK");
        dialog.show(stage);
    }
}
