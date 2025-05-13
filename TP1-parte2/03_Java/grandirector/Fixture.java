package grandirector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fixture
{

    private final List<Team> teams;
    private List<GameWeek> weeks;

    public Fixture(List<Team> teams)
    {
        this.teams = teams;
        generateMatches();
    }

    private void generateMatches()
    {
        List<Team> teamsAux = new ArrayList<>(teams);
        Collections.shuffle(teamsAux);
        int teamsSize = teamsAux.size();
        int numRounds = teamsSize - 1;
        int teamsSizeHalf = teamsSize / 2;

        this.weeks = new ArrayList<>();

        for (int round = 0; round < numRounds; round++)
        {
            List<Match> weekMatches = new ArrayList<>();
            for (int i = 0; i < teamsSizeHalf; i++)
            {
                Team home = teamsAux.get(i);
                Team away = teamsAux.get(teamsSize - 1 - i);

                if (round % 2 == 0)
                {
                    weekMatches.add(new Match(home, away));
                } else
                {
                    weekMatches.add(new Match(away, home));
                }
            }

            weeks.add(new GameWeek(Integer.toString(round + 1), weekMatches));

            Team fixed = teamsAux.get(0);
            teamsAux.remove(0);
            teamsAux.add(1, fixed);
            Collections.rotate(teamsAux.subList(1, teamsSize), 1);
        }
    }

    @Override
    public String toString()
    {
        String text = "--\n";
        for (GameWeek week : weeks)
        {
            text = text.concat(week.toString()).concat("--\n");
        }
        return text;
    }

    public List<Team> getTeams()
    {
        return teams;
    }

    public List<GameWeek> getWeeks()
    {
        return weeks;
    }
}
