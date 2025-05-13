package grandirector;

public class MatchResult
{
    int goalsHome;
    int goalsAway;

    public MatchResult(int goalsHome, int goalsAway)
    {
        this.goalsHome = goalsHome;
        this.goalsAway = goalsAway;
    }


    public int getGoalsAway()
    {
        return goalsAway;
    }

    public int getGoalsHome()
    {
        return goalsHome;
    }

    @Override
    public String toString()
    {
        return goalsHome + " - " + goalsAway;
    }

    public ResultType getResult()
    {
        if (goalsHome > goalsAway)
            return ResultType.HOME_WON;
        if (goalsAway > goalsHome)
            return ResultType.AWAY_WON;
        return ResultType.TIE;
    }
}
