package grandirector;

public class Team
{
    private final String name;
    private final Integer power;

    public Team(String name, Integer power)
    {
        this.name = name;
        this.power = power;
    }

    public String getName()
    {
        return name;
    }

    public Integer getPower()
    {
        return power;
    }
}
