package exceptions;

import java.io.IOException;

public class ClientRejectedException extends IOException
{
    public ClientRejectedException(String errorMessage)
    {
        super(errorMessage);
    }
}
