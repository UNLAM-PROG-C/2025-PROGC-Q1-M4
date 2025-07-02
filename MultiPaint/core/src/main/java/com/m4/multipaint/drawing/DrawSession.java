package com.m4.multipaint.drawing;

import com.m4.multipaint.networking.ServerConnection;

import java.util.HashMap;
import java.util.Map;

public class DrawSession
{
    private Canvas canvas;
    private final Map<String, User> users;
    private final ServerConnection serverConnection;

    public DrawSession(Canvas canvas, ServerConnection serverConnection)
    {
        this.canvas = canvas;
        this.users = new HashMap<>();
        if (serverConnection != null)
        {
            serverConnection.setDrawSession(this);
        }
        this.serverConnection = serverConnection;
    }

    public void addUser(User user)
    {
        users.put(user.getId(), user);
    }

    public void applyAction(DrawAction action)
    {
        action.apply(canvas);
        if (this.serverConnection != null)
            serverConnection.sendActionToServer(action);
    }

    public void applyRemoteAction(DrawAction action)
    {
        action.apply(canvas);
    }

    public Canvas getCanvas()
    {
        return canvas;
    }

    public User getUser(String id)
    {
        return users.get(id);
    }

    public void setCanvas(Canvas newCanvas)
    {
        this.canvas = newCanvas;
    }
}
