package grandirector;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class GameWeek
{

    private final String gameWeekNumber;
    private final List<Match> matches;

    public GameWeek(String gameWeekNumber, List<Match> matches)
    {
        this.gameWeekNumber = gameWeekNumber;
        this.matches = matches;
    }

    public List<Match> playConcurrent(ExecutorService executor, int poolSize)
    {
        System.out.println("Jugando fecha " + this.gameWeekNumber);
        CountDownLatch latch = new CountDownLatch(poolSize);

        for (Match match : matches)
        {
            executor.submit(() ->
            {
                match.run();
                latch.countDown();
            });
        }

        try
        {
            latch.await();
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        return matches;
    }

    public List<Match> playSecuential()
    {
        System.out.println("Jugando fecha " + this.gameWeekNumber);
        for (Match match : matches)
        {
            match.play();
        }

        return matches;
    }

    @Override
    public String toString()
    {
        String text = "Fecha " + gameWeekNumber + ":\n";
        for (Match match : matches)
        {
            text = text.concat(match.toString() + "\n");
        }
        return text;
    }

    public List<Match> getMatches()
    {
        return matches;
    }
}
