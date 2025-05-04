
import java.util.Random;

public class EventoParanormal 
{
    private static final String[] eventos = 
    {
        "Sin actividad",
        "Movimiento detectado",
        "Anomalía térmica",
        "Sombra extraña",
        "Ruido detectado"
    };

    private static final double[] probabilidades = 
    {
        0.5,
        0.7,
        0.8,
        0.9,
        1.0 
    };

    private static final Random random = new Random();

    public static String generarEvento() 
    {
        double r = random.nextDouble();
        for (int i = 0; i < probabilidades.length; i++) 
        {
            if (r < probabilidades[i]) 
            {
                return eventos[i];
            }
        }
        return eventos[0];
    }

    public static boolean esParanormal(String evento) 
    {
        return !evento.equals("Sin actividad");
    }
}