import java.io.*;
import java.net.*;

public class Client
{
    private final Socket socket;
    private final String clientId;
    private final PrintWriter out;
    private final BufferedReader in;

    public Client(Socket socket)
    {
        this.socket = socket;
        PrintWriter tempOut = null;
        BufferedReader tempIn = null;
        String tempClientId = null;

        try
        {
            tempOut = new PrintWriter(socket.getOutputStream(), true);
            tempIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            tempClientId = tempIn.readLine();
            System.out.println("Nuevo cliente: " + tempClientId);
        } catch (IOException e)
        {
            System.out.println("Error al recibir conexión: " + e.getMessage());
        }

        this.out = tempOut;
        this.in = tempIn;
        this.clientId = tempClientId;
    }

    public String getClientId()
    {
        return clientId;
    }

    public PrintWriter getWriter()
    {
        return out;
    }

    public BufferedReader getReader()
    {
        return in;
    }

    public Socket getSocket()
    {
        return socket;
    }


}
