import java.io.*;
import java.net.*;

public class PaintServer
{
    private ServerSocket serverSocket;
    private final ConnectionsManager connectionsManager = ConnectionsManager.getConnectionManager();

    public void start(int port) throws IOException
    {
        System.out.println("Esperando conexiones...");
        serverSocket = new ServerSocket(port);
        while (true)
        {
            Socket socket = serverSocket.accept();
            connectionsManager.addClient(socket);
        }
    }

    public static void main(String[] args) throws IOException
    {

        PaintServer server = new PaintServer();
        server.start(Constants.DEFAULT_PORT);
    }
}
