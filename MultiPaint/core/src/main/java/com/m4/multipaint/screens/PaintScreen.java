package com.m4.multipaint.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.m4.multipaint.MultiPaint;
import com.m4.multipaint.drawing.*;
import com.m4.multipaint.networking.ServerConnection;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.m4.multipaint.ui.UserInterface;


public class PaintScreen implements Screen
{
    private final MultiPaint game;
    private final DrawSession session;
    private final User localUser;
    private Vector2 lastDrawPosition;
    private final Stage stage;
    private final ServerConnection serverConnection;
    private boolean wasLeftButtonPressed = false;

    private Vector2 shapeStartPosition;


    public PaintScreen(MultiPaint game, String userName, ServerConnection serverConnection)
    {
        this.game = game;

        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.serverConnection = serverConnection;


        int canvasWidth = Gdx.graphics.getDisplayMode().width;
        int canvasHeight = Gdx.graphics.getDisplayMode().height;

        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        this.session = new DrawSession(canvas, serverConnection);

        this.localUser = new User(userName, Color.BLACK);
        this.session.addUser(localUser);

        stage.addActor(new UserInterface(localUser));

        serverConnection.start(); //Lanzo hilo para escuchar desde el server
    }

    private void renderShapePreview()
    {
        if (shapeStartPosition != null && Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
            Vector2 currentPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.unproject(currentPos);

            // Usar ShapeRenderer para dibujar la previsualización
            game.shapeRenderer.setProjectionMatrix(game.viewport.getCamera().combined);
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            game.shapeRenderer.setColor(localUser.getColor().r, localUser.getColor().g,
                localUser.getColor().b, 0.5f); // Semi-transparente

            switch (localUser.getCurrentTool())
            {
                case LINE:
                    game.shapeRenderer.line(shapeStartPosition.x, shapeStartPosition.y,
                        currentPos.x, currentPos.y);
                    break;
                case RECTANGLE:
                    float minX = Math.min(shapeStartPosition.x, currentPos.x);
                    float maxX = Math.max(shapeStartPosition.x, currentPos.x);
                    float minY = Math.min(shapeStartPosition.y, currentPos.y);
                    float maxY = Math.max(shapeStartPosition.y, currentPos.y);
                    game.shapeRenderer.rect(minX, minY, maxX - minX, maxY - minY);
                    break;
                case CIRCLE:
                    float radius = shapeStartPosition.dst(currentPos);
                    game.shapeRenderer.circle(shapeStartPosition.x, shapeStartPosition.y, radius);
                    break;
            }

            game.shapeRenderer.end();
        }
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        updateBrushSettings();
        handleInput();

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();
        session.getCanvas().render(game.batch);
        game.batch.end();

        // Renderizar previsualización de forma
        renderShapePreview();

        stage.act(delta);
        stage.draw();
    }

    private void handleInput()
    {
        boolean isLeftButtonPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            Vector2 current = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.project(current);

            if (localUser.getCurrentTool() != DrawingTool.BRUSH)
            {
                shapeStartPosition = new Vector2(current);
                game.viewport.unproject(shapeStartPosition);
            }
        }

        if (Gdx.input.isTouched() && isLeftButtonPressed)
        {
            Vector2 current = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.project(current);

            if (localUser.getCurrentTool() == DrawingTool.BRUSH)
            {
                DrawAction action;
                if (lastDrawPosition != null)
                {
                    action = new DrawAction(localUser.getColor(), localUser.getBrushSize(), (int) lastDrawPosition.x, (int) lastDrawPosition.y, (int) current.x, (int) current.y);
                } else
                {
                    action = new DrawAction(localUser.getColor(), localUser.getBrushSize(), (int) current.x, (int) current.y, (int) current.x, (int) current.y);
                }
                session.applyAction(action);
                lastDrawPosition = current;
            }
        }

        // Detectar cuando se suelta el botón
        if (wasLeftButtonPressed && !isLeftButtonPressed && shapeStartPosition != null)
        {
            Vector2 current = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.project(current);

            createShapeAction(shapeStartPosition, current);
            shapeStartPosition = null;
        }

        if (!Gdx.input.isTouched())
        {
            lastDrawPosition = null;
        }

        wasLeftButtonPressed = isLeftButtonPressed;
    }

    private void createShapeAction(Vector2 start, Vector2 end)
    {
        game.viewport.unproject(start);
        game.viewport.project(end);
        switch (localUser.getCurrentTool())
        {
            case LINE:
                DrawAction lineAction = new DrawAction(this.localUser.getColor(), localUser.getBrushSize(), (int) start.x, (int) start.y, (int) end.x, (int) end.y);
                session.applyAction(lineAction);
                break;
            case RECTANGLE:
                drawRectangle(start, end);
                break;
            case CIRCLE:
                drawCircle(start, end);
                break;
        }
    }

    private void drawRectangle(Vector2 start, Vector2 end)
    {
        int minX = (int) Math.min(start.x, end.x);
        int maxX = (int) Math.max(start.x, end.x);
        int minY = (int) Math.min(start.y, end.y);
        int maxY = (int) Math.max(start.y, end.y);

        // Dibujar los cuatro lados del rectángulo
        DrawAction topSide = new DrawAction(localUser.getColor(), localUser.getBrushSize(), minX, maxY, maxX, maxY);
        DrawAction bottomSide = new DrawAction(localUser.getColor(), localUser.getBrushSize(), minX, minY, maxX, minY);
        DrawAction leftSide = new DrawAction(localUser.getColor(), localUser.getBrushSize(), minX, minY, minX, maxY);
        DrawAction rightSide = new DrawAction(localUser.getColor(), localUser.getBrushSize(), maxX, minY, maxX, maxY);

        session.applyAction(topSide);
        session.applyAction(bottomSide);
        session.applyAction(leftSide);
        session.applyAction(rightSide);

        serverConnection.sendActionToServer(topSide);
        serverConnection.sendActionToServer(bottomSide);
        serverConnection.sendActionToServer(leftSide);
        serverConnection.sendActionToServer(rightSide);
    }

    private void updateBrushSettings()
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
        {
            localUser.incrementBrushSize();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
        {
            localUser.reduceBrushSize();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.B))
        {
            this.localUser.setCurrentTool(DrawingTool.BRUSH);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L))
        {
            this.localUser.setCurrentTool(DrawingTool.LINE);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R))
        {
            this.localUser.setCurrentTool(DrawingTool.RECTANGLE);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O))
        {
            this.localUser.setCurrentTool(DrawingTool.CIRCLE);
        }
    }

    private void drawCircle(Vector2 start, Vector2 end)
    {
        int centerX = (int) start.x;
        int centerY = (int) start.y;
        int radius = (int) start.dst(end);

        // Aproximar círculo con múltiples líneas
        int segments = Math.max(16, radius / 2);
        Vector2 prevPoint = null;

        for (int i = 0; i <= segments; i++)
        {
            double angle = (2 * Math.PI * i) / segments;
            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY + (int) (radius * Math.sin(angle));

            Vector2 currentPoint = new Vector2(x, y);

            if (prevPoint != null)
            {
                DrawAction circleSegment = new DrawAction(localUser.getColor(), localUser.getBrushSize(), (int) prevPoint.x, (int) prevPoint.y, x, y);
                session.applyAction(circleSegment);
                serverConnection.sendActionToServer(circleSegment);
            }

            prevPoint = currentPoint;
        }
    }

    @Override
    public void resize(int width, int height)
    {
        game.viewport.update(width, height, false);
        stage.getViewport().update(width, height, true);

        Canvas oldCanvas = session.getCanvas();
        Canvas newCanvas = new Canvas(width, height);

        // Obtener dimensiones del pixmap del canvas anterior
        int oldWidth = oldCanvas.getPixmap().getWidth();
        int oldHeight = oldCanvas.getPixmap().getHeight();

        // Dibujar el canvas anterior escalado al nuevo tamaño
        newCanvas.getPixmap().drawPixmap(oldCanvas.getPixmap(),
            0, 0, oldWidth, oldHeight,
            0, 0, width, height);

        session.setCanvas(newCanvas);

        oldCanvas.dispose();
    }

    @Override
    public void show()
    {
    }

    @Override
    public void hide()
    {
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
    public void dispose()
    {
        session.getCanvas().dispose();
        stage.dispose();
        serverConnection.disconnect();
    }
}
