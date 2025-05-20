import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ChargeStation
{
    private final Semaphore pumpsSemaphore = new Semaphore(Constants.PUMPS_AMOUNT);

    public void chargeTruck(Truck truck) throws InterruptedException
    {
        if (!pumpsSemaphore.tryAcquire(0, TimeUnit.SECONDS)){
            System.out.println(truck+ " esperando para cargar combustible");
            this.pumpsSemaphore.acquire();
        }
        System.out.println(truck + " cargando combustible");
        Thread.sleep(Constants.FUEL_RECHARGE_TIME * Constants.SIMULATION_DELAY);
        this.pumpsSemaphore.release();
    }
}
