
import java.io.*;
import java.util.*;

public class MansionDercetoMonitor {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java MansionDercetoMonitor  ");
            System.exit(1);
        }

        int duracion = 0, frecuencia = 0;

        try {
            duracion = Integer.parseInt(args[0]);
            frecuencia = Integer.parseInt(args[1]);

            if (duracion <= 0 || frecuencia <= 0) {
                System.err.println("Duración y frecuencia deben ser mayores que cero.");
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            System.err.println("La duración y frecuencia deben ser enteros.");
            System.exit(1);
        }

         String[] zonas = {
            "Sotano",
            "Atico",
            "Cocina",
            "Dormitorio",
            "Jardin",
            "Mausoleo"
        };

        List<Process> procesos = new ArrayList<>();
        List<String> logFiles = new ArrayList<>();

        for (int i = 0; i < zonas.length; i++) {
            String zona = zonas[i];
            String logFile = "camara_" + (i + 1) + "_" + zona + ".log";
            logFiles.add(logFile);

            try {
                ProcessBuilder pb = new ProcessBuilder(
                        "java", "CamaraProceso",
                        String.valueOf(i + 1),
                        zona,
                        String.valueOf(duracion),
                        String.valueOf(frecuencia)
                );
                pb.directory(new File("."));
                Process proceso = pb.start();
                procesos.add(proceso);
            } catch (IOException e) {
                System.err.println("Error iniciando camara " + (i + 1));
                e.printStackTrace();
            }
        }

        // Esperar a que terminen todas las camaras
        for (Process proceso : procesos) {
            try {
                proceso.waitFor();
            } catch (InterruptedException e) {
                System.err.println("Error esperando una camara.");
                e.printStackTrace();
            }
        }

         System.out.println("\nMonitoreo finalizado. Lectura de logs:");

        System.out.println("\n======= LOG DE TODAS LAS CaMARAS =======\n");
        for (String file : logFiles) {
            System.out.println(">>> " + file);
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    System.out.println(linea);
                }
            } catch (IOException e) {
                System.err.println("No se pudo leer el archivo: " + file);
            }
            System.out.println(); // Espacio entre logs
        }

        System.out.println("Monitoreo completo.");
    }
}