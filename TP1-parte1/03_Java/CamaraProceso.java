
public class CamaraProceso 
{
    public static void main(String[] args) 
    {
        if (args.length < 4) 
        {
            System.out.println("Uso: java CamaraProceso    ");
            System.exit(1);
        }

        int id = Integer.parseInt(args[0]);
        String zona = args[1];
        int duracion = Integer.parseInt(args[2]);
        int frecuencia = Integer.parseInt(args[3]);

        if (duracion <= 0 || frecuencia <= 0) 
        {
            System.err.println("Duración y frecuencia deben ser mayores que cero.");
            System.exit(1);
        }

        int eventosParanormales = 0;
        String logFile = "camara_" + id + "_" + zona + ".log";

        try 
        {
            LoggerCamara logger = new LoggerCamara(logFile);
            long inicio = System.currentTimeMillis();
            long tiempoTotal = duracion * 1000L;

            while ((System.currentTimeMillis() - inicio) < tiempoTotal) 
            {
                String evento = EventoParanormal.generarEvento();
                logger.log("CÁMARA " + id + " | ZONA: " + zona + " | EVENTO: " + evento);

                if (EventoParanormal.esParanormal(evento)) 
                {
                    eventosParanormales++;
                }

                try 
                {
                    Thread.sleep(frecuencia * 1000L);
                } catch (InterruptedException e) 
                {
                    logger.log("Cámara " + id + " interrumpida.");
                    break;
                }
            }

            logger.log("CÁMARA " + id + " finaliza con " + eventosParanormales + " eventos paranormales detectados.");
            logger.cerrar();
        } catch (Exception e) 
        {
            System.err.println("Error en cámara " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}