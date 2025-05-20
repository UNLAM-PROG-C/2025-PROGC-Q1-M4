import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public abstract class Plant
{
    private final String name;
    private final String loadCommodity;
    private final String unloadCommodity;
    private final Semaphore loadingSemaphore = new Semaphore(Constants.MAX_LOADING_TRUCKS);
    private final Semaphore unloadingSemaphore = new Semaphore(Constants.MAX_UNLOADING_TRUCKS);

    public Plant(String name, String loadCommodity, String unloadCommodity)
    {
        this.name = name;
        this.loadCommodity = loadCommodity;
        this.unloadCommodity = unloadCommodity;
    }

    public void action(Truck truck) throws InterruptedException
    {
        if(truck.isLoaded()){
            this.unloadTruck(truck);
        }
        this.loadTruck(truck);
    }

    private void unloadTruck(Truck truck) throws InterruptedException
    {
        if(!this.unloadingSemaphore.tryAcquire(0, TimeUnit.SECONDS)){
            System.out.println(truck + " esperando para descargar " +this.unloadCommodity + " en "+ this.name);
            this.unloadingSemaphore.acquire();
        }
        System.out.println(truck +" descargando "+this.unloadCommodity +" en "+this.name);
        truck.unload();
        this.unloadingSemaphore.release();
    }

    private void loadTruck(Truck truck) throws InterruptedException
    {
        if(!this.loadingSemaphore.tryAcquire(0, TimeUnit.SECONDS)){
            System.out.println(truck + " esperando para cargar " +this.loadCommodity + " en "+ this.name);
            this.loadingSemaphore.acquire();
        }
        System.out.println(truck +" cargando "+this.loadCommodity +" en "+this.name);
        truck.load();
        this.loadingSemaphore.release();
    }

    public String getName()
    {
        return name;
    }
}
