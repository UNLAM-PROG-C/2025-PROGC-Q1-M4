package exceptions;

import java.io.IOException;

public class ClientDisconnectedException extends IOException
{
    public ClientDisconnectedException(String errorMessage)
    {
        super(errorMessage);
    }
}
