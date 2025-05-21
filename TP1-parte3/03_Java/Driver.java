import java.util.Random;

public class Driver
{
    private final static int MIN_TRAVEL_TIME = 18;
    private final static int MAX_TRAVEL_TIME = 24;

    private final float travelTime;

    public Driver()
    {
        Random random = new Random();
        this.travelTime = (random.nextFloat() * (MAX_TRAVEL_TIME - MIN_TRAVEL_TIME)) + MIN_TRAVEL_TIME;
    }

    public float getTravelTime()
    {
        return travelTime;
    }
}
