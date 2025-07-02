import exceptions.ClientRejectedException;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionsManager
{
    private final List<ClientConnection> clients;

    private static ConnectionsManager connectionManager;

    private ConnectionsManager()
    {
        this.clients = new ArrayList<>();
    }

    public static ConnectionsManager getConnectionManager()
    {
        if (connectionManager == null)
            connectionManager = new ConnectionsManager();
        return connectionManager;
    }

    public void addClient(Socket clientSocket)
    {
        Thread acceptThread = new Thread(() ->
        {
            ClientConnection client = new ClientConnection(clientSocket);
            try
            {
                acceptConnection(client);
            } catch (ClientRejectedException e)
            {
                System.out.println("Error al aceptar nuevo cliente");
            }
        });
        acceptThread.start();
    }

    private void acceptConnection(ClientConnection clientConnection) throws ClientRejectedException
    {
        if (clients.contains(clientConnection))
        {
            clientConnection.send("User Name already in use");
            clientConnection.closeConnection();
            throw new ClientRejectedException("El cliente " + clientConnection.getClientId() + " ya esta conectado");
        }
        this.clients.add(clientConnection);
        clientConnection.send("OK");
        clientConnection.start();
    }

    public void broadcast(String message, String from)
    {
        this.clients.stream().filter(clientConnection -> !clientConnection.getClientId().equals(from)).forEach(clientConnection -> clientConnection.send(message));
    }

    public void removeClient(ClientConnection clientConnection)
    {
        this.clients.remove(clientConnection);
    }
}
