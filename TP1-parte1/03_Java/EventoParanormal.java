
import java.util.Random;

public class EventoParanormal {
    private static final String[] eventos = {
        "Sin actividad",
        "Movimiento detectado",
        "Anomalía térmica",
        "Sombra extraña",
        "Ruido detectado"
    };

    private static final double[] probabilidades = {
        0.5,  // Sin actividad (50%)
        0.7,  // Movimiento detectado (20%)
        0.8,  // Anomalía térmica (10%)
        0.9,  // Sombra extraña (10%)
        1.0   // Ruido detectado (10%)
    };

    private static final Random random = new Random();

    public static String generarEvento() {
        double r = random.nextDouble();
        for (int i = 0; i < probabilidades.length; i++) {
            if (r < probabilidades[i]) {
                return eventos[i];
            }
        }
        return eventos[0]; // fallback
    }

    public static boolean esParanormal(String evento) {
        return !evento.equals("Sin actividad");
    }
}