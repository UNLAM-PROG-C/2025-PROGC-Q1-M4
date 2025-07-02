package com.m4.multipaint.networking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.m4.multipaint.drawing.DrawAction;
import com.m4.multipaint.drawing.DrawSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ServerConnection extends Thread
{
    String clientId;
    Socket socket;
    boolean isConnected = false;
    BufferedReader in;
    DrawSession drawSession;

    public ServerConnection(String clientId, String ip, int port)
    {
        SocketHints hints = new SocketHints();
        hints.connectTimeout = 2500;
        socket = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, port, hints);
        this.clientId = clientId;
    }

    public void connect()
    {
        try
        {
            int attempts = 0;
            int maxAttempts = 5;

            while (!socket.isConnected() && attempts < maxAttempts)
            {
                Gdx.app.log("NETWORK", "Intentando conectar... intento #" + (attempts + 1));
                Thread.sleep(500); // Esperar medio segundo antes de intentar de nuevo
                attempts++;
            }

            if (socket.isConnected())
            {
                Gdx.app.log("NETWORK", "Conectado");
                this.isConnected = true;
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                sendToServer(clientId);
            } else
            {
                Gdx.app.log("NETWORK", "No se pudo conectar al servidor");
            }
        } catch (Exception e)
        {
            Gdx.app.error("NETWORK", "Error al intentar conectar: " + e.getMessage(), e);
        }
    }

    public boolean isConnected()
    {
        return socket != null && socket.isConnected() && this.isConnected;
    }

    public void disconnect()
    {
        this.isConnected = false;
        this.interrupt();
        this.socket.dispose();
    }

    @Override
    public void run()
    {

        while (this.isConnected())
        {
            in.lines().forEach(this::processMessage);
        }
        this.disconnect();

    }

    private void processMessage(String message)
    {
        Gdx.app.log("NETWORK", "Receiving: " + message);
        if (drawSession != null)
        {
            DrawAction incomingAction = MessageParser.parseDrawAction(message);
            Gdx.app.postRunnable(() -> drawSession.applyRemoteAction(incomingAction));
        }
    }


    public void sendActionToServer(DrawAction action)
    {
        sendToServer(action.toString());
    }

    public void sendToServer(String message)
    {
        try
        {
            Gdx.app.log("NETWORK", "Sending: " + message);
            socket.getOutputStream().write((message + "\n").getBytes(StandardCharsets.UTF_8));
            socket.getOutputStream().flush();
        } catch (IOException e)
        {
            Gdx.app.log("ERROR", "Error al comunicarse con el server");
        }
    }

    public void setDrawSession(DrawSession drawSession)
    {
        this.drawSession = drawSession;
    }

    public String readNextMessage() throws Exception
    {
        return in.readLine();
    }
}
