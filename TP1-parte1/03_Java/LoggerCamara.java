
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerCamara {
    private final PrintWriter writer;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LoggerCamara(String fileName) throws IOException {
        this.writer = new PrintWriter(new FileWriter(fileName));
    }

    public void log(String mensaje) {
        String timestamp = LocalDateTime.now().format(dtf);
        writer.println(timestamp + " | " + mensaje);
        writer.flush();
    }

    public void cerrar() {
        writer.close();
    }
}