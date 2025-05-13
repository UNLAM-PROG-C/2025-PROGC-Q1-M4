package grandirector;

public class Stats implements Comparable<Stats>
{
    int points = 0;
    int played = 0;
    int won = 0;
    int tied = 0;
    int lost = 0;
    int goalsInFavor = 0;
    int goalsAgainst = 0;
    int goalsDiff = 0;

    public void addVictory(MatchResult result)
    {
        this.played++;
        this.won++;
        this.points += 3;
        this.goalsInFavor += Math.max(result.getGoalsAway(), result.getGoalsHome());
        this.goalsAgainst += Math.min(result.getGoalsAway(), result.getGoalsHome());
        this.goalsDiff += Math.abs(result.getGoalsAway() - result.getGoalsHome());
    }

    public void addLost(MatchResult result)
    {
        this.played++;
        this.lost++;
        this.goalsInFavor += Math.min(result.getGoalsAway(), result.getGoalsHome());
        this.goalsAgainst += Math.max(result.getGoalsAway(), result.getGoalsHome());
        this.goalsDiff -= Math.abs(result.getGoalsAway() - result.getGoalsHome());
    }

    public void addTie(MatchResult result)
    {
        this.played++;
        this.tied++;
        this.points += 1;
        this.goalsInFavor += result.getGoalsAway();
        this.goalsAgainst += result.getGoalsAway();
    }

    @Override
    public int compareTo(Stats otherStats)
    {
        int result = Integer.compare(this.points, otherStats.points);
        if (result != 0)
        {
            return result;
        }

        result = Integer.compare(this.goalsDiff, otherStats.goalsDiff);
        if (result != 0)
        {
            return result;
        }

        return Integer.compare(this.goalsInFavor, otherStats.goalsInFavor);
    }

    public int getPoints()
    {
        return points;
    }

    public int getGoalsDiff()
    {
        return goalsDiff;
    }

    public int getGoalsAgainst()
    {
        return goalsAgainst;
    }

    public int getGoalsInFavor()
    {
        return goalsInFavor;
    }

    public int getLost()
    {
        return lost;
    }

    public int getTied()
    {
        return tied;
    }

    public int getWon()
    {
        return won;
    }

    public int getPlayed()
    {
        return played;
    }
}
